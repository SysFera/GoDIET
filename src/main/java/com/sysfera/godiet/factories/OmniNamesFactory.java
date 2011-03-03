package com.sysfera.godiet.factories;

import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.generated.OmniNames;


/**
 * Managed OmniNames factory
 * @author phi
 *
 */
public class OmniNamesFactory {
	
	private static String OMNINAMESBINARY = "omniNames";
	
	/**
	 * Create a managed omninames given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param omniNamesDescription
	 * @return The managed omniNames
	 */
	public DietServiceManager create(OmniNames omniNamesDescription) 
	{
		DietServiceManager omniNamesManaged = new DietServiceManager();
		omniNamesManaged.setDietService(omniNamesDescription);
		
		return omniNamesManaged;
	} 
}
