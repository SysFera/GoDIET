package com.sysfera.godiet.exceptions.generics;

/**
 * 
 * Throw when search path error
 * 
 * @author phi
 * 
 */
public class GoDietConfigurationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public GoDietConfigurationException() {
		super();
	}
	
	public GoDietConfigurationException(String message)
	{
		super(message);
	}
	public GoDietConfigurationException(String message,Exception e)
	{
		super(message,e);
	}
}
