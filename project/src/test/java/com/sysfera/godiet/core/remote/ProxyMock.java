package com.sysfera.godiet.core.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author phi
 * 
 */


public class ProxyMock {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final String username;
	private final String host;
	private final int port;
	ProxyMock proxy = null;

	public ProxyMock(String username, String host, Integer port) {
		this.username = username;
		this.host = host;
		this.port = port;
	}

	public void setProxy(ProxyMock proxy) {
		this.proxy = proxy;
	}

	public void connect(String username, String destHost, int destPort) {
		if (proxy != null) {
			proxy.connect(username, host, port);
		}
		log.debug("Create a proxy command on " + username + "@" + host + ":"
				+ port + " to " + destHost + ":" + destPort);

	}
}
