package com.sysfera.godiet.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */
public class DietResourceManaged extends SoftwareManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	// Agent description
	private Software agentManaged;

	public DietResourceManaged() {

	}

	/**
	 * Set the agent to manage.
	 * 
	 * @param dietAgent
	 */
	public void setManagedSoftware(Software dietAgent) {
		this.agentManaged = dietAgent;
	}


	@Override
	public Resource getPluggedOn() {
		if (agentManaged != null) {
			return agentManaged.getConfig().getServer();
		} else
			return null;

	}

	public void start() throws LaunchException {

		ResourceState currentState = this.stateController.getState();
		currentState.start();

	}

	public void prepare() throws PrepareException {
		ResourceState currentState = this.stateController.getState();
		currentState.prepare();

	}

	public void stop() throws StopException {
		ResourceState currentState = this.stateController.getState();
		currentState.stop();
	}

	@Override
	public Software getSoftwareDescription() {
		return agentManaged;
	}
}
