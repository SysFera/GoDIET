package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;
/**
 * MA Runtime Validation
 * @author phi
 *
 */
public class MasterAgentRuntimeValidatorImpl extends RuntimeValidator {

	public MasterAgentRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * Check if the OmniNames running
	 */
	@Override
	public void wantLaunch(SoftwareManager ma) throws LaunchException {
		// get the omniNames
		DietResourceManaged omniNamesManaged = dietManager
				.getManagedOmniName(ma.getPluggedOn().getDomain());
		
		//Could never happen
		if (omniNamesManaged == null) {
			throw new LaunchException("Could not start"
					+ ma.getSoftwareDescription().getId()
					+ ". Unable to find his omniNames");
		}

		ResourceState omniNameState = omniNamesManaged.getState();

		if (!omniNameState.getStatus().equals(State.UP))
		{
			throw new LaunchException("Could not start"
					+ ma.getSoftwareDescription().getId() + ". Start "
					+ omniNamesManaged.getSoftwareDescription().getId()
					+ " omniNames before.");
		}
	}

	@Override
	public void wantStop(SoftwareManager managedResource) throws StopException {
	
	}

	@Override
	public void wantIncubate(SoftwareManager managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((MasterAgent)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
	}

}
