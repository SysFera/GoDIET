package com.sysfera.godiet.model;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.remote.RemoteAccess;

public interface SoftwareController {

	/**
	 * Prepare physical resource to launch the Software 
	 * @param resource
	 * 
	 * @throws PrepareException
	 *             if create local files or can't copy files on remote host.
	 */
	public abstract void configure(SoftwareManager resource)
			throws PrepareException;

	/**
	 * Launch software on the physical resource
	 * @param managedSofware
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	public abstract void launch(SoftwareManager managedSofware)
			throws LaunchException;

	/**
	 * Check if the software running
	 * @param managed
	 */
	public abstract void check(SoftwareManager resource) throws CheckException;

	/**
	 * Remote stop software
	 * @param resource
	 * @throws StopException
	 */
	public abstract void stop(SoftwareManager resource) throws StopException;

	
	

}