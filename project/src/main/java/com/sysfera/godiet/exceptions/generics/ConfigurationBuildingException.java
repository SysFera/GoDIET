package com.sysfera.godiet.exceptions.generics;

public class ConfigurationBuildingException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurationBuildingException(String string) {
		super(string);
	}
	
	public ConfigurationBuildingException(String string, Throwable e) {
		super(string,e);
	}
}
