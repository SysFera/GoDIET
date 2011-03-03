package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.factories.ForwarderFactory;
import com.sysfera.godiet.factories.LocalAgentFactory;
import com.sysfera.godiet.factories.MasterAgentFactory;
import com.sysfera.godiet.factories.OmniNamesFactory;
import com.sysfera.godiet.factories.SedFactory;
import com.sysfera.godiet.model.DietResourceManager;
import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.generated.Forwarder;
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

	private final MasterAgentFactory maFactory;
	private final LocalAgentFactory laFactory;
	private final SedFactory sedFactory;
	private final OmniNamesFactory omFactory;
	private final ForwarderFactory forwFactory;

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

		this.maFactory = new MasterAgentFactory();
		this.laFactory = new LocalAgentFactory();
		this.sedFactory = new SedFactory();
		this.omFactory = new OmniNamesFactory();
		this.forwFactory = new ForwarderFactory();
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
	public void addForwarder(Forwarder forwarder)
			throws DietResourceCreationException {

		this.forwaders.add(forwFactory.create(forwarder));
	}

	/**
	 * @param omniNames
	 * @throws DietResourceCreationException
	 */
	public void addOmniName(OmniNames omniName)
			throws DietResourceCreationException {
		this.omninames.add(omFactory.create(omniName));
	}

}
