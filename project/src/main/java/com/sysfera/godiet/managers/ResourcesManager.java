package com.sysfera.godiet.managers;

import com.sysfera.godiet.model.generated.GoDietConfiguration;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field
	private final DietManager dietModel;
	private  PlatformManager platformModel;
	// TODO move this field in configuration manager
	private final ConfigurationManager godietConfiguration;

	public ResourcesManager() {
		this.dietModel = new DietManager();
		this.platformModel = new PlatformManager();
		this.godietConfiguration = new ConfigurationManager();
	}

	public DietManager getDietModel() {
		return dietModel;
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
	
	public void setPlatformModel(PlatformManager platformModel) {
		this.platformModel = platformModel;
	}
	

	
}
