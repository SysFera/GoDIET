package com.sysfera.godiet.common.exceptions;

/**
 * Throw when a managed resource creation couldn't be done.
 * @author phi
 *
 */
public class ResourceAddException extends Exception {
	private static final long serialVersionUID = 1L;

	public ResourceAddException() {
		super();
	}

	public ResourceAddException(String message) {
		super(message);
	}

	public ResourceAddException(String message, Exception e) {
		super(message, e);
	}
}
