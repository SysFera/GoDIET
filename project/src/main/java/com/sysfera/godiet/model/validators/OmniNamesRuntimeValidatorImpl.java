package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareManager;

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

}
