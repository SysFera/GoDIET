package com.sysfera.godiet.remote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.graph.PathException;
import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Scratch;

/**
 * Agent configuration and remote access helper.
 * 
 * @author phi
 * 
 */
public class RemoteConfigurationHelper {
	private Logger log = LoggerFactory.getLogger(getClass());

	private RemoteAccess remoteAccess;
	private GoDietConfiguration configuration;
	private Platform platform;

	// Singleton
	private volatile static RemoteConfigurationHelper instance;

	private RemoteConfigurationHelper() {
	}

	/**
	 * Use only with JEE > 1.4 !
	 * 
	 * @return the launchhelper singleton instance.
	 */
	public static RemoteConfigurationHelper getInstance() {
		if (instance == null) {
			synchronized (RemoteConfigurationHelper.class) {
				if (instance == null) {
					instance = new RemoteConfigurationHelper();
				}
			}
		}
		return instance;
	}

	/**
	 * Prepare physical resource to launch the Software - Search the physical
	 * resource to run the diet agent - Create remote directory - Create
	 * configuration file on local scratch directory - Copy configuration files
	 * on remote physical resource
	 * 
	 * @param resource
	 * 
	 * @throws PrepareException
	 *             if create local files or can't copy files on remote host.
	 */
	public void configure(SoftwareManager resource) throws PrepareException {
		// TODO: Delete this check when Spring IOC configuration will be done
		if (remoteAccess == null || configuration == null || platform == null) {
			log.error("Unable to configure remote resource. Remote helper isn't correctly initialized");
			throw new PrepareException("Remote configurator isn't ready");
		}

		// the remote physical node to configure
		Node remoteNode = resource.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new PrepareException(
					"Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration.getLocalNode());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new PrepareException("Unable to find the resource: "
					+ configuration.getLocalNode());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath((Node) localNode, remoteNode);
		} catch (PathException e1) {
			throw new PrepareException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new PrepareException("Path node found");
		}

		String command = "";
		try {
			// Create Remote Directory
			// TODO: Do that in a init platform method
			command = "mkdir -p " + remoteNode.getDisk().getScratch().getDir();
			remoteAccess.launch(command, path);

			// Create local config file
			// TODO: not to be here ?
			File file = createConfigFile(resource);

			// Copy file on remote host
			remoteAccess.copy(file, remoteNode.getDisk().getScratch().getDir(),
					path);
		} catch (RemoteAccessException e) {
			log.error("Unable to configure "
					+ resource.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " commmand " + command, e);
			throw new PrepareException("Unable to run configure "
					+ resource.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " .Commmand: " + command, e);
		}

	}

	/**
	 * 
	 * Create the resource configuration file on local scratch directory
	 * 
	 * TODO: Move this code in prepare app
	 * 
	 * @param resource
	 * @throws PrepareException
	 *             if unable write on local scratch directory
	 */
	private File createConfigFile(SoftwareManager resource)
			throws PrepareException {
		Scratch scratch = configuration.getLocalscratch();
		File file = new File(scratch.getDir());
		if (!file.exists()) {

			if (!file.mkdirs()) {
				throw new PrepareException(
						"Unable to create local directories "
								+ scratch.getDir());
			}
		}
		String filename = getConfigFileName(resource);
		File retFile = new File(scratch.getDir() + "/" + filename);

		BufferedWriter writerFile = null;
		try {

			writerFile = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(retFile)));
			Options options = resource.getSoftwareDescription().getCfgOptions();

			if (options != null) {
				for (Option option : options.getOption()) {
					writerFile.write(option.getKey() + " = "
							+ option.getValue());
					writerFile.newLine();
				}
			}
			writerFile.flush();
			writerFile.close();
		} catch (FileNotFoundException e) {
			throw new PrepareException("Unable to create file "
					+ scratch.getDir() + filename, e);
		} catch (IOException e) {
			throw new PrepareException("Unable to write on file "
					+ scratch.getDir() + filename, e);
		} finally {
			if (writerFile != null)
				try {
					writerFile.close();
				} catch (IOException e) {
				}

		}
		return retFile;
	}

	/**
	 * Launch software on the physical resource - Search the physical resource
	 * to run the diet agent - Launch command
	 * 
	 * @param managedSofware
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	public void launch(SoftwareManager managedSofware) throws LaunchException {
		// TODO: Delete this check when Spring IOC configuration will be done
		if (remoteAccess == null || configuration == null || platform == null) {
			log.error("Unable to configure remote resource. Remote helper isn't correctly initialized");
			throw new LaunchException("Remote configurator isn't ready");
		}
		// the remote physical node to configure
		Node remoteNode = managedSofware.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new LaunchException(
					"Resource not plugged on physial resource");
		}
		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration.getLocalNode());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new LaunchException(
					"Unable to find the resource from which the remote command is call");
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath((Node) localNode, remoteNode);
		} catch (PathException e1) {
			throw new LaunchException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new LaunchException("Path node found");
		}

		// End of duplicate code

		String command = RemoteCommandBuilder.buildRunCommand(managedSofware);
		try {
			Integer pid = remoteAccess.launch(command, path);
			managedSofware.setPid(pid);
			log.info("Command " + command + " run with pid " + pid);
		} catch (RemoteAccessException e) {
			log.error("Unable to configure "
					+ managedSofware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " commmand " + command, e);
			throw new LaunchException("Unable to run configure "
					+ managedSofware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " .Commmand: " + command, e);
		}

	}

	/**
	 * Stop software on the physical resource - Search the physical resource to
	 * stop the diet agent - Launch stop command (actually kill but hope change
	 * :) )
	 * 
	 * @param resource
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	public void stop(SoftwareManager resource) throws StopException {
		// TODO: Delete this check when Spring IOC configuration will be done
		// TODO: Duplicate code with configure, start
		if (remoteAccess == null || configuration == null || platform == null) {
			log.error("Unable to configure remote resource. Remote helper isn't correctly initialized");
			throw new StopException("Remote configurator isn't ready");
		}

		// the remote physical node to configure
		Node remoteNode = resource.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new StopException("Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration.getLocalNode());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new StopException("Unable to find the resource: "
					+ configuration.getLocalNode());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath((Node) localNode, remoteNode);
		} catch (PathException e1) {
			throw new StopException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new StopException("Path node found");
		}

		Integer pid = resource.getPid();
		if (pid == null)
			throw new StopException("Unable to kill "
					+ resource.getSoftwareDescription().getId()
					+ ". Pid is null");
		
		
		//End of duplicate code
		String command = "kill " + pid;

		try {
			remoteAccess.launch(command, path);
			
			//TODO: Check if always run
		} catch (RemoteAccessException e) {
			throw new StopException("Unable to kill "
					+ resource.getSoftwareDescription().getId(), e);
		}

	}

	/**
	 * Check if the software running
	 * @param managed
	 */
	public void check(SoftwareManager resource) throws CheckException
	{
		// TODO: Delete this check when Spring IOC configuration will be done
		// TODO:  99% Duplicate code with  stop :) GENIAL !
		if (remoteAccess == null || configuration == null || platform == null) {
			log.error("Unable to configure remote resource. Remote helper isn't correctly initialized");
			throw new CheckException("Remote configurator isn't ready");
		}

		// the remote physical node to configure
		Node remoteNode = resource.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new CheckException("Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration.getLocalNode());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new CheckException("Unable to find the resource: "
					+ configuration.getLocalNode());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath((Node) localNode, remoteNode);
		} catch (PathException e1) {
			throw new CheckException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new CheckException("Path node found");
		}

		Integer pid = resource.getPid();
		if (pid == null)
			throw new CheckException("Unable to check "
					+ resource.getSoftwareDescription().getId()
					+ ". Pid is null");
		
		
		//End of duplicate code
		String command = "kill -0 " + pid;

		try {
			remoteAccess.check(command, path);
			
			//TODO: Check if always run
		} catch (RemoteAccessException e) {
			throw new CheckException("Unable to check "
					+ resource.getSoftwareDescription().getId(), e);
		}

	}
	public void setRemoteAccess(RemoteAccess remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	public void setConfiguration(GoDietConfiguration configuration) {
		this.configuration = configuration;
	}

	private String getFileName(SoftwareManager resource) {
		return resource.getSoftwareDescription().getId();
	}

	private String getConfigFileName(SoftwareManager resource) {
		return getFileName(resource) + ".cfg";
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
}
