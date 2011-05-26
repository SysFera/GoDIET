package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.validators.RuntimeValidator;


/**
 * Managed LA factory
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
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged<LocalAgent> create(LocalAgent localAgentDescription, Node pluggedOn, DietServiceManaged<OmniNames> omniNames) throws DietResourceCreationException {
		
		DietResourceManaged<LocalAgent> localAgentManaged = new DietResourceManaged<LocalAgent>(pluggedOn,softwareController,validator,omniNames);
		localAgentManaged.setSoftwareDescription(localAgentDescription);
		AgentFactoryUtil.settingConfigurationOptions(localAgentManaged,"DIET_LOCAL_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames.getSoftwareDescription(),localAgentManaged);
		return localAgentManaged;
	}
	

}
