package com.sysfera.godiet.exceptions.remote;

/**
 * Throw when unable to add a authentification key
 * @author phi
 *
 */
public class AddAuthentificationException extends Exception {
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AddAuthentificationException() {
		super();
	}
	
	public AddAuthentificationException(String message)
	{
		super(message);
	}
	public AddAuthentificationException(String message,Exception e)
	{
		super(message,e);
	}
}
