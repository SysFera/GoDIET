package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.LocalAgent;


/**
 * Managed LA factory
 * @author phi
 *
 */
public class LocalAgentFactory {
	
	
	private final Diet dietPlatform;
	public LocalAgentFactory(Diet dietPlatform) {
		this.dietPlatform = dietPlatform;
	}
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
		AgentFactoryUtil.settingConfigurationOptions(localAgentManaged,"DIET_LOCAL_AGENT");
		AgentFactoryUtil.settingRunningCommand(dietPlatform,localAgentManaged);
		return localAgentManaged;
	}
	

}
