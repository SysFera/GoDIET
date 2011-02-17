package com.sysfera.godiet.Model.physicalresources;

import com.sysfera.godiet.Model.ComputeCollection;
import com.sysfera.godiet.Model.Domain;

/**
 * Represent an physical element which is an interface  between Domains
 * In charge of keeping Diet/Log Forwarder
 * @see Domain
 * 
 * 
 * @author phi
 *
 */
public class GatewayResource extends ComputeResource{

	public GatewayResource(String name, ComputeCollection collection,Domain domain) {
		super(name,  collection,domain);
		this.domain = domain;
	}

	private Domain domain;
	
	public Domain getDomain() {
		return domain;
	}
	
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
}
