package com.sysfera.godiet.exceptions.generics;

/**
 * Throw when Remote access error
 * @author phi
 *
 */
public class RemoteAccessException extends Exception {

	public RemoteAccessException() {
		super();
	}

	public RemoteAccessException(String message) {
		super(message);
	}

	public RemoteAccessException(String message, Exception e) {
		super(message, e);
	}
}
