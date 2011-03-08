package com.sysfera.godiet.exceptions.graph;

/**
 * 
 * Throw when search path error
 * 
 * @author phi
 * 
 */
public class PathException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PathException() {
		super();
	}
	
	public PathException(String message)
	{
		super(message);
	}
	public PathException(String message,Exception e)
	{
		super(message,e);
	}
}
