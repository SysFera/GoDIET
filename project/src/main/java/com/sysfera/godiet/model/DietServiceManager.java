/**
 * 
 */
package com.sysfera.godiet.model;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.model.generated.DietService;
import com.sysfera.godiet.model.generated.Node;

/**
 * Use to manage and control all DIET services (OmniNames ...)
 * 
 * @author phi
 * 
 */
public class DietServiceManager {//extends SoftwareManager {

	private DietService dietServiceManaged;

	/**
	 * Set the service to manage.
	 * 
	 * @param dietAgent
	 */
	public void setDietService(DietService dietService) {
		this.dietServiceManaged = dietService;
	}

	/**
	 * The agent that is managed
	 * 
	 * @return the agent description
	 */
	public DietService getDietAgent() {
		return dietServiceManaged;
	}

	
//	@Override
	public Node getPluggedOn() {
		if (dietServiceManaged != null)
			return dietServiceManaged.getConfig().getServer();
		return null;
	}

	public void start()  throws LaunchException {
		//stateController.getState().start();
	}

}
