package com.sysfera.godiet.managers;

import com.sysfera.godiet.Model.xml.generated.GoDietConfiguration;

/**
 * GoDiet configuration description
 * @author phi
 *
 */
public class GoDiet {

	private GoDietConfiguration goDietConfiguration;

	public void setConfiguration(GoDietConfiguration goDietConfiguration) {
		this.goDietConfiguration = goDietConfiguration;		
	}

	public GoDietConfiguration getGoDietConfiguration() {
		return goDietConfiguration;
	}
	
}
