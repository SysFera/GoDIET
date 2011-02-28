package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Model.xml.DietServiceManager;
import com.sysfera.godiet.Model.xml.generated.DietService;
import com.sysfera.godiet.Model.xml.generated.OmniNames;

/**
 * Diet platform description
 * 
 * @author phi
 * 
 */
public class Diet {

	private final List<DietResourceManager> masterAgents;
	private final List<DietResourceManager> localAgents;
	private final List<DietResourceManager> seds;
	private final List<DietServiceManager> omninames;
	private final List<DietResourceManager> forwaders;

	public Diet() {
		this.masterAgents = new ArrayList<DietResourceManager>();
		this.localAgents = new ArrayList<DietResourceManager>();
		this.seds = new ArrayList<DietResourceManager>();
		this.omninames = new ArrayList<DietServiceManager>();
		this.forwaders = new ArrayList<DietResourceManager>();
	}

	/**
	 * @return the masterAgents
	 */
	public List<DietResourceManager> getMasterAgents() {
		return masterAgents;
	}

	/**
	 * @return the localAgents
	 */
	public List<DietResourceManager> getLocalAgents() {
		return localAgents;
	}

	/**
	 * @return the seds
	 */
	public List<DietResourceManager> getSeds() {
		return seds;
	}

	/**
	 * 
	 * @return list of forwarders
	 */
	public List<DietResourceManager> getForwarders() {
		return forwaders;
	}

	/**
	 * @return the omninames
	 */
	public List<DietServiceManager> getOmninames() {
		return omninames;
	}

	/**
	 * TODO: check validity of parameter
	 * 
	 * @param sedDiet
	 */
	public void addSed(DietResourceManager sedDiet) {
		this.seds.add(sedDiet);
	}

	/**
	 * TODO: check validity of parameter
	 * 
	 * @param dietResource
	 */
	public void addLocalAgent(DietResourceManager dietResource) {
		this.localAgents.add(dietResource);
	}

	/**
	 * TODO: check validity of parameter
	 * 
	 * @param dietResource
	 */
	public void addMasterAgent(DietResourceManager dietResource) {
		this.masterAgents.add(dietResource);

	}

	/**
	 * TODO: check validity of parameter
	 * 
	 * @param forwarder
	 */
	public void addForwarder(DietResourceManager forwarder) {
		this.forwaders.add(forwarder);
	}

	/**
	 * TODO: check validity of parameter
	 * 
	 * @param omniNames
	 */
	public void addOmniName(DietServiceManager omniName) {
		this.omninames.add(omniName);
	}

}
