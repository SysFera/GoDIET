package com.sysfera.godiet.model;

import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.StateController;

/**
 * 
 * Use to manage software lifecycle.
 * 
 * @author phi
 * 
 */
public abstract class SoftwareManager {
	private String runningCommand;

	
	private Integer pid;
	protected final StateController  stateController;

	
	
	public SoftwareManager(SoftwareController softwareController) {
		
		stateController = new StateController(this,softwareController);

	}

	/**
	 * Return the physical resource on which agent is plugged on
	 * 
	 * @return the pluggedOn or null otherwise
	 */
	public abstract Resource getPluggedOn();

	/**
	 * 
	 * @return stateController
	 */
	public StateController getStateController() {
		return stateController;
	}

	public abstract Software getSoftwareDescription();

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getPid() {
		return pid;
	}

	/**
	 * Return the command to run the software on the remote physical resource
	 * 
	 * @return the command to run the managed software or null if it doesn't plugged.s
	 */
	public String getRunningCommand(){
		return runningCommand;
	}
	
	/**
	 * TODO: Perhaps code refactor: setRunning may not be null.
	 * @param runningCommand the command
	 */
	public void setRunningCommand(String runningCommand) {
		this.runningCommand = runningCommand;
	}

}