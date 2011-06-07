package com.sysfera.godiet.managers;

import org.springframework.stereotype.Component;

import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.users.UserManager;

/**
 * GoDiet configuration description
 * @author phi
 *
 */
@Component
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
