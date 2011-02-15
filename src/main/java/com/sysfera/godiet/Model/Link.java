package com.sysfera.godiet.Model;


/**
 * Directional Link between two Gateways
 * @author phi
 *
 */
public class Link {

	/**
	 * Source link
	 */
	private Gateway from;
	
	/**
	 * Destination link
	 */
	private Gateway to;

	/**
	 * @return the from
	 */
	public Gateway getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Gateway from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Gateway getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Gateway to) {
		this.to = to;
	}
	
	
}
