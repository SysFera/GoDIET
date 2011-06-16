package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.MasterAgent;
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
	private final RuntimeValidator validator;

	public MasterAgentFactory(SoftwareController softwareController,
			RuntimeValidator maValidator) {
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
	 */
	public DietResourceManaged create(MasterAgent masterAgentDescription,Resource pluggedOn,
			OmniNames omniNames) throws DietResourceCreationException {
		try {
			DietResourceManaged MAManaged = new DietResourceManaged(masterAgentDescription,
					softwareController, validator);
			MAManaged.setPluggedOn(pluggedOn);
			AgentFactoryUtil.settingConfigurationOptions(MAManaged,
					"DIET_MASTER_AGENT");
			AgentFactoryUtil.settingRunningCommand(omniNames, MAManaged);
			return MAManaged;
		} catch (IncubateException e) {
			throw new DietResourceCreationException("Resource creation fail.",
					e);
		}
	}

}
