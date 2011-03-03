package com.sysfera.godiet.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.xml.DietResourceManager;
import com.sysfera.godiet.model.xml.generated.MasterAgent;
import com.sysfera.godiet.model.xml.generated.ObjectFactory;
import com.sysfera.godiet.model.xml.generated.Options;
import com.sysfera.godiet.model.xml.generated.Options.Option;


/**
 * Managed MA factory
 * 
 * @author phi
 *
 */
public class MasterAgentFactory {
	
	private static String MASTERAGENTBINARY = "dietAgent";
	/**
	 * Create a managed MasterAgent given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManager create(MasterAgent masterAgentDescription) throws DietResourceCreationException
	{
		DietResourceManager sedManaged = new DietResourceManager();
		sedManaged.setDietAgent(masterAgentDescription);
		settingConfigurationOptions(sedManaged);
		return sedManaged;
	}
	
	/**
	 * Init default value
	 * @param masterAgent
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManager masterAgent)
			throws DietResourceCreationException {
		if (masterAgent.getPluggedOn() == null) {
			throw new DietResourceCreationException(masterAgent.getDietAgent()
					.getId() + " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();

		Option type = new Option();
		type.setKey("agentType");
		type.setValue("DIET_MASTER_AGENT");
		Option name = new Option();
		name.setKey("name");
		name.setValue(masterAgent.getDietAgent().getId());
		opts.getOption().add(type);
		opts.getOption().add(name);
		
		masterAgent.getDietAgent().setCfgOptions(opts);
		

	}
}
