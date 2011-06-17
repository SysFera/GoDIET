package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed MA factory
 * 
 * @author phi
 * 
 */
public class MasterAgentFactory {
	private final SoftwareController softwareController;

	private final RuntimeValidator<DietResourceManaged<MasterAgent>> validator;
	public MasterAgentFactory(SoftwareController softwareController,RuntimeValidator<DietResourceManaged<MasterAgent>> maValidator) {
		this.softwareController = softwareController;
		this.validator = maValidator;
	}

	/**
	 * Create a managed MasterAgent given his description. Check validity. Set
	 * the default option if needed (like command launch).
	 * 
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException
	 * @throws IncubateException 
	 */
	public DietResourceManaged<MasterAgent> create(MasterAgent masterAgentDescription,Resource pluggedOn,OmniNamesManaged omniNames) throws DietResourceCreationException, IncubateException
	{
		DietResourceManaged<MasterAgent> MAManaged = new DietResourceManaged<MasterAgent>(masterAgentDescription,pluggedOn, softwareController, validator,omniNames);
		
		AgentFactoryUtil.settingConfigurationOptions(MAManaged,"DIET_MASTER_AGENT");
		AgentFactoryUtil.settingRunningCommand(omniNames.getSoftwareDescription(),MAManaged);
		return MAManaged;
	}

}
