package com.sysfera.godiet.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.user.UserManager;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
@Component
public class ResourcesManager {

	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field

	@Autowired
	private  DietManager dietModel;
	@Autowired
	private  InfrastructureManager infrastructureModel;
	@Autowired
	private  ConfigurationManager godietConfiguration;
	// TODO move this field in configuration manager??
	@Autowired
	private UserManager userManager;



	public DietManager getDietModel() {
		return dietModel;
	}

	public InfrastructureManager getInfrastructureModel() {
		return infrastructureModel;
	}

	public ConfigurationManager getGodietConfiguration() {
		return godietConfiguration;
	}

	public UserManager getUserManager() {
		return userManager;
	}
	

}
