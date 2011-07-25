package com.sysfera.godiet.core.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.model.generated.GoDietConfiguration.Proxy;
import com.sysfera.godiet.common.model.generated.Scratch;

/**
 * GoDiet configuration description. Temporary manage shadow SSH Proxy
 * 
 * @author phi
 * 
 */
@Component
public class ConfigurationManager {

	private List<Proxy> shadowedProxy;
	private String localNodeId;
	private Scratch localScratch;

	public ConfigurationManager() {
		shadowedProxy = new ArrayList<Proxy>();
	}

	public void setLocalNodeId(String localNodeId) {
		this.localNodeId = localNodeId;
	}

	public String getLocalNodeId() {
		return localNodeId;
	}

	public void addShadowedProxy(Proxy proxy) {
		shadowedProxy.add(proxy);
	}

	public List<Proxy> getShadowedProxy() {
		return shadowedProxy;
	}

	public Scratch getLocalScratch() {
		return localScratch;
	}

	public void setLocalScratch(Scratch localScratch)
			throws GoDietConfigurationException {
		// Test if we could write in
		new File(localScratch.getDir()).mkdirs();
		if (!(new File(localScratch.getDir())).isDirectory())
			throw new GoDietConfigurationException("Unable to write in "
					+ localScratch.getDir() + " directory");
		this.localScratch = localScratch;
	}
	

}
