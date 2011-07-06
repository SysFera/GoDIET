package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.softwares.DietResourceManaged;

/**
 * Forwarders Runtime Validation
 * 
 * @author phi
 * 
 */
public class ForwarderRuntimeValidatorImpl extends RuntimeValidator<DietResourceManaged<Forwarder>> {

	public ForwarderRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * TODO: note: take care about forwarder type:CLIENT or SERVER 
	 */
	@Override
	public void wantLaunch(DietResourceManaged<Forwarder> ma) throws LaunchException {
	
	}

	//TODO: check if childs ( la,sed ) currently running
	@Override
	public void wantStop(DietResourceManaged<Forwarder> managedResource) throws StopException {

	}

	@Override
	public void wantIncubate(DietResourceManaged<Forwarder> managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((Forwarder)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
		
	}

}
