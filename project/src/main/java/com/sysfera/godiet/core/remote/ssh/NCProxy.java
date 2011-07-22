package com.sysfera.godiet.core.remote.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SocketFactory;
import com.jcraft.jsch.UserInfo;

/**
 * JSCH Proxy that use netcat
 * 
 * @author phi
 * 
 */
public class NCProxy implements Proxy {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ChannelExec channel;
	private Session session1;
	private JSch jsch;

	private final String username;
	private final String host;
	private final int port;
	private UserInfo ui;
	private Proxy proxy = null;

	public NCProxy(String username, String host, int port, JSch jsch,
			UserInfo ui) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.jsch = jsch;
		this.ui = ui;
	}

	public String getHost() {
		return host;
	}

	@Override
	public void connect(SocketFactory socket_factory, String dhost, int dport,
			int timeout) throws Exception {
		try {
			session1 = jsch.getSession(username, host, port);
			session1.setUserInfo(ui);
			if (proxy != null) {
				session1.setProxy(proxy);
			}
			session1.connect(timeout);
			channel = (ChannelExec) session1.openChannel("exec");
			channel.setAgentForwarding(true);
			channel.setCommand("nc " + dhost + " " + dport); // or netcat, bash,
																// ...
			channel.connect(timeout);
		} catch (JSchException e) {
			log.error("Unable to connect to " + username + "@" + host + ":"
					+ port);
			throw e;
		}

	}

	@Override
	public InputStream getInputStream() {
		try {
			return channel.getInputStream();
		} catch (IOException e) {
			log.error("Fatal: channel inputstream error",e);
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return channel.getOutputStream();
		} catch (IOException e) {
			log.error("Fatal: channel outpustream error",e);
			return null;
		}
	}

	@Override
	public Socket getSocket() {
		return null;
	}

	@Override
	public void close() {
		channel.disconnect();
		session1.disconnect();

	}

	/**
	 * Add a proxy to this proxy
	 * 
	 * @param proxy
	 *            the proxy to add
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

}
