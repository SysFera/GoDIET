package com.sysfera.godiet.exceptions.remote;


/**
 * Throw when a remote execution could not be done
 * @author phi
 *
 */
public class StopException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public StopException() {
		super();
	}
	
	public StopException(String message)
	{
		super(message);
	}
	public StopException(String message,Exception e)
	{
		super(message,e);
	}
}
