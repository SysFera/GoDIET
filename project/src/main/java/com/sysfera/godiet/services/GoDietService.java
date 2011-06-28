package com.sysfera.godiet.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.sysfera.godiet.exceptions.generics.StartException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.observer.ForwardersCreator;

/**
 * Godiet core main interface. Store controllers
 * 
 * @author phi
 * 
 */
public class GoDietService {

	@Autowired
	private ResourcesManager ressourceManager;
	@Autowired
	private ConfigurationManager configurationManager;

	@Autowired
	private UserControllerImpl userController;
	@Autowired
	private PlatformControllerImpl platformController;
	@Autowired
	private XMLHelpControllerImpl xmlHelpController;
	@Autowired
	private InfrastructureControllerImpl infrastructureController;

	@Autowired
	private ForwardersCreator forwarderAutoCreator;

	public GoDietService() {
		
	}

	/**
	 * Service lifecycle. Start: Initialize controllers
	 * 
	 * @throws StartException
	 */
	@PostConstruct
	public void start() throws StartException {
		if (ressourceManager == null || ressourceManager.getDietModel() == null
				|| ressourceManager.getInfrastructureModel() == null) {
			throw new StartException(getClass().getName(), "4",
					"ressource manager isn't initialize", null);
		}

	}

	public PlatformController getPlatformController() {
		return platformController;
	}

	public XMLHelpControllerImpl getXmlHelpController() {
		return xmlHelpController;
	}

	public InfrastructureControllerImpl getInfrastructureController() {
		return infrastructureController;
	}

	public UserControllerImpl getUserController() {
		return userController;
	}

	public ResourcesManager getModel() {
		return this.ressourceManager;
	}

	public void startForwarderCreator(boolean start) {
		if (start)
			this.forwarderAutoCreator.start();
		else
			this.forwarderAutoCreator.stop();
	}
}
