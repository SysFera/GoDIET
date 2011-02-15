package com.sysfera.godiet.Model;

/**
 * Represent an physical element which is an interface  between Domains
 * In charge of keeping Diet/Log Forwarder
 * @see Domain
 * 
 * 
 * @author phi
 *
 */
public class Gateway {

	/*
	 * The id of the gateway
	 */
	private String id;
	
	/*
	 * The IP or DNS address
	 */
	private String address;
	
	/*
	 * The SSH TCP port
	 */
	private Integer port;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	
}
