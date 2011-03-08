package com.sysfera.godiet.exceptions.graph;

/**
 * Throw when try to incorrect graph modification
 * @author phi
 *
 */
public class GraphDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public GraphDataException() {
		super();
	}

	public GraphDataException(String message) {
		super(message);
	}

	public GraphDataException(String message, Exception e) {
		super(message, e);
	}
}
