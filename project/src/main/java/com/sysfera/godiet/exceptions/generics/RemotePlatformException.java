package com.sysfera.godiet.exceptions.generics;

/**
 * Throw when Remote access error
 * @author phi
 *
 */
public class RemotePlatformException extends Exception {

	public RemotePlatformException() {
		super();
	}

	public RemotePlatformException(String message) {
		super(message);
	}

	public RemotePlatformException(String message, Exception e) {
		super(message, e);
	}
}
