package com.sysfera.godiet.services;

import com.sysfera.godiet.exceptions.generics.StartException;

public interface GoDietService {

	/**
	 * Service lifecycle. Start: Initialize controllers
	 * 
	 * @throws StartException
	 */
	
	public abstract void start() throws StartException;

	public abstract PlatformService getPlatformService();

	public abstract XMLLoaderService getXmlHelpService();

	public abstract InfrastructureService getInfrastructureService();

	public abstract UserService getUserService();

	public abstract ConfigurationService getConfigurationService();
	
//	public abstract ResourcesManager getModel();


}