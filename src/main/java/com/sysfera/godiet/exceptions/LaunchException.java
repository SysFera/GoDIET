package com.sysfera.godiet.exceptions;

public class LaunchException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public LaunchException() {
		super();
	}
	
	public LaunchException(String message)
	{
		super(message);
	}
	public LaunchException(String message,Exception e)
	{
		super(message,e);
	}
}
