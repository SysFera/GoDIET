package com.sysfera.godiet.exceptions;
/**
 * 
 * 
 * Throw when a call on inconsistent state called on Diet Resource
 * @author phi
 *
 */
public class InconsistentStateException extends Exception{

	public InconsistentStateException() {
		super();
	}
	
	public InconsistentStateException(String message)
	{
		super(message);
	}
	public InconsistentStateException(String message,Exception e)
	{
		super(message,e);
	}
}
