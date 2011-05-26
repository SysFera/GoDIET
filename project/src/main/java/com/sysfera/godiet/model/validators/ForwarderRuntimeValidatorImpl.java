package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Forwarder;

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

}
