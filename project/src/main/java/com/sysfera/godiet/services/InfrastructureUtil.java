package com.sysfera.godiet.services;

import java.util.List;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;

public class InfrastructureUtil {

	static OmniNames getOmniNames(DietManager dietManager, Resource resource)
			throws DietResourceCreationException {
		List<DietServiceManaged> omniNamesManaged = dietManager.getOmninames();
		for (DietServiceManaged omniManaged : omniNamesManaged) {
			if(resource.getDomain().equals(omniManaged.getDomain()))
				return (OmniNames)omniManaged.getSoftwareDescription();
		}
		throw new DietResourceCreationException(
					"Unable to find the omniNames for domain "
							+ resource.getDomain()
									.getLabel());
	
	}
}
