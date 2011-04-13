package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Options.Option;


/**
 * Managed sed factory
 * @author phi
 *
 */
public class SedFactory {
	
	private final Diet dietPlatform;
	
	public SedFactory(Diet dietPlatform) {
		this.dietPlatform = dietPlatform;
	}
	/**
	 * Create a managed sed given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param sedDescription
	 * @return The managed Sed
	 */
	public DietResourceManaged create(Sed sedDescription)
	{
		DietResourceManaged sedManaged = new DietResourceManaged();
		sedManaged.setManagedSoftware(sedDescription);
	

		settingConfigurationOptions(sedManaged);
		AgentFactoryUtil.settingRunningCommand(dietPlatform,sedManaged);
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
