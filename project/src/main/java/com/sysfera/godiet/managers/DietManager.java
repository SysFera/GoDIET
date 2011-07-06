package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.observer.PlatformObserver;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareManager;

/**
 * Diet platform manager.
 * 
 * @author phi
 * 
 */
@Component
public class DietManager implements PlatformObservable{
	private Logger log = LoggerFactory.getLogger(getClass());

	private final List<DietResourceManaged<MasterAgent>> masterAgents;
	private final List<DietResourceManaged<LocalAgent>> localAgents;
	private final List<DietResourceManaged<Sed>> seds;
	private final List<OmniNamesManaged> omninames;
	private final List<DietResourceManaged<Forwarder>> forwaders;
	@Autowired
	private  DomainsManager domainManager;

	// Transient list to store all register's resourceId
	private final Set<String> dietResourceId;

	private final Set<PlatformObserver> observers;
	public DietManager() {
		this.masterAgents = new ArrayList<DietResourceManaged<MasterAgent>>();
		this.localAgents = new ArrayList<DietResourceManaged<LocalAgent>>();
		this.seds = new ArrayList<DietResourceManaged<Sed>>();
		this.omninames = new ArrayList<OmniNamesManaged>();
		this.forwaders = new ArrayList<DietResourceManaged<Forwarder>>();

		this.dietResourceId = new HashSet<String>();
		this.observers = new HashSet<PlatformObserver>();
	}

	/**
	 * @return the masterAgents
	 */
	public List<DietResourceManaged<MasterAgent>> getMasterAgents() {
		return masterAgents;
	}

	/**
	 * @return the localAgents
	 */
	public List<DietResourceManaged<LocalAgent>> getLocalAgents() {
		return localAgents;
	}

	/**
	 * @return the seds
	 */
	public List<DietResourceManaged<Sed>> getSeds() {
		return seds;
	}

	/**
	 * 
	 * @return list of forwarders
	 */
	public List<DietResourceManaged<Forwarder>> getForwarders() {
		return forwaders;
	}

	/**
	 * @return the omninames
	 */
	public List<OmniNamesManaged> getOmninames() {
		return omninames;
	}

	/**
	 * 
	 * @param sedDiet
	 * @throws DietResourceCreationException 
	 */
	public void addSed(DietResourceManaged<Sed> sedDiet) throws DietResourceCreationException {
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
	public void addLocalAgent(DietResourceManaged<LocalAgent> localAgentManaged) throws DietResourceCreationException {
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
	public void addMasterAgent(DietResourceManaged<MasterAgent> masterAgentManaged) throws DietResourceCreationException {
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
	public void addForwarders(DietResourceManaged<Forwarder> forwarderClient,
			DietResourceManaged<Forwarder> forwarderServer) throws DietResourceCreationException {
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
		this.domainManager.addForwarders(forwarderClient, forwarderServer);
	}

	/**
	 * @param omniNames
	 * @throws DietResourceCreationException
	 */
	public void addOmniName(OmniNamesManaged omniName) throws DietResourceCreationException {
		String id = omniName.getSoftwareDescription().getId();
		if (dietResourceId.contains(id))
			throw new DietResourceCreationException(id + " already registred");
		this.domainManager.addOmniNames(omniName);
		dietResourceId.add(id);
		this.omninames.add(omniName);
	}

	/**
	 * Create a new list contains a reference on all Software Managed by Godiet
	 * Contains a reference on OmniNames, *Agents and SeDs.
	 * 
	 * @return A list of all softwares managed by godiet
	 */
	public List<SoftwareManager<? extends Software>> getAllManagedSoftware() {
		List<SoftwareManager<? extends Software>> softwaresManaged = new ArrayList<SoftwareManager<? extends Software>>(
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
	 * Return an OmniNames given given the Domain 
	 * @param domain
	 * @return The managed omniName which are is in the managedSoftware's
	 *         domain. Null if it's not found
	 */
	public OmniNames getOmniName(Domain domain) {
		return domain.getOmniNames();
	}

	/**
	 * Search the Software managed given is id
	 * 
	 * @param id
	 *            the Id of software
	 * @return the managed software or null if doesn't exist
	 */
	public SoftwareManager<? extends Software> getManagedSoftware(String id) {
		List<SoftwareManager<? extends Software>> softwaresManaged = getAllManagedSoftware();
		for (SoftwareManager<? extends Software> softwareManager : softwaresManaged) {
			if (softwareManager.getSoftwareDescription().getId().equals(id)) {
				return softwareManager;
			}
		}
		return null;
	}

	/**
	 * Search the Managed domain given a domain description
	 * TODO: OmniNames builder ? In case of no omniNames for nodes
	 * @param domain
	 * @return null if doesn't exist
	 */
	public OmniNamesManaged getManagedOmniName(Node node) {
		if(node == null)
		{
			log.error("Node Null");
			return null;
		}
		List<Ssh> sshs = node.getSsh();
		for (Ssh ssh : sshs) {
			 //TODO: A map<omniDesc,OmniManage) 
			OmniNames omni = ssh.getDomain().getOmniNames();
			for (OmniNamesManaged manageOmni : omninames) {
				if (manageOmni.getSoftwareDescription().getId().equals(omni.getId())) {
					return manageOmni;
				}
			}
		}
		log.error("Unable to found omniNames");
		return null;
	}

	@Override
	public void register(PlatformObserver observer) {
		if(observers.contains(observer))
		{
			log.warn("Try to register an already registred observer"); 
		}
		observers.add(observer);
	}

	@Override
	public void unregister(PlatformObserver observer) {
		observers.remove(observer);
		
	}
}
