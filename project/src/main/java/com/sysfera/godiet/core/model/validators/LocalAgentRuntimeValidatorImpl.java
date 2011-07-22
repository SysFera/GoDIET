package com.sysfera.godiet.core.model.validators;

import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.common.model.states.ResourceState.State;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;

/**
 * MA Runtime Validation
 * 
 * @author phi
 * 
 */
public class LocalAgentRuntimeValidatorImpl extends RuntimeValidator<DietResourceManaged<LocalAgent>> {

	public LocalAgentRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * Check if the parent Ma  running
	 */
	@Override
	public void wantLaunch(DietResourceManaged<LocalAgent> ma) throws LaunchException {
		SoftwareInterface parentMa = dietManager.getManagedSoftware(ma
				.getSoftwareDescription().getParent().getId());
		State parentMaState = parentMa.getState();
		synchronized (parentMaState) {
			if (!parentMaState.equals(State.UP)) {
				throw new LaunchException("Could not start"
						+ ma.getSoftwareDescription().getId() + ". Start "
						+ parentMa.getSoftwareDescription().getId()
						+ " MA before.");
			}

		}
	}

	//TODO: check if childs ( la,sed ) currently running
	@Override
	public void wantStop(DietResourceManaged<LocalAgent> managedResource) throws StopException {

	}

	@Override
	public void wantIncubate(DietResourceManaged<LocalAgent> managedResource)
			throws IncubateException {
		try {
			BuildingValidator.validate((LocalAgent)managedResource.getSoftwareDescription(), dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException("Software description unvalide. Can't incubate it.",e);
		}
		
	}

}
