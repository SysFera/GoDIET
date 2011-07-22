package com.sysfera.godiet.common.exceptions.generics;

/**
 * 
 * Throw when search path error
 * 
 * @author phi
 * 
 */
public class UnManagedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public UnManagedException() {
		super();
	}
	
	public UnManagedException(String message)
	{
		super(message);
	}
	public UnManagedException(String message,Exception e)
	{
		super(message,e);
	}
}
