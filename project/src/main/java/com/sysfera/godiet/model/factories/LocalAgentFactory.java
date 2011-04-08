package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;


/**
 * Managed LA factory
 * @author phi
 *
 */
public class LocalAgentFactory {
	
	
	
	/**
	 * Create a managed LocalAgent given his description. Check validity. Set
	 * the default option if needed.
	 * 
	 * @param localAgentDescription
	 * @return The managed LocalAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(LocalAgent localAgentDescription) throws DietResourceCreationException {
		
		DietResourceManaged localAgentManaged = new DietResourceManaged();
		localAgentManaged.setManagedSoftware(localAgentDescription);
		settingConfigurationOptions(localAgentManaged);
		return localAgentManaged;
	}
	
	/**
	 * Init default configuration values
	 * TODO: Check if options are setted
	 * @param localAgent
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManaged localAgent)
			throws DietResourceCreationException {
		if (localAgent.getPluggedOn() == null || localAgent.getSoftwareDescription().getParent() ==null) {
			throw new DietResourceCreationException(localAgent.getSoftwareDescription()
					.getId() + " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();
		
		Option type = new Option();
		type.setKey("agentType");
		type.setValue("DIET_MASTER_AGENT");
		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(localAgent.getSoftwareDescription().getParent().getId());
		Option name = new Option();
		name.setKey("name");
		name.setValue(localAgent.getSoftwareDescription().getId());
		opts.getOption().add(type);
		opts.getOption().add(parent);
		opts.getOption().add(name);
		
		localAgent.getSoftwareDescription().setCfgOptions(opts);
	}
}
