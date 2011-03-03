package com.sysfera.godiet.managers;

import com.sysfera.godiet.model.xml.generated.GoDietConfiguration;
import com.sysfera.godiet.users.UserManager;

/**
 * GoDiet configuration description
 * @author phi
 *
 */
public class GoDiet {

	private GoDietConfiguration goDietConfiguration;
	private UserManager userManager;
	
	public void setConfiguration(GoDietConfiguration goDietConfiguration) {
		this.goDietConfiguration = goDietConfiguration;		
	}

	public GoDietConfiguration getGoDietConfiguration() {
		return goDietConfiguration;
	}
	
	
}
