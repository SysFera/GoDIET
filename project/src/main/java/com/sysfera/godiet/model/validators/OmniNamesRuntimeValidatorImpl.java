package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.OmniNames;

public class OmniNamesRuntimeValidatorImpl extends RuntimeValidator{

	public OmniNamesRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	@Override
	public void wantLaunch(SoftwareManager managedResource)
			throws LaunchException {
		//Nothing to do
		
	}

	//TODO: check if all childs (ma, la, sed) are down
	@Override
	public void wantStop(SoftwareManager managedResource) throws StopException {
		
		
	}

	@Override
	public void wantIncubate(SoftwareManager managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((OmniNames)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
		
	}

}
