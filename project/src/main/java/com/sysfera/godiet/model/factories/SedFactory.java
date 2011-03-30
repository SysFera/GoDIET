package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Sed;


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
	public DietResourceManaged create(Sed sedDescription)
	{
		DietResourceManaged sedManaged = new DietResourceManaged();
		sedManaged.setManagedSoftware(sedDescription);
		
		return sedManaged;
	}
}
