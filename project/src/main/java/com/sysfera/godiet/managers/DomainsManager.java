package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Ssh;
/**
 * 
 * 
 * 
 * @author phi
 *
 */
public class DomainsManager {

	private final List<Domain> domains;
	
	public DomainsManager() {
		domains = new ArrayList<Domain>();
	}
	
	public void addAll(List<Domain> domains) {
		this.domains.addAll(domains);
	}

	
	public void addOmniNames(DietServiceManaged<OmniNames> omniName) {
		List<Ssh> sshs = omniName.getPluggedOn().getSsh();
		for (Ssh ssh : sshs) {
			ssh.getDomain().setOmniNames(omniName.getSoftwareDescription());
		}
	}

	
	
}
