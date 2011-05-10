package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.OmniNames;

/**
 * Diet platform manager.
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

	// Transient list to store all id registers
	private final Set<String> dietResourceId;

	public DietManager() {
		this.masterAgents = new ArrayList<DietResourceManaged>();
		this.localAgents = new ArrayList<DietResourceManaged>();
		this.seds = new ArrayList<DietResourceManaged>();
		this.omninames = new ArrayList<DietServiceManaged>();
		this.forwaders = new ArrayList<DietResourceManaged>();

		this.dietResourceId = new HashSet<String>();
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
	 * @throws DietResourceCreationException 
	 */
	public void addSed(DietResourceManaged sedDiet) throws DietResourceCreationException {
		String id = sedDiet.getSoftwareDescription().getId();
		if (dietResourceId.contains(id))
			throw new DietResourceCreationException(id + " already registred");
		dietResourceId.add(id);
		this.seds.add(sedDiet);

	}

	/**
	 * @param dietResource
	 * @throws DietResourceCreationException 
	 */
	public void addLocalAgent(DietResourceManaged localAgentManaged) throws DietResourceCreationException {
		String id = localAgentManaged.getSoftwareDescription().getId();
		if (dietResourceId.contains(id))
			throw new DietResourceCreationException(id + " already registred");
		dietResourceId.add(id);
		this.localAgents.add(localAgentManaged);
	}

	/**
	 * @param dietResource
	 * @throws DietResourceCreationException 
	 */
	public void addMasterAgent(DietResourceManaged masterAgentManaged) throws DietResourceCreationException {
		String id = masterAgentManaged.getSoftwareDescription().getId();
		if (dietResourceId.contains(id))
			throw new DietResourceCreationException(id + " already registred");
		dietResourceId.add(id);
		this.masterAgents.add(masterAgentManaged);

	}

	/**
	 * 
	 * @param forwarder
	 * @throws DietResourceCreationException
	 */
	public void addForwarders(DietResourceManaged forwarderClient,
			DietResourceManaged forwarderServer) throws DietResourceCreationException {
		String idC = forwarderClient.getSoftwareDescription().getId();
		String idS = forwarderServer.getSoftwareDescription().getId();

		if (dietResourceId.contains(idC) || dietResourceId.contains(idS))
			throw new DietResourceCreationException(idC + " or " + idS + " already registred");
		dietResourceId.add(idC);
		dietResourceId.add(idS);
		// DietResourceManaged[] managedForwarders =
		// forwFactory.create(forwarder);
		// if (managedForwarders.length != 2) {
		// throw new DietResourceCreationException("TODO: What the fuck");
		// }
		this.forwaders.add(forwarderClient);
		this.forwaders.add(forwarderServer);
	}

	/**
	 * @param omniNames
	 * @throws DietResourceCreationException
	 */
	public void addOmniName(DietServiceManaged omniName) throws DietResourceCreationException {
		String id = omniName.getSoftwareDescription().getId();
		if (dietResourceId.contains(id))
			throw new DietResourceCreationException(id + " already registred");
		dietResourceId.add(id);
		this.omninames.add(omniName);
	}

	/**
	 * Create a new list contains a reference on all Software Managed by Godiet
	 * Contains a reference on OmniNames, *Agents and SeDs.
	 * 
	 * @return A list of all softwares managed by godiet
	 */
	public List<SoftwareManager> getAllManagedSoftware() {
		List<SoftwareManager> softwaresManaged = new ArrayList<SoftwareManager>(
				forwaders.size() + localAgents.size() + masterAgents.size()
						+ seds.size() + omninames.size());
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

	/**
	 * Search the Software managed given is id
	 * 
	 * @param id
	 *            the Id of software
	 * @return the managed software or null if doesn't exist
	 */
	public SoftwareManager getManagedSoftware(String id) {
		List<SoftwareManager> softwaresManaged = getAllManagedSoftware();
		for (SoftwareManager softwareManager : softwaresManaged) {
			if (softwareManager.getSoftwareDescription().getId().equals(id)) {
				return softwareManager;
			}
		}
		return null;
	}

	/**
	 * Search the Managed domain given a domain description
	 * 
	 * @param domain
	 * @return
	 */
	public DietResourceManaged getManagedOmniName(Domain domain) {
		for (DietResourceManaged manageOmni : omninames) {
			if (manageOmni.getPluggedOn().getDomain().equals(domain)) {
				return manageOmni;
			}
		}
		return null;
	}
}
