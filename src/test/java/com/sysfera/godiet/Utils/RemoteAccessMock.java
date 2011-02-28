/**
 * 
 */
package com.sysfera.godiet.Utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.RemoteAccessException;

/**
 * 
 * Mock of ssh. See integration test to test all features	
 * 
 * @author phi
 * 
 */
public class RemoteAccessMock implements RemoteAccess {
	private Logger log = LoggerFactory.getLogger(getClass());

	private boolean remoteAccessDown = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#run(java.lang.String,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	public void run(String command, String user, String host, int port)
			throws RemoteAccessException {
		if (remoteAccessDown)
			throw new RemoteAccessException("Unable to run " + command + " on "
					+ host + ":" + port + " . Login: " + user);
		log.debug("Run " + command + " on " + host + ":" + port + " . Login: "
				+ user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#copy(java.io.File,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	public void copy(File file, String user, String host, int port)
			throws RemoteAccessException {
		if (remoteAccessDown)
			throw new RemoteAccessException("Unable to copy file "
					+ file.getName() + " on " + host + ":" + port
					+ " . Login: " + user);

		log.debug("Copy " + file.getName() + " on " + host + ":" + port
				+ " . Login: " + user);

	}

	public void setRemoteAccessDown(boolean remoteAccessDown) {
		this.remoteAccessDown = remoteAccessDown;
	}
}
