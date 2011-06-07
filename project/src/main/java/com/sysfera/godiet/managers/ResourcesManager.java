package com.sysfera.godiet.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.generated.GoDietConfiguration;

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
	private DietManager dietModel;
	private final PlatformManager platformModel;
	// TODO move this field in configuration manager
	@Autowired
	private ConfigurationManager godietConfiguration;
	@Autowired
	private UserManager userManager;

	public ResourcesManager() {
		this.dietModel = new DietManager();
		this.platformModel = new PlatformManager();
	}

	public DietManager getDietModel() {
		return dietModel;
	}

	public void setDietModel(DietManager dietModel) {
		this.dietModel = dietModel;
	}

	public PlatformManager getPlatformModel() {
		return platformModel;
	}

	public ConfigurationManager getGodietConfiguration() {
		return godietConfiguration;
	}

	public void setGoDietConfiguration(GoDietConfiguration goDietConfiguration) {
		this.godietConfiguration.setConfiguration(goDietConfiguration);

	}

	public UserManager getUserManager() {
		return userManager;
	}

}
