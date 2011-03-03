package com.sysfera.godiet.factories;

import com.sysfera.godiet.model.xml.DietResourceManager;
import com.sysfera.godiet.model.xml.generated.Sed;


/**
 * Managed sed factory
 * @author phi
 *
 */
public class SedFactory {
	
	/**
	 * Create a managed sed given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param sedDescription
	 * @return The managed Sed
	 */
	public DietResourceManager create(Sed sedDescription)
	{
		DietResourceManager sedManaged = new DietResourceManager();
		sedManaged.setDietAgent(sedDescription);
		
		return sedManaged;
	}
}
