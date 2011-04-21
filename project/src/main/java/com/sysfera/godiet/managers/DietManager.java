package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.OmniNames;

/**
 * Diet platform description.
 * 
 * @author phi
 * 
 */
public class DietManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final List<DietResourceManaged> masterAgents;
	private final List<DietResourceManaged> localAgents;
	private final List<DietResourceManaged> seds;
	private final List<DietServiceManaged> omninames;
	private final List<DietResourceManaged> forwaders;

	public DietManager() {
		this.masterAgents = new ArrayList<DietResourceManaged>();
		this.localAgents = new ArrayList<DietResourceManaged>();
		this.seds = new ArrayList<DietResourceManaged>();
		this.omninames = new ArrayList<DietServiceManaged>();
		this.forwaders = new ArrayList<DietResourceManaged>();

	}

	/**
	 * @return the masterAgents
	 */
	public List<DietResourceManaged> getMasterAgents() {
		return masterAgents;
	}

	/**
	 * @return the localAgents
	 */
	public List<DietResourceManaged> getLocalAgents() {
		return localAgents;
	}

	/**
	 * @return the seds
	 */
	public List<DietResourceManaged> getSeds() {
		return seds;
	}

	/**
	 * 
	 * @return list of forwarders
	 */
	public List<DietResourceManaged> getForwarders() {
		return forwaders;
	}

	/**
	 * @return the omninames
	 */
	public List<DietServiceManaged> getOmninames() {
		return omninames;
	}

	/**
	 * 
	 * @param sedDiet
	 */
	public void addSed(DietResourceManaged sedDiet) {
		this.seds.add(sedDiet);

	}

	/**
	 * @param dietResource
	 */
	public void addLocalAgent(DietResourceManaged localAgentManaged) {
		this.localAgents.add(localAgentManaged);
	}

	/**
	 * @param dietResource
	 */
	public void addMasterAgent(DietResourceManaged masterAgentManaged) {
		this.masterAgents.add(masterAgentManaged);

	}

	/**
	 * 
	 * @param forwarder
	 * @throws DietResourceCreationException
	 */
	public void addForwarders(DietResourceManaged forwarderClient,DietResourceManaged forwarderServer){
//		DietResourceManaged[] managedForwarders = forwFactory.create(forwarder);
//		if (managedForwarders.length != 2) {
//			throw new DietResourceCreationException("TODO: What the fuck");
//		}
		this.forwaders.add(forwarderClient);
		this.forwaders.add(forwarderServer);
	}

	/**
	 * @param omniNames
	 * @throws DietResourceCreationException
	 */
	public void addOmniName(DietServiceManaged omniName){
		this.omninames.add(omniName);
	}

	/**
	 * Create a new list contains a reference on all Software Managed by Godiet
	 * Contains a reference on OmniNames, *Agents and SeDs.
	 * 
	 * @return A list of all softwares managed by godiet
	 */
	public List<SoftwareManager> getAllManagedSoftware() {
		List<SoftwareManager> softwaresManaged = new ArrayList<SoftwareManager>();
		softwaresManaged.addAll(forwaders);
		softwaresManaged.addAll(localAgents);
		softwaresManaged.addAll(masterAgents);
		softwaresManaged.addAll(seds);
		softwaresManaged.addAll(omninames);

		return softwaresManaged;
	}

	/**
	 * 
	 * @param domain
	 * @return The managed omniName which are is in the managedSoftware's
	 *         domain. Null if it's not found
	 */
	public OmniNames getOmniName(Domain domain) {

		for (DietServiceManaged omniName : omninames) {
			if (omniName.getPluggedOn().getDomain().getLabel()
					.equals(domain.getLabel())) {
				return (OmniNames) omniName.getSoftwareDescription();
			}

		}
		log.error("Unable to find a known omniName for domain: "
				+ domain.getLabel());
		return null;
	}
}
