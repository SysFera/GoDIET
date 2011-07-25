package com.sysfera.godiet.common.model;

import java.util.Date;
import java.util.List;

import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState.State;

public interface SoftwareInterface<T extends Software>{

	public abstract String getId();
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
	public abstract State getState();


	public abstract List<ConfigurationFile> getFiles();

	public abstract Date getLastTransition();

	public abstract String getErrorMessage();

	public abstract String getErrorMessageDetails();

}