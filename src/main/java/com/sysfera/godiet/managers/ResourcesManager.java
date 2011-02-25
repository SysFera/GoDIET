package com.sysfera.godiet.managers;

import com.sysfera.godiet.Model.xml.generated.DietDescription;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	// Root level of goDiet configuration
	private DietDescription goDiet;

	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field
	private final Diet dietModel;
	private final Platform platformModel;
	// TODO move this field in configuration manager
	private GoDietConfiguration godietConfiguration;

	public ResourcesManager() {
		this.dietModel = new Diet();
		this.platformModel =  new Platform();
	}
	
	
	/**
	 * Reset all model and load DietConfigurtion
	 */
	public void load(DietDescription dietConfiguration) {
		this.goDiet = dietConfiguration;
		if (goDiet != null) {
			platformModel.init(dietConfiguration.getInfrastructure());
			dietModel.init(dietConfiguration.getDietInfrastructure(),
					dietConfiguration.getDietServices());

		}
	}

	public Diet getDietModel() {
		return dietModel;
	}

	public Platform getPlatformModel() {
		return platformModel;
	}
}
