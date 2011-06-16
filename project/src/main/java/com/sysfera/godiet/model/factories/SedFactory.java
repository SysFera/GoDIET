package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed sed factory
 * 
 * @author phi
 * 
 */
public class SedFactory {

	private final SoftwareController softwareController;
	private final RuntimeValidator validator;

	public SedFactory(SoftwareController softwareController,
			RuntimeValidator sedValidator) {
		this.softwareController = softwareController;
		this.validator = sedValidator;
	}

	/**
	 * Create a managed sed given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * @param sedDescription
	 * @return The managed Sed
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(Sed sedDescription, Resource pluggedOn,OmniNames omniNames) throws DietResourceCreationException {
		try {
			DietResourceManaged sedManaged = new DietResourceManaged(sedDescription,
					softwareController, validator);
			sedManaged.setPluggedOn(pluggedOn);
			settingConfigurationOptions(sedManaged);
			AgentFactoryUtil.settingRunningCommand(omniNames, sedManaged);
			return sedManaged;
		} catch (IncubateException e) {
			throw new DietResourceCreationException("Resource creation fail.",
					e);
		}
	}

	private void settingConfigurationOptions(DietResourceManaged sedManaged) {
		Options opts = new ObjectFactory().createOptions();
		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(sedManaged.getSoftwareDescription().getParent().getId());
		opts.getOption().add(parent);
		sedManaged.getSoftwareDescription().setCfgOptions(opts);
	}

}
