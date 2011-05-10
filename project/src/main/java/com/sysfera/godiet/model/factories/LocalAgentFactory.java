package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.validators.RuntimeValidator;


/**
 * Managed LA factory
 * @author phi
 *
 */
public class LocalAgentFactory {
	
	
	private final SoftwareController softwareController;
	private final RuntimeValidator validator;
	public LocalAgentFactory(SoftwareController softwareController,RuntimeValidator laValidator) {
		this.softwareController = softwareController;
		this.validator = laValidator;
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
		
		DietResourceManaged localAgentManaged = new DietResourceManaged(softwareController,validator);
		localAgentManaged.setManagedSoftware(localAgentDescription);
		AgentFactoryUtil.settingConfigurationOptions(localAgentManaged,"DIET_LOCAL_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames,localAgentManaged);
		return localAgentManaged;
	}
	

}
