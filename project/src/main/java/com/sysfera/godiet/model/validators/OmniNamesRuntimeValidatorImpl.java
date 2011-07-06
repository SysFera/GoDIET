package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareManager;

public class OmniNamesRuntimeValidatorImpl extends RuntimeValidator<OmniNamesManaged>{

	public OmniNamesRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	@Override
	public void wantLaunch(OmniNamesManaged managedResource)
			throws LaunchException {
		//Nothing to do
		
	}

	//TODO: check if all childs (ma, la, sed) are down
	@Override
	public void wantStop(OmniNamesManaged managedResource) throws StopException {
		
		
	}

	@Override
	public void wantIncubate(OmniNamesManaged managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((OmniNames)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
		
	}

}
