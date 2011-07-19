/**
 * 
 */
package com.sysfera.godiet.exceptions;

/**
 * @author phi
 * 
 */
public class CommandExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public CommandExecutionException() {
		super();
	}

	public CommandExecutionException(String message) {
		super(message);
	}

	public CommandExecutionException(String message, Exception e) {
		super(message, e);
	}
}
