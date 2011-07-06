package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareController;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed LA factory
 * 
 * @author phi
 * 
 */
public class LocalAgentFactory {

	private final SoftwareController softwareController;
	private final RuntimeValidator<DietResourceManaged<LocalAgent>> validator;
	
	public LocalAgentFactory(SoftwareController softwareController,RuntimeValidator<DietResourceManaged<LocalAgent>> laValidator) {

		this.softwareController = softwareController;
		this.validator = laValidator;
	}

	/**
	 * Create a managed LocalAgent given his description. Check validity. Set
	 * the default option if needed.
	 * 
	 * @param localAgentDescription
	 * @return The managed LocalAgent
	 * @throws IncubateException 
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged<LocalAgent> create(LocalAgent localAgentDescription, Resource pluggedOn, OmniNamesManaged omniNames) throws IncubateException, DietResourceCreationException  {
		
		DietResourceManaged<LocalAgent> localAgentManaged = new DietResourceManaged<LocalAgent>(localAgentDescription,pluggedOn,softwareController,validator,omniNames);
		AgentFactoryUtil.settingConfigurationOptions(localAgentManaged,"DIET_LOCAL_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames.getSoftwareDescription(),localAgentManaged);
		return localAgentManaged;
	}

}
