package com.sysfera.godiet.remote;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class SessionMock {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final String username;
	private final String host;
	private final int port;
	private ProxyMock proxy;

	public SessionMock(String username, String host, Integer port) {
		this.username = username;
		this.host = host;
		this.port = port;
	}

	public void setProxy(ProxyMock proxy) {
		this.proxy = proxy;
	}

	public void connect() {
		if (proxy != null) {
			proxy.connect(username, host, port);
		}
		log.debug("Session opened to " + username + "@" + host + ":" + port);
	}

}
