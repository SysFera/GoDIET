package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;


/**
 * Managed MA factory
 * 
 * @author phi
 *
 */
public class MasterAgentFactory {
	private final SoftwareController softwareController;

	public MasterAgentFactory(SoftwareController softwareController) {
		this.softwareController = softwareController;
	}

	/**
	 * Create a managed MasterAgent given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(MasterAgent masterAgentDescription,OmniNames omniNames) throws DietResourceCreationException
	{
		DietResourceManaged MAManaged = new DietResourceManaged(softwareController);
		MAManaged.setManagedSoftware(masterAgentDescription);
		AgentFactoryUtil.settingConfigurationOptions(MAManaged,"DIET_MASTER_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames,MAManaged);
		return MAManaged;
	}
	
	
}
