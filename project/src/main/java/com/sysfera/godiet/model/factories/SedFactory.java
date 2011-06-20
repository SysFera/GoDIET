package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
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
	 */
	public DietResourceManaged create(Sed sedDescription, OmniNames omniNames) {
		DietResourceManaged sedManaged = new DietResourceManaged(
				softwareController, validator);
		sedManaged.setManagedSoftware(sedDescription);

		settingConfigurationOptions(sedManaged);
		AgentFactoryUtil.settingRunningCommand(omniNames, sedManaged);
		return sedManaged;
	}

	private void settingConfigurationOptions(DietResourceManaged sedManaged) {

		Options opts = sedManaged.getSoftwareDescription().getCfgOptions();
		if (opts == null) {
			opts = new ObjectFactory().createOptions();
		}

		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(sedManaged.getSoftwareDescription().getParent().getId());

		opts.getOption().add(parent);
		sedManaged.getSoftwareDescription().setCfgOptions(opts);
	}

}
