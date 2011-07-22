package com.sysfera.godiet.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.services.ConfigurationService;
import com.sysfera.godiet.core.managers.ConfigurationManager;

@Component
public class ConfigurationServiceImpl implements ConfigurationService{

	@Autowired
	private ConfigurationManager configurationManager;
	
	
	@Override
	public String getServerNodeLabel() {
		return configurationManager.getLocalNodeId();
	}

	
}
