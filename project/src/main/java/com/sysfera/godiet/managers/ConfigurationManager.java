package com.sysfera.godiet.managers;

import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.users.UserManager;

/**
 * GoDiet configuration description
 * @author phi
 *
 */
public class ConfigurationManager {

	private GoDietConfiguration goDietConfiguration;
	private UserManager userManager;
	
	public void setConfiguration(GoDietConfiguration goDietConfiguration) {
		this.goDietConfiguration = goDietConfiguration;		
	}

	public GoDietConfiguration getGoDietConfiguration() {
		return goDietConfiguration;
	}
	
	
}
