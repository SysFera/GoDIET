package com.sysfera.godiet.common.exceptions.generics;

/**
 * Throw when Remote access error
 * @author phi
 *
 */
public class RemoteConfigurationException extends Exception {

	public RemoteConfigurationException() {
		super();
	}

	public RemoteConfigurationException(String message) {
		super(message);
	}

	public RemoteConfigurationException(String message, Exception e) {
		super(message, e);
	}
}
