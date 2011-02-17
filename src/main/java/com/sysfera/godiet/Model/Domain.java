package com.sysfera.godiet.Model;

import java.util.HashSet;
import java.util.Set;

import com.sysfera.godiet.Model.physicalresources.GatewayResource;

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
	private Set<GatewayResource> gateways;

	
	public Domain() {
		gateways = new HashSet<GatewayResource>();
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
	public Set<GatewayResource> getGateways() {
		return gateways;
	}
	public void addGateway(GatewayResource gateway) {
		this.gateways.add(gateway);
	}

}
