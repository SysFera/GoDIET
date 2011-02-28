package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SocketFactory;

public	class NCProxy {//implements Proxy {
	// private ChannelExec channel;
	// private Session session1;
	// private String username;
	// private JSch jsch;
	// private String host;
	// private int port;
	// public NCProxy(String username, String host, int port, JSch jsch) {
	// this.host = host;
	// this.port = port;
	// this.username = username;
	// this.jsch = jsch;
	// }
	// @Override
	// public void connect(SocketFactory socket_factory, String dhost,
	// int dport, int timeout) throws Exception {
	// session1 = jsch.getSession(username, host, port);
	// session1.setUserInfo(new MyUserInfo());
	// session1.connect(timeout);
	// channel = (ChannelExec)session1.openChannel("exec");
	// channel.setCommand("nc "+dhost+" "+dport); //or netcat, bash, ...
	// channel.connect(timeout);
	// }
	// @Override
	// public InputStream getInputStream() {
	// try {
	// return channel.getInputStream();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	// @Override
	// public OutputStream getOutputStream() {
	// try {
	// return channel.getOutputStream();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	// @Override
	// public Socket getSocket() {
	// return null;
	// }
	// @Override
	// public void close() {
	// channel.disconnect();
	// session1.disconnect();
	// }
	// }

}
