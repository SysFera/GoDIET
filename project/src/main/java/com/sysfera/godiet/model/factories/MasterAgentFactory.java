package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.utils.ResourceUtil;


/**
 * Managed MA factory
 * 
 * @author phi
 *
 */
public class MasterAgentFactory {
	// Needed to find the omniNames of the Domain
	private final Diet dietPlatform;

	
	public MasterAgentFactory(Diet dietPlatform) {
		this.dietPlatform = dietPlatform;
	}
	/**
	 * Create a managed MasterAgent given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(MasterAgent masterAgentDescription) throws DietResourceCreationException
	{
		DietResourceManaged MAManaged = new DietResourceManaged();
		MAManaged.setManagedSoftware(masterAgentDescription);
		AgentFactoryUtil.settingConfigurationOptions(MAManaged,"DIET_MASTER_AGENT");
		AgentFactoryUtil.settingRunningCommand(dietPlatform,MAManaged);
		return MAManaged;
	}
	
	
}
