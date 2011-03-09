package com.sysfera.godiet.exceptions.remote;

/**
 * Throw when unable to add a authentification key
 * @author phi
 *
 */
public class AddKeyException extends Exception{
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AddKeyException() {
		super();
	}
	
	public AddKeyException(String message)
	{
		super(message);
	}
	public AddKeyException(String message,Exception e)
	{
		super(message,e);
	}
}
