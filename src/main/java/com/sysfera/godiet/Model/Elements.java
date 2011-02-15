/*@GODIET_LICENSE*/
/*
 * Elements.java
 *
 * Created on 13 avril 2004, 14:44
 */

package com.sysfera.godiet.Model;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author hdail
 */
public abstract class Elements extends java.util.Observable {
	/**
	 * Config-related items. These should never be changed if the element is
	 * currently running
	 */
	protected String name = null;
	protected ComputeResource compHost = null;
	protected String binaryName = null;
	protected ElementCfg elConfig = null;
	protected boolean useDietStats = com.sysfera.godiet.Defaults.USE_DIET_STATS;
	protected Domain domain = null;
	/** Run-related items */
	private LaunchInfo launchInfo = null;

	/* Constructor for Elements. */
	public Elements(String name, ComputeResource compRes, String binary,
			Domain domain) {
		this.name = name;
		this.compHost = compRes;
		this.binaryName = binary;
		String filename = "";
		if(domain != null) filename += domain.getName() + "/";
		filename+=  name + ".cfg";
		this.elConfig = new ElementCfg(filename);
		this.launchInfo = new LaunchInfo();
		compRes.addElement(this);
		compRes.getCollection().getStorageResource().addElement(this);
		this.domain = domain;
	}

	public abstract void writeCfgFile(FileWriter out) throws IOException;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setComputeResource(ComputeResource compRes) {
		this.compHost = compRes;
	}

	public ComputeResource getComputeResource() {
		return this.compHost;
	}

	public void setBinaryName(String binaryName) {
		this.binaryName = binaryName;
	}

	public String getBinaryName() {
		return this.binaryName;
	}

	public void setUseDietStats(boolean flag) {
		this.useDietStats = flag;
	}

	public boolean getUseDietStats() {
		return this.useDietStats;
	}

	public String getCfgFileName() {
		return this.elConfig.getCfgFileName();
	}

	public String setCfgFileName(String cfgFileName) {
		return this.elConfig.setCfgFileName(cfgFileName);
	}

	public void setLaunchInfo(LaunchInfo launchInfo) {
		this.launchInfo = launchInfo;
	}

	public LaunchInfo getLaunchInfo() {
		return this.launchInfo;
	}

	public ElementCfg getElementCfg() {
		return this.elConfig;
	}

	public Domain getDomain() {
		return domain;
	}
}
