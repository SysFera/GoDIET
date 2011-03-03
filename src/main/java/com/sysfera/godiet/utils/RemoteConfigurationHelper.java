package com.sysfera.godiet.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;
import com.sysfera.godiet.exceptions.RemoteAccessException;
import com.sysfera.godiet.model.DietResourceManager;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Scratch;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.generated.Options.Option;

/**
 * Agent configuration and remote access helper.
 * 
 * @author phi
 * 
 */
public class RemoteConfigurationHelper {
	private Logger log = LoggerFactory.getLogger(getClass());

	private volatile static RemoteConfigurationHelper instance;

	private RemoteAccess remoteAccess;
	private GoDietConfiguration configuration;

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
	 * Prepare physical resource to launch the diet agent - Search the physical
	 * resource to run the diet agent - Create remote directory - Create
	 * configuration file on local scratch directory - Copy configuration files
	 * on remote physical resource
	 * 
	 * @param resource
	 * 
	 * @throws PrepareException
	 *             if create local files or can't copy files on remote host.
	 */
	public void configure(DietResourceManager resource) throws PrepareException {
		if (remoteAccess == null || configuration == null) {
			log.error("Unable to configure remote resource. Remote configurator isn't corectly initialized");
			throw new PrepareException(
					"Initialize the remote access and configuration first");
		}

		Node node = resource.getPluggedOn();
		if (node == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new PrepareException(
					"Resource not plugged on physial resource");
		}
		Ssh sshConfig = node.getSsh();
		String command = "";
		try {
			// Create Remote Directory
			command = "mkdir -p " + node.getDisk().getScratch().getDir();
			remoteAccess.run(command, sshConfig.getLogin(),
					sshConfig.getServer(), sshConfig.getPort());

			// Create local config file
			File file = createConfigFile(resource);

			// Copy file on remote host
			remoteAccess.copy(file, sshConfig.getLogin(),
					sshConfig.getServer(), sshConfig.getPort());
		} catch (RemoteAccessException e) {
			log.error("Unable to configure " + resource.getDietAgent().getId()
					+ " on " + node.getId() + " commmand " + command, e);
			throw new PrepareException("Unable to run configure "
					+ resource.getDietAgent().getId() + " on " + node.getId()
					+ " .Commmand: " + command,e);
		}

	}

	/**
	 * 
	 * Create the resource configuration file on local scratch directory
	 * 
	 * 
	 * @param resource
	 * @throws PrepareException
	 *             if unable write on local scratch directory
	 */
	private File createConfigFile(DietResourceManager resource)
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
			Options options = resource.getDietAgent().getCfgOptions();

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
	 * Launch diet agent on the physical resource - Search the physical resource
	 * to run the diet agent - Launch command
	 * 
	 * @param resource
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	public void launch(DietResourceManager resource) throws LaunchException {

	}

	/**
	 * Stop diet agent on the physical resource - Search the physical resource
	 * to stop the diet agent - Launch stop command (actually kill but hope
	 * change :) )
	 * 
	 * @param resource
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	public void stop(DietResourceManager resource) throws LaunchException {

	}

	public void setRemoteAccess(RemoteAccess remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

	public void setConfiguration(GoDietConfiguration configuration) {
		this.configuration = configuration;
	}

	private String getFileName(DietResourceManager resource) {
		return resource.getDietAgent().getId();
	}

	private String getConfigFileName(DietResourceManager resource) {
		return getFileName(resource) + ".cfg";
	}
}
