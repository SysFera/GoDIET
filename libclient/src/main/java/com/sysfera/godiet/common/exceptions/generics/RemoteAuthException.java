package com.sysfera.godiet.common.exceptions.generics;

/**
 * Throw when Remote access error
 * @author phi
 *
 */
public class RemoteAuthException extends Exception {

	public RemoteAuthException() {
		super();
	}

	public RemoteAuthException(String message) {
		super(message);
	}

	public RemoteAuthException(String message, Exception e) {
		super(message, e);
	}
}
