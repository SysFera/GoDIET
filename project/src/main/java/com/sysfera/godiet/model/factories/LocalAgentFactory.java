package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.OmniNames;


/**
 * Managed LA factory
 * @author phi
 *
 */
public class LocalAgentFactory {
	
	
	private final SoftwareController softwareController;

	public LocalAgentFactory(SoftwareController softwareController) {
		this.softwareController = softwareController;
	}

	/**
	 * Create a managed LocalAgent given his description. Check validity. Set
	 * the default option if needed.
	 * 
	 * @param localAgentDescription
	 * @return The managed LocalAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(LocalAgent localAgentDescription, OmniNames omniNames) throws DietResourceCreationException {
		
		DietResourceManaged localAgentManaged = new DietResourceManaged(softwareController);
		localAgentManaged.setManagedSoftware(localAgentDescription);
		AgentFactoryUtil.settingConfigurationOptions(localAgentManaged,"DIET_LOCAL_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames,localAgentManaged);
		return localAgentManaged;
	}
	

}
