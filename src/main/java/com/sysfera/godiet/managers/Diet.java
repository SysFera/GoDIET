package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Model.xml.DietResource;
import com.sysfera.godiet.Model.xml.generated.DietHierarchy;
import com.sysfera.godiet.Model.xml.generated.DietServices;
import com.sysfera.godiet.Model.xml.generated.LocalAgent;
import com.sysfera.godiet.Model.xml.generated.MasterAgent;
import com.sysfera.godiet.Model.xml.generated.OmniNames;
import com.sysfera.godiet.Model.xml.generated.Sed;

/**
 * Diet platform description
 * 
 * @author phi
 * 
 */
public class Diet {

	private final List<DietResource> masterAgents;
	private final List<DietResource> localAgents;
	private final List<DietResource> seds;
	private final List<OmniNames> omninames;

	public Diet() {
		this.masterAgents = new ArrayList<DietResource>();
		this.localAgents = new ArrayList<DietResource>();
		this.seds = new ArrayList<DietResource>();
		this.omninames = new ArrayList<OmniNames>();
	}

	void init(DietHierarchy dietHierarchy, DietServices dietServices) {

		initMasterAgent(dietHierarchy.getMasterAgent());

		this.omninames.addAll(dietServices.getOmniNames());
	}

	/**
	 * Init masterAgents. Deep tree search
	 * 
	 * @param List
	 *            of masterAgent
	 */
	private void initMasterAgent(List<MasterAgent> masterAgents) {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				DietResource dietResource = new DietResource();
				this.masterAgents.add(dietResource);
				initSeds(masterAgent.getSed());
				initLocalAgents(masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call on LocalAgents.
	 * @param localAgent
	 */
	private void initLocalAgents(List<LocalAgent> localAgents) {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				DietResource dietResource = new DietResource();
				this.localAgents.add(dietResource);
				initSeds(localAgent.getSed());
				initLocalAgents(localAgent.getLocalAgent());
			}
			
		}
	}

	/**
	 * 
	 * @param sed
	 */
	private void initSeds(List<Sed> seds) {
		if (seds != null) {
			for (Sed sed : seds) {
				DietResource sedDiet = new DietResource();
				sedDiet.setDietAgent(sed);
				this.seds.add(sedDiet);
			}

		}

	}
}
