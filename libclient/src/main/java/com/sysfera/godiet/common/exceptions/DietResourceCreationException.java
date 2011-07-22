package com.sysfera.godiet.common.exceptions;

/**
 * Throw when a managed resource creation couldn't be done.
 * @author phi
 *
 */
public class DietResourceCreationException extends Exception {
	private static final long serialVersionUID = 1L;

	public DietResourceCreationException() {
		super();
	}

	public DietResourceCreationException(String message) {
		super(message);
	}

	public DietResourceCreationException(String message, Exception e) {
		super(message, e);
	}
}
