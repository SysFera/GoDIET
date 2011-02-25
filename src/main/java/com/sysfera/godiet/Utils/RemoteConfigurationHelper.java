package com.sysfera.godiet.Utils;

import com.sysfera.godiet.Model.xml.DietResource;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;


/**
 * Agent configuration and remote access helper.
 * 
 * @author phi
 *
 */
public class RemoteConfigurationHelper {

	private volatile static RemoteConfigurationHelper instance;
	
	
	
	private RemoteConfigurationHelper(){}
	
	/**
	 * Use only with JEE > 1.4 ! 
	 * @return the launchhelper singleton instance. 
	 */
	public static RemoteConfigurationHelper getInstance()
	{
		if(instance ==null){
			synchronized (RemoteConfigurationHelper.class) {
				if(instance == null)
				{
					instance = new RemoteConfigurationHelper();
				}
			}
		}
		return instance;
	}
	/**
	 * Prepare physical resource to launch the diet agent
	 * - Search the physical resource to run the diet agent
	 * - Create remote directory
	 * - Create configuration file on local directory
	 * - Copy configuration files on remote physical resource
	 * @param resource.
	 * @throws PrepareException if create local files or can't copy files on remote host. 
	 */
	public void configure(DietResource resource) throws PrepareException
	{
		
	}
	
	/**
	 * Launch diet agent on the physical resource
	 * - Search the physical resource to run the diet agent
	 * - Launch command
	 * @param resource
	 * @throws LaunchException if can't connect to the remote host or can't launch binary
	 */
	public void launch(DietResource resource) throws LaunchException{
		
	}
	
	/**
	 * Stop diet agent on the physical resource
	 * - Search the physical resource to stop the diet agent
	 * - Launch kill command
	 * @param resource
	 * @throws LaunchException if can't connect to the remote host or can't launch binary
	 */
	public void stop(DietResource resource) throws LaunchException{
		
	}
	
}
