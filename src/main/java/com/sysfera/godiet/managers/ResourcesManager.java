package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Model.deprecated.Elements;
import com.sysfera.godiet.Model.deprecated.Forwarder;
import com.sysfera.godiet.Model.xml.generated.DietConfiguration;
import com.sysfera.godiet.Model.xml.generated.Link;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	// Root level of goDiet configuration
	private DietConfiguration goDiet;

	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field
	private Diet dietModel;
	private Platform platformModel;
	//TODO move this field in configuration manager
	private GoDietConfiguration godietConfiguration;
	/**
	 * Reset all model and load DietConfigurtion 
	 */
	public void load(DietConfiguration dietConfiguration) {
		this.goDiet = dietConfiguration;
		if (goDiet != null) {
			platformModel.init(dietConfiguration.getInfrastructure());
			dietModel.init(dietConfiguration.getDietHierarchy(),dietConfiguration.getDietServices());
			
		}
	}



}
