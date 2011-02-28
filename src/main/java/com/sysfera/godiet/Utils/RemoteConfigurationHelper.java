package com.sysfera.godiet.Utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Model.xml.generated.GoDietConfiguration;
import com.sysfera.godiet.Model.xml.generated.Node;
import com.sysfera.godiet.Model.xml.generated.Scratch;
import com.sysfera.godiet.Model.xml.generated.Ssh;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;
import com.sysfera.godiet.exceptions.RemoteAccessException;

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
			throw new PrepareException("Initialize the remote access and configuration first");
		}
		Node node = resource.getPluggedOn();
		Ssh sshConfig = node.getSsh();

		// Create Remote Directory

		String command = "mkdir -p " + node.getDisk().getScratch();
		try {
			remoteAccess.run(command, sshConfig.getLogin(),
					sshConfig.getServer(), sshConfig.getPort());
			createConfigFile(resource);
			
		} catch (RemoteAccessException e) {
			log.error(
					"Unable to configure "
							+ resource.getDietAgent().getId() + " on "
							+ node.getId()+" commmand "+ command, e);
			throw new PrepareException("Unable to run configure "
					+ resource.getDietAgent().getId() + " on "
					+ node.getId()
					+" .Commmand: "+ command);
		}

	}

	/**
	 * 
	 * Create the resource configuration file on local scratch directory
	 * Configuration must not be null
	 * @param resource
	 * @throws PrepareException if unable write on local scratch directory
	 */
	private void createConfigFile(DietResourceManager resource)throws PrepareException {
		Scratch scratch = configuration.getLocalscratch();
		
		if(!new File(scratch.getDir()).mkdirs())
		{
			throw new PrepareException("Unable to create local directories " + scratch.getDir());
		}
		
		//resource.getDietAgent().;
		
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
}
