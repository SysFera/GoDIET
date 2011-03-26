package com.sysfera.godiet.remote.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.remote.RemoteAccess;

/**
 * JSCH remote access implementation
 * 
 * @author phi
 *
 */
public class RemoteAccessJschImpl implements RemoteAccess {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final JSch jsch;
	private final UserInfo trustedUI;

	//TODO: Extract JSCH default options.
	public RemoteAccessJschImpl() {
		this.jsch = new JSch();
		Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch.setConfig(config);
		
		this.trustedUI = new TrustedUserInfo();

	}

	public JSch getJsch() {
		return jsch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#execute(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public void run(String command, Path path)
			throws RemoteAccessException {

		Channel channel = null;
		Session session = null;
		try {
			//TODO loop on path, create Proxy
			Node last = (Node) path.getPath().toArray()[path.getPath().size()-1];
			Ssh lastNodeContig = last.getSsh();
			session = jsch.getSession(lastNodeContig.getServer(), lastNodeContig.getLogin(), lastNodeContig.getPort());
			
			session.setUserInfo(trustedUI);
			session.connect();

			channel = session.openChannel("exec");
			// TODO: Decor with tee to write on standard err and out stream and
			// alse remote scratch file
			((ChannelExec) channel).setCommand(command);

			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					log.debug(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {

					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					log.error("Runtime error. Thread Interupted.", e);
					throw new RemoteAccessException("Thread interupted", e);
				}
			}
			if (channel.getExitStatus() < 0)
				throw new RemoteAccessException("SSH connection close abruptly");

		} catch (Exception e) {
			throw new RemoteAccessException("Unable to SSH connect. Command: "
					+ command, e);
		} finally {
			try {
				if (channel != null)
					channel.disconnect();
				if (session != null)
					session.disconnect();
			} catch (Exception e) {
				log.error("SSH disconnect error", e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#copy(java.io.File,
	 * java.lang.String, int)
	 */
	@Override
	public void copy(File localFile, Ssh sshConfig)
			throws RemoteAccessException {
		FileInputStream fis = null;
		Channel channel = null;
		Session session = null;
		String user = sshConfig.getLogin();
		String host = sshConfig.getServer();
		int port = sshConfig.getPort();
		try {

			session = jsch.getSession(user, host, 22);

			// username and password will be given via UserInfo interface.

			session.setUserInfo(trustedUI);
			session.connect();

			// exec 'scp -t rfile' remotely
			String command = "scp -p -t " + localFile.getAbsolutePath();
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				throw new RemoteAccessException("Unable to scp on : " + user
						+ "@" + host + ":" + port);
			}

			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = localFile.length();
			String localFileName = localFile.getName();
			command = "C0644 " + filesize + " ";
			if (localFileName.lastIndexOf('/') > 0) {
				command += localFileName.substring(localFileName
						.lastIndexOf('/') + 1);
			} else {
				command += localFileName;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new RemoteAccessException("Unable to scp on : " + user
						+ "@" + host + ":" + port + "Command: " + command);
			}

			// send a content of lfile
			fis = new FileInputStream(localFile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				throw new RemoteAccessException(
						"Error when close connection on : " + user + "@" + host
								+ ":" + port + "Command: " + command);
			}
			out.close();

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			throw new RemoteAccessException("Unable to scp on : " + user + "@"
					+ host + ":" + port, e);
		} finally {
			try {
				if (channel != null)
					channel.disconnect();
				if (session != null)
					session.disconnect();
				try {
					if (fis != null)
						fis.close();
				} catch (Exception ee) {
				}
			} catch (Exception e) {
				log.error("SSH disconnect error", e);
			}
		}

	}

	private int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				log.error("Error when copy file. Remote output: "
						+ sb.toString());
			}
			if (b == 2) { // fatal error
				log.error("Error when copy file. Remote output: "
						+ sb.toString());
			}
		}
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#addKey(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void addKey(String privateKey, String publicKey, String passphrase) throws AddKeyException{
		try {
			jsch.addIdentity(privateKey, publicKey, passphrase.getBytes());
		} catch (JSchException e) {
			
			throw new AddKeyException("Unable to add key",e);
		} finally {
			// Give to GC
			passphrase = null;
		}

	}

	// TODO: to improve

	public void jschDebug(boolean activate) {
		if (activate) {
			com.jcraft.jsch.Logger jschLog = new com.jcraft.jsch.Logger() {

				@Override
				public void log(int level, String message) {
					switch (level) {
					case 0:
						log.debug(message);
						break;
					case 1:
						log.info(message);
						break;
					case 2:
						log.warn(message);
						break;
					case 3:
						log.error(message);
						break;
					case 4:
						log.error("FATAL" + message);
						break;

					default:
						break;
					}

				}

				@Override
				public boolean isEnabled(int level) {
					return true;
				}
			};
			JSch.setLogger(jschLog);
		} else
			JSch.setLogger(null);

	}

}
