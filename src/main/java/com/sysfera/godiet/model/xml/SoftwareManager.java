package com.sysfera.godiet.model.xml;

import com.sysfera.godiet.model.states.StateController;
import com.sysfera.godiet.model.xml.generated.Node;

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