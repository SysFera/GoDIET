package com.sysfera.godiet.core.model.validators;

import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState.State;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;

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
		SoftwareInterface<? extends Software> managedParent = dietManager
				.getManagedSoftware(sed.getSoftwareDescription().getParent()
						.getId());
		State parentMaState = managedParent.getState();
		synchronized (parentMaState) {
			if (!parentMaState.equals(State.UP)) {
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
