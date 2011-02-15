package com.sysfera.godiet.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * A Domain represent a subnet. In Diet design, a domain is composed by one OmniName and
 * several Gateways 
 * 
 * @author phi
 *
 */
public class Domain {

	private OmniNames omniNames;
	private String id;
	private Set<Gateway> gateways;

	
	public Domain() {
		gateways = new HashSet<Gateway>();
	}
	public OmniNames getOmniNames() {
		return omniNames;
	}

	public void setOmniNames(OmniNames omniNames) {
		this.omniNames = omniNames;
	}

	/**
	 * 
	 * @param The unique Id of the domain
	 * 
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	/**
	 * 
	 * @return the Name (cuurently = Id ) of the Domain
	 * 
	 */
	public String getName() {
		return id;

	}
	/**
	 * @return the domain Id
	 */
	public String getId() {
		return id;
	}



	/**
	 * @return the gateways
	 */
	public Set<Gateway> getGateways() {
		return gateways;
	}
	public void addGateway(Gateway gateway) {
		this.gateways.add(gateway);
	}

}
