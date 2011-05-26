package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.Node;
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
	 */
	public DietResourceManaged<Sed> create(Sed sedDescription, Node pluggedOn,
			DietServiceManaged<OmniNames> omniNames) {
		DietResourceManaged<Sed> sedManaged = new DietResourceManaged<Sed>(pluggedOn, softwareController, validator, omniNames);
		sedManaged.setSoftwareDescription(sedDescription);

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
		Options opts = new ObjectFactory().createOptions();
		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(sedManaged.getSoftwareDescription().getParent().getId());
		opts.getOption().add(parent);
		sedManaged.getSoftwareDescription().setCfgOptions(opts);
	}

}
