package com.sysfera.godiet.managers;

import com.sysfera.godiet.Model.xml.generated.Infrastructure;

/**
 * NG Physical infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	private final Infrastructure physicalInfrastructure;

	public ResourcesManager(Infrastructure physicalInfrastructure) {
		this.physicalInfrastructure = physicalInfrastructure;

	}

	/**
	 * @return the physicalInfrastructure
	 */
	public Infrastructure getPhysicalInfrastructure() {
		return physicalInfrastructure;
	}
}
