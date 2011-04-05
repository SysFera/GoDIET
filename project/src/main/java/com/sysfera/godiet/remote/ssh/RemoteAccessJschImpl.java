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
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.remote.RemoteAccess;

/**
 * JSCH remote access implementation
 * 
 * @author phi
 * 
 */
public class RemoteAccessJschImpl implements RemoteAccess {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ChannelManagerJsch channelManager;

	// TODO: Extract JSCH default options.
	public RemoteAccessJschImpl() {
		Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch.setConfig(config);

	}

	public JSch getJsch() {
		return channelManager.getJsch();
	}

	public void setChannelManager(ChannelManagerJsch channelManager) {
		this.channelManager = channelManager;
	}

	/**
	 * Execute the command on the destination path and parse the output to
	 * return an integer
	 * 
	 * @param command
	 * @param path
	 * @return
	 * @throws RemoteAccessException
	 */
	private Integer execute(String command, Path path)
			throws RemoteAccessException {
		log.debug(command);
		ChannelExec channel = null;
		StringBuilder sb = new StringBuilder();
		try {
			channel = channelManager.getExecChannel(path, false);
			// TODO: Decor with tee to write on standard err and out stream and
			// alse remote scratch file

			channel.setCommand(command);

			channel.setInputStream(null);
			channel.setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			// Read Pid
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					sb.append(new String(tmp, 0, i));
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
			} catch (Exception e) {
				log.error("SSH disconnect error", e);
			}
		}
		// parse pid
		Integer pid = null;
		try {
			pid = Integer.valueOf(sb.toString().trim());
		} catch (NumberFormatException e) {
			log.error("Unable to convert the command output to PID ! Output =  "
					+ sb.toString());

		}
		return pid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#execute(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public Integer launch(String command, Path path)
			throws RemoteAccessException {
		String getPidCommand = "( /bin/echo \"" + command
				+ "\";  echo 'echo ${!}' )|sh -";
		return execute(getPidCommand, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.remote.RemoteAccess#check(java.lang.String,
	 * com.sysfera.godiet.model.Path)
	 */
	@Override
	public void check(String command, Path path) throws RemoteAccessException {
		String checkCommand = "( /bin/echo \"" + command
				+ " \";  echo 'echo ${?}' )|sh -";
		Integer result = execute(checkCommand, path);
		if (result.intValue() == 0 || result.intValue() == 1) {
			if (result.intValue() == 1)
				throw new RemoteAccessException("Process doesn't running");
		} else
			throw new RemoteAccessException("Unable to check if " + "sds"
					+ " process running");
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#copy(java.io.File,
	 * java.lang.String, int)
	 */
	@Override
	public void copy(File localFile, String remotePath, Path path)
			throws RemoteAccessException {
		FileInputStream fis = null;

		Channel channel = null;
		try {
			channel = channelManager.getExecChannel(path, true);
			// exec 'scp -t rfile' remotely
			String command = "scp -p -t " + remotePath + "/";
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				throw new RemoteAccessException("Unable to scp");
				// on : " + user+ "@" + host + ":" + port);
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
				throw new RemoteAccessException("Unable to scp ");
				// + user + "@" + host + ":" + port + "Command: " + command);
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
				throw new RemoteAccessException("Error when close connection ");
				// + user +"@" + host + ":" + port + "Command: " + command);
			}
			out.close();
			channel.disconnect();
		} catch (Exception e) {
			throw new RemoteAccessException("Unable to scp");// on :
																// " + user + "@"
			// + host + ":" + port, e);
		} finally {
			try {
				if (channel != null)
					channel.disconnect();
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
	public void addItentity(String privateKey, String publicKey,
			String passphrase) throws AddKeyException {

		channelManager.addIdentity(privateKey, publicKey, passphrase);
	}

	// TODO: to improve

	public void debug(boolean activate) {
		channelManager.debug(activate);
	}

}
