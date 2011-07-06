package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareController;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed sed factory
 * 
 * @author phi
 * 
 */
public class SedFactory {

	private final SoftwareController softwareController;
	private final RuntimeValidator<DietResourceManaged<Sed>> validator;

	public SedFactory(SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<Sed>> sedValidator) {

		this.softwareController = softwareController;
		this.validator = sedValidator;
	}

	/**
	 * Create a managed sed given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * @param sedDescription
	 * @return The managed Sed
	 * @throws IncubateException 
	 */


	public DietResourceManaged<Sed> create(Sed sedDescription, Resource pluggedOn,
			OmniNamesManaged omniNames) throws IncubateException {
		DietResourceManaged<Sed> sedManaged = new DietResourceManaged<Sed>(sedDescription,
				pluggedOn, softwareController, validator, omniNames);
		
		settingConfigurationOptions(sedManaged);

		AgentFactoryUtil.settingRunningCommand(
				omniNames.getSoftwareDescription(), sedManaged);
		return sedManaged;
	}

	/**
	 * Set the parentName. Could be the name of a LocalAgent or MasterAgent
	 * 
	 * @param sedManaged
	 */

	private void settingConfigurationOptions(DietResourceManaged<Sed> sedManaged) {

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
