package com.sysfera.godiet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.services.ConfigurationService;

@Component
public class ConfigurationServiceImpl implements ConfigurationService{

	@Autowired
	private ConfigurationManager configurationManager;
	
	
	@Override
	public String getServerNodeLabel() {
		return configurationManager.getLocalNodeId();
	}

	
}
