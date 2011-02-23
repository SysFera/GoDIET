package com.sysfera.godiet.Model.deprecated;

import com.sysfera.godiet.Model.physicalresources.deprecated.GatewayResource;


/**
 * Directional Link between two Gateways
 * @author phi
 *
 */
public class Link {

	/**
	 * Source link
	 */
	private GatewayResource from;
	
	/**
	 * Destination link
	 */
	private GatewayResource to;

	/**
	 * @return the from
	 */
	public GatewayResource getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(GatewayResource from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public GatewayResource getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(GatewayResource to) {
		this.to = to;
	}
	
	
}
