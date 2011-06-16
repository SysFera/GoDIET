package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;

/**
 * Forwarders Runtime Validation
 * 
 * @author phi
 * 
 */
public class ForwarderRuntimeValidatorImpl extends RuntimeValidator {

	public ForwarderRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * TODO: note: take care about forwarder type:CLIENT or SERVER 
	 */
	@Override
	public void wantLaunch(SoftwareManager ma) throws LaunchException {
	
	}

	//TODO: check if childs ( la,sed ) currently running
	@Override
	public void wantStop(SoftwareManager managedResource) throws StopException {

	}

	@Override
	public void wantIncubate(SoftwareManager managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((Forwarder)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
		
	}

}
