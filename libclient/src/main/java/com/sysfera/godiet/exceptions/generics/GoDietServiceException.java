package com.sysfera.godiet.exceptions.generics;

/**
 * 
 * Throw when search path error
 * 
 * @author phi
 * 
 */
public class GoDietServiceException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public GoDietServiceException() {
		super();
	}
	
	public GoDietServiceException(String message)
	{
		super(message);
	}
	public GoDietServiceException(String message,Exception e)
	{
		super(message,e);
	}
}
