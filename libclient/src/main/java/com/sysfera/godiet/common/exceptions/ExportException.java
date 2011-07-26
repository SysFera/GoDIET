package com.sysfera.godiet.common.exceptions;

/**
 * 
 * Throw when search path error
 * 
 * @author phi
 * 
 */
public class ExportException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ExportException() {
		super();
	}
	
	public ExportException(String message)
	{
		super(message);
	}
	public ExportException(String message,Exception e)
	{
		super(message,e);
	}
}
