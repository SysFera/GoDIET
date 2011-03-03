package com.sysfera.godiet.model;

import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.states.StateController;

/**
 * 
 * Use to manage software lifecycle.
 * 
 * @author phi
 * 
 */
public abstract class SoftwareManager {

	protected StateController stateController;

	
	/**
	 * Return the physical resource on which agent is plugged on
	 * 
	 * @return the pluggedOn or null otherwise
	 */
	public abstract Node getPluggedOn();

	/**
	 * 
	 * @return stateController
	 */
	public StateController getStateController() {
		return stateController;
	}

}