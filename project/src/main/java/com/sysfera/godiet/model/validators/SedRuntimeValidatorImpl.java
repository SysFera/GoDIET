package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;

/**
 * MA Runtime Validation
 * 
 * @author phi
 * 
 */
public class SedRuntimeValidatorImpl extends RuntimeValidator {

	public SedRuntimeValidatorImpl(DietManager dietManager) {
		super(dietManager);
	}

	/**
	 * Check if the parent (Ma or La)  running
	 */
	@Override
	public void wantLaunch(SoftwareManager sed) throws LaunchException {
		SoftwareManager managedParent = dietManager.getManagedSoftware(sed.getSoftwareDescription().getParent().getId());
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
	public void wantStop(SoftwareManager managedResource) throws StopException {
		//Nothing to do
	}

}
