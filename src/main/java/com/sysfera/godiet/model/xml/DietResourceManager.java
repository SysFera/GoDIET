package com.sysfera.godiet.model.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;
import com.sysfera.godiet.model.states.DownStateImpl;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.StateController;
import com.sysfera.godiet.model.states.UpStateImpl;
import com.sysfera.godiet.model.xml.generated.Agent;
import com.sysfera.godiet.model.xml.generated.Node;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */
public class DietResourceManager extends SoftwareManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	// Agent description
	private Agent agentManaged;

	public DietResourceManager() {
		stateController = new StateController(this);
	}

	/**
	 * Set the agent to manage.
	 * 
	 * @param dietAgent
	 */
	public void setDietAgent(Agent dietAgent) {
		this.agentManaged = dietAgent;
	}

	/**
	 * The agent that is managed
	 * 
	 * @return the agent description
	 */
	public Agent getDietAgent() {
		return agentManaged;
	}

	@Override
	public Node getPluggedOn() {
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
}
