package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;

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
		ResourceState parentMaState = parentMa.getState();
		synchronized (parentMaState) {
			if (!parentMaState.getStatus().equals(State.UP)) {
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
