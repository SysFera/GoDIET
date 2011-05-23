package com.sysfera.godiet.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.validators.RuntimeValidator;

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

	public DietResourceManaged(SoftwareController softwareController,RuntimeValidator validator) {
		super(softwareController,validator);
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
		} else {
			return null;
		}
	}


	@Override
	public Software getSoftwareDescription() {
		return agentManaged;
	}
	

}
