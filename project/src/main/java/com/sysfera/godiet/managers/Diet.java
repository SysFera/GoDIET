package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.factories.ForwardersFactory;
import com.sysfera.godiet.model.factories.LocalAgentFactory;
import com.sysfera.godiet.model.factories.MasterAgentFactory;
import com.sysfera.godiet.model.factories.OmniNamesFactory;
import com.sysfera.godiet.model.factories.SedFactory;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;

/**
 * Diet platform description.
 * 
 * @author phi
 * 
 */
public class Diet {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final MasterAgentFactory maFactory;
	private final LocalAgentFactory laFactory;
	private final SedFactory sedFactory;
	private final OmniNamesFactory omFactory;
	private final ForwardersFactory forwFactory;

	private final List<DietResourceManaged> masterAgents;
	private final List<DietResourceManaged> localAgents;
	private final List<DietResourceManaged> seds;
	private final List<DietServiceManager> omninames;
	private final List<DietResourceManaged> forwaders;

	public Diet() {
		this.masterAgents = new ArrayList<DietResourceManaged>();
		this.localAgents = new ArrayList<DietResourceManaged>();
		this.seds = new ArrayList<DietResourceManaged>();
		this.omninames = new ArrayList<DietServiceManager>();
		this.forwaders = new ArrayList<DietResourceManaged>();

		this.maFactory = new MasterAgentFactory(this);
		this.laFactory = new LocalAgentFactory();
		this.sedFactory = new SedFactory();
		this.omFactory = new OmniNamesFactory();
		this.forwFactory = new ForwardersFactory(this);
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
	public List<DietServiceManager> getOmninames() {
		return omninames;
	}

	/**
	 * 
	 * @param sedDiet
	 * @throws DietResourceCreationException
	 */
	public void addSed(Sed sedDiet) throws DietResourceCreationException {
		sedFactory.create(sedDiet);
		this.seds.add(sedFactory.create(sedDiet));

	}

	/**
	 * @param dietResource
	 * @throws DietResourceCreationException
	 */
	public void addLocalAgent(LocalAgent dietResource)
			throws DietResourceCreationException {
		this.localAgents.add(laFactory.create(dietResource));
	}

	/**
	 * @param dietResource
	 * @throws DietResourceCreationException
	 */
	public void addMasterAgent(MasterAgent dietResource)
			throws DietResourceCreationException {
		this.masterAgents.add(maFactory.create(dietResource));

	}

	/**
	 * 
	 * @param forwarder
	 * @throws DietResourceCreationException
	 */
	public void addForwarders(Forwarders forwarder)
			throws DietResourceCreationException {
		DietResourceManaged[] managedForwarders =  forwFactory.create(forwarder);
		if(managedForwarders.length != 2) throw new DietResourceCreationException("TODO: What's the fuck");
		this.forwaders.add(managedForwarders[0]);
		this.forwaders.add(managedForwarders[1]);
	}

	/**
	 * @param omniNames
	 * @throws DietResourceCreationException
	 */
	public void addOmniName(OmniNames omniName)
			throws DietResourceCreationException {
		this.omninames.add(omFactory.create(omniName));
	}

	/**
	 * Create a new list contains a reference on all Software Managed by Godiet
	 * Contains a reference on OmniNames, *Agents and SeDs.
	 * 
	 * @return A list of all softwares managed by godiet
	 */
	public List<SoftwareManager> getAllDietSoftwareManaged() {
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
	 * @param managedSoftware
	 * @return The managed omniName which are is in the managedSoftware's
	 *         domain. Null if it's not found
	 */
	public OmniNames getOmniName(SoftwareManager managedSoftware) {
		Domain domain = managedSoftware.getPluggedOn().getDomain();

		for (DietServiceManager omniName : omninames) {
			if (omniName.getPluggedOn().getDomain().getLabel()
					.equals(domain.getLabel())) {
				return (OmniNames) omniName.getSoftwareDescription();
			}

		}
		log.error("Unable to find a known domain for the software with the name: "
				+ managedSoftware.getSoftwareDescription().getId());
		return null;
	}
}
