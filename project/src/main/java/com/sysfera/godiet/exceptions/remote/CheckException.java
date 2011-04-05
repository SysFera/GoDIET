package com.sysfera.godiet.exceptions.remote;


/**
 * Throw when a remote execution could not be done
 * @author phi
 *
 */
public class CheckException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public CheckException() {
		super();
	}
	
	public CheckException(String message)
	{
		super(message);
	}
	public CheckException(String message,Exception e)
	{
		super(message,e);
	}
}
