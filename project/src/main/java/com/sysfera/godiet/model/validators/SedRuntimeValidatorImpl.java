package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;

/**
 * Sed Runtime Validation
 * 
 * @author phi
 * 
 */
public class SedRuntimeValidatorImpl extends
		RuntimeValidator<DietResourceManaged<Sed>> {

	public SedRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * Check if the parent (Ma or La) currently running
	 */
	@Override
	public void wantLaunch(DietResourceManaged<Sed> sed) throws LaunchException {
		SoftwareManager<? extends Software> managedParent = dietManager
				.getManagedSoftware(sed.getSoftwareDescription().getParent()
						.getId());
		ResourceState parentMaState = managedParent.getState();
		synchronized (parentMaState) {
			if (!parentMaState.getStatus().equals(State.UP)) {
				throw new LaunchException("Could not start"
						+ sed.getSoftwareDescription().getId() + ". Start "
						+ managedParent.getSoftwareDescription().getId()
						+ " parent (MA or LA) before.");
			}

		}
	}

	@Override
	public void wantStop(DietResourceManaged<Sed> managedResource)
			throws StopException {
		// Nothing to do
	}

	@Override
	public void wantIncubate(DietResourceManaged<Sed> managedResource)
			throws IncubateException {
		try {
			BuildingValidator
					.validate((Sed) managedResource.getSoftwareDescription(),
							dietManager);
		} catch (DietResourceValidationException e) {
			throw new IncubateException(
					"Software description unvalide. Can't incubate it.", e);
		}

	}

}
