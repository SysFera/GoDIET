package com.sysfera.godiet.model;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.StateController;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * 
 * Use to manage software lifecycle.
 * 
 * @author phi
 * 
 */
public abstract class SoftwareManager<T extends Software> {
	private String runningCommand;

	// Software description
	private T softwareDescription;
	private final Resource pluggedOn;
	
	private Integer pid;
	protected final StateController stateController;

	public SoftwareManager(Resource pluggedOn, SoftwareController softwareController,RuntimeValidator<? extends SoftwareManager<T>> validator) {
		this.pluggedOn = pluggedOn;
		stateController = new StateController(this, softwareController,validator);

	}

	/**
	 * Return the physical resource on which agent is plugged on
	 * 
	 * @return the pluggedOn or null otherwise
	 */
	public Resource getPluggedOn() {
		if (softwareDescription != null) {
			return pluggedOn;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return stateController
	 */
	public StateController getStateController() {
		return stateController;
	}


	
	public T getSoftwareDescription() {
		return softwareDescription;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getPid() {
		return pid;
	}

	/**
	 * Return the command to run the software on the remote physical resource
	 * 
	 * @return the command to run the managed software or null if it doesn't
	 *         plugged.s
	 */
	public String getRunningCommand() {
		return runningCommand;
	}

	/**
	 * TODO: Perhaps code refactor: setRunning may not be null.
	 * 
	 * @param runningCommand
	 *            the command
	 */
	public void setRunningCommand(String runningCommand) {
		this.runningCommand = runningCommand;
	}

	/**
	 * Use carrefully: could be modify in indenpendant thread
	 * @return
	 */
	public ResourceState getState() {
		return stateController.getState();
	}

	public synchronized void start() throws LaunchException {

		ResourceState currentState = this.stateController.getState();
		currentState.start();

	}

	public synchronized void prepare() throws PrepareException {
		ResourceState currentState = this.stateController.getState();
		currentState.prepare();

	}

	public synchronized void stop() throws StopException {
		ResourceState currentState = this.stateController.getState();
		currentState.stop();
	}
	/**
	 * Set the agent to manage.
	 * 
	 * @param description
	 */
	public void setSoftwareDescription(T description) {
		this.softwareDescription = description;
	}



}