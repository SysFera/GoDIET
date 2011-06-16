package com.sysfera.godiet.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.OmniNames;

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
	private  PlatformManager platformModel;
	@Autowired
	private  ConfigurationManager godietConfiguration;
	// TODO move this field in configuration manager??
	@Autowired
	private UserManager userManager;



	public DietManager getDietModel() {
		return dietModel;
	}

	public PlatformManager getPlatformModel() {
		return platformModel;
	}

	public ConfigurationManager getGodietConfiguration() {
		return godietConfiguration;
	}

	public UserManager getUserManager() {
		return userManager;
	}
	
	/**
	 * 
	 * @param domain
	 * @return The managed omniName which are is in the managedSoftware's
	 *         domain. Null if it's not found
	 */
	public OmniNames getOmniName(SoftwareManager software) {

		
		for (DietServiceManaged omniName : dietModel.getOmninames()) {
			if (omniName.getPluggedOn().getDomain().getLabel()
					.equals(software.getPluggedOn())) {
				return (OmniNames) omniName.getSoftwareDescription();
			}

		}
		
		return null;
	}

}
