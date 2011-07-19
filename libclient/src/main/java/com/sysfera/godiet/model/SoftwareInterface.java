package com.sysfera.godiet.model;

import java.util.List;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;

public interface SoftwareInterface<T extends Software> {

	/**
	 * Return the physical resource on which agent is plugged on
	 * 
	 * @return the pluggedOn resource or null if not yet plugged
	 */
	public abstract Resource getPluggedOn();

	public abstract T getSoftwareDescription();

	/**
	 * Return the command to run the software on the remote physical resource
	 * 
	 * @return the command to run the managed software or null if it doesn't
	 *         plugged.s
	 */
	public abstract String getRunningCommand();

	/**
	 * Use carrefully: could be modify in indenpendant thread
	 * 
	 * @return
	 */
	public abstract ResourceState getState();

	public abstract void start() throws LaunchException;

	public abstract void prepare() throws PrepareException;

	public abstract void stop() throws StopException;

	public abstract List<ConfigurationFile> getFiles();

}