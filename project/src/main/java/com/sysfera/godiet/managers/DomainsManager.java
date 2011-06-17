package com.sysfera.godiet.managers;

import java.util.List;

import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.generated.Ssh;
/**
 * 
 * 
 * 
 * @author phi
 *
 */
public class DomainsManager {



	
	public void addOmniNames(OmniNamesManaged omniName) {
		List<Ssh> sshs = omniName.getPluggedOn().getSsh();
		for (Ssh ssh : sshs) {
			ssh.getDomain().setOmniNames(omniName.getSoftwareDescription());
		}
	}

	
	
}
