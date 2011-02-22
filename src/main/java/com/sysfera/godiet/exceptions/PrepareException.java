package com.sysfera.godiet.exceptions;


/**
 * Throw when a preparation execution could not be done
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
