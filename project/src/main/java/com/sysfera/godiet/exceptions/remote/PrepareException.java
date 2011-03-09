package com.sysfera.godiet.exceptions.remote;


/**
 * Throw when a preparation could not be done
 * @author phi
 *
 */
public class PrepareException extends Exception{

	public PrepareException() {
		super();
	}
	
	public PrepareException(String message)
	{
		super(message);
	}
	public PrepareException(String message,Exception e)
	{
		super(message,e);
	}
}
