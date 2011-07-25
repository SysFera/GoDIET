package com.sysfera.godiet.core.model.validators;

import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.states.ResourceState.State;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;
/**
 * MA Runtime Validation
 * @author phi
 *
 */
public class MasterAgentRuntimeValidatorImpl extends RuntimeValidator<DietResourceManaged<MasterAgent>> {

	public MasterAgentRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * Check if the OmniNames running
	 */
	@Override
	public void wantLaunch(DietResourceManaged<MasterAgent> ma) throws LaunchException {
		// get the omniNames
		OmniNamesManaged omniNamesManaged = ma.getOmniNames();
		
		//Could never happen
		if (omniNamesManaged == null) {
			throw new LaunchException("Could not start"
					+ ma.getSoftwareDescription().getId()
					+ ". Unable to find his omniNames");
		}

		State omniNameState = omniNamesManaged.getState();

		if (!omniNameState.equals(State.UP))
		{
			throw new LaunchException("Could not start"
					+ ma.getSoftwareDescription().getId() + ". Start "
					+ omniNamesManaged.getSoftwareDescription().getId()
					+ " omniNames before.");
		}
	}


	@Override
	public void wantStop(DietResourceManaged<MasterAgent> managedResource)
			throws StopException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wantIncubate(DietResourceManaged<MasterAgent> managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((MasterAgent)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
	}

}
