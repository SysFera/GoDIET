package com.sysfera.godiet.core.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sysfera.godiet.common.exceptions.generics.StartException;
import com.sysfera.godiet.common.services.ConfigurationService;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.services.InfrastructureService;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.common.services.XMLLoaderService;
import com.sysfera.godiet.common.utils.StringUtils;
import com.sysfera.godiet.core.managers.ResourcesManager;
import com.sysfera.godiet.core.model.observer.ForwardersCreator;

/**
 * Godiet core main interface. Store controllers
 * 
 * @author phi
 * 
 */
public class GoDietServiceImpl implements GoDietService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private  ResourcesManager ressourceManager;
	@Autowired
	private UserServiceImpl userController;
	@Autowired
	private PlatformService platformController;
	@Autowired
	private XMLLoaderService xmlHelpController;
	@Autowired
	private InfrastructureServiceImpl infrastructureController;

	@Autowired
	private  ConfigurationServiceImpl configurationService;

	@Autowired
	private  ForwardersCreator forwarderAutoCreator;

	/**
	 * Service lifecycle. Start: Initialize controllers
	 * 
	 * @throws StartException
	 */
	@Override
	@PostConstruct
	public void start() throws StartException {
		if (ressourceManager == null || ressourceManager.getDietModel() == null
				|| ressourceManager.getInfrastructureModel() == null) {
			throw new StartException(getClass().getName(), "4",
					"ressource manager isn't initialize", null);
		}
		initConfig();

	}

	/**
	 * Initialize configuration. Try to load
	 * ${user.dir}/godiet/configuration.xml. If not found load
	 * resources/configuration/configuration.xml
	 * 
	 */
	private void initConfig() {

		InputStream inputStream = null;

		// TryLoad {user.dir}/godiet/configuration.xml
		String configFilePath = System.getProperty("user.home")
				+ "/.godiet/configuration.xml";
		try {
			log.debug("Init Config: try to open file url: file:"
					+ configFilePath);
			URL url = new URL("file:" + configFilePath);

			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			inputStream = getClass().getResourceAsStream(
					"/configuration/configuration.xml");
		} catch (Exception e) {
			log.warn("Unable to open file " + configFilePath, e);
			inputStream = getClass().getResourceAsStream(
					"/configuration/configuration.xml");
		}
		if (inputStream == null) {
			log.error("Fatal: Unable to load user config file and open default config file");

		} else {
			try {
				String outputString = StringUtils.streamToString(inputStream);
				xmlHelpController.registerConfigurationFile(outputString);
			} catch (Exception e) {
				log.error("Error when loading config", e);

			}
		}

	}

	@Override
	public PlatformService getPlatformService() {
		return platformController;
	}

	@Override
	public XMLLoaderService getXmlHelpService() {
		return xmlHelpController;
	}

	@Override
	public InfrastructureService getInfrastructureService() {
		return infrastructureController;
	}

	@Override
	public UserServiceImpl getUserService() {
		return userController;
	}

	// @Override
	public ResourcesManager getModel() {
		return this.ressourceManager;
	}

	public void startForwarderCreator(boolean start) {
		if (start)
			this.forwarderAutoCreator.start();
		else
			this.forwarderAutoCreator.stop();
	}

	@Override
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}
}
