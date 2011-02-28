package com.sysfera.godiet.managers;

import com.sysfera.godiet.Model.xml.generated.GoDietConfiguration;


/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {


	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field
	private final Diet dietModel;
	private final Platform platformModel;
	// TODO move this field in configuration manager
	private final GoDiet godietConfiguration;

	public ResourcesManager() {
		this.dietModel = new Diet();
		this.platformModel =  new Platform();
		this.godietConfiguration = new GoDiet();
	}
	

	public Diet getDietModel() {
		return dietModel;
	}

	public Platform getPlatformModel() {
		return platformModel;
	}


	public void setGoDietConfiguration(GoDietConfiguration goDietConfiguration) {
		this.godietConfiguration.setConfiguration(goDietConfiguration);
		
	}
}
