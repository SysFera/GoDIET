package com.sysfera.godiet.exceptions.remote;


/**
 * Throw when a preparation could not be done
 * @author phi
 *
 */
public class IncubateException extends Exception{

	public IncubateException() {
		super();
	}
	
	public IncubateException(String message)
	{
		super(message);
	}
	public IncubateException(String message,Exception e)
	{
		super(message,e);
	}
}
