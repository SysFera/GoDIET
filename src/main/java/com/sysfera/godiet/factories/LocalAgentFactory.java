package com.sysfera.godiet.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManager;
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
	 * the default option if needed (like command launch).
	 * 
	 * @param localAgentDescription
	 * @return The managed LocalAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManager create(LocalAgent localAgentDescription) throws DietResourceCreationException {
		
		DietResourceManager localAgentManaged = new DietResourceManager();
		localAgentManaged.setDietAgent(localAgentDescription);
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
	private void settingConfigurationOptions(DietResourceManager localAgent)
			throws DietResourceCreationException {
		if (localAgent.getPluggedOn() == null || localAgent.getDietAgent().getParent() ==null) {
			throw new DietResourceCreationException(localAgent.getDietAgent()
					.getId() + " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();
		
		Option type = new Option();
		type.setKey("agentType");
		type.setValue("DIET_MASTER_AGENT");
		Option parent = new Option();
		parent.setKey("parentName");
		parent.setValue(localAgent.getDietAgent().getParent().getId());
		Option name = new Option();
		name.setKey("name");
		name.setValue(localAgent.getDietAgent().getId());
		opts.getOption().add(type);
		opts.getOption().add(parent);
		opts.getOption().add(name);
		
		localAgent.getDietAgent().setCfgOptions(opts);
	}
}
