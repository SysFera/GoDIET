package com.sysfera.godiet.common.exceptions.remote;

/**
 * Throw when unable to add a authentification key
 * @author phi
 *
 */
public class ModifyAuthentificationException extends Exception {
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ModifyAuthentificationException() {
		super();
	}
	
	public ModifyAuthentificationException(String message)
	{
		super(message);
	}
	public ModifyAuthentificationException(String message,Exception e)
	{
		super(message,e);
	}
}
