package com.sysfera.godiet.exceptions.remote;

/**
 * Throw when unable to add a authentification key
 * @author phi
 *
 */
public class RemoveAuthentificationException extends Exception {
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public RemoveAuthentificationException() {
		super();
	}
	
	public RemoveAuthentificationException(String message)
	{
		super(message);
	}
	public RemoveAuthentificationException(String message,Exception e)
	{
		super(message,e);
	}
}
