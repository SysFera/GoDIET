package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Sed;


/**
 * Managed sed factory
 * @author phi
 *
 */
public class SedFactory {
	
	private final SoftwareController softwareController;
	public SedFactory(SoftwareController softwareController) {
		this.softwareController = softwareController;
	}

	/**
	 * Create a managed sed given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param sedDescription
	 * @return The managed Sed
	 */
	public DietResourceManaged create(Sed sedDescription,OmniNames omniNames)
	{
		DietResourceManaged sedManaged = new DietResourceManaged(softwareController);
		sedManaged.setManagedSoftware(sedDescription);
	

		settingConfigurationOptions(sedManaged);
		AgentFactoryUtil.settingRunningCommand(omniNames,sedManaged);
		return sedManaged;
	}
	
	private void settingConfigurationOptions(DietResourceManaged sedManaged)
	{
		Options opts = new ObjectFactory().createOptions();
		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(sedManaged.getSoftwareDescription().getParent().getId());
		opts.getOption().add(parent);
		sedManaged.getSoftwareDescription().setCfgOptions(opts);
	}
	

}
