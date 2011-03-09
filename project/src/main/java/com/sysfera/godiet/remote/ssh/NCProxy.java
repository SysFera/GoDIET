package com.sysfera.godiet.remote.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
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
	private ChannelExec channel;
	private Session session1;
	private String username;
	private JSch jsch;
	private String host;
	private int port;
	private UserInfo ui;
	private Proxy proxy = null;

	public NCProxy(String username, String host, int port, JSch jsch,UserInfo ui) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.jsch = jsch;
		this.ui = ui;
	}

	@Override
	public void connect(SocketFactory socket_factory, String dhost, int dport,
			int timeout) throws Exception {
		session1 = jsch.getSession(username, host, port);
		session1.setUserInfo(ui);
		if(proxy != null)session1.setProxy(proxy);
		session1.connect(timeout);
		channel = (ChannelExec) session1.openChannel("exec");
		channel.setCommand("nc " + dhost + " " + dport); // or netcat, bash, ...
		channel.connect(timeout);
		
	}

	@Override
	public InputStream getInputStream() {
		try {
			return channel.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return channel.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
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
	 * @param proxy the proxy to add
	 */
	public void setProxy(Proxy proxy){
		this.proxy = proxy;
	}

}
