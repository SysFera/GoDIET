package com.sysfera.godiet.core.model.validators;

import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.core.managers.DietManager;

/**
 * Use to validate an action (Launch and Stop) in the current context. 
 * @author phi
 *
 */
public abstract class RuntimeValidator<T extends SoftwareInterface<? extends Software>> {
	
	final DietManager dietManager;
	
	public RuntimeValidator(DietManager dietManager) {
		assert dietManager != null;
		this.dietManager = dietManager;
	}
	/**
	 * Check if the managedResource could be launch. Use the platform 
	 * Typically an master agent could be launch only if the omniNames is running.
	 * @param managedResource
	 * @throws LaunchException
	 */
	public  abstract void wantLaunch(T managedResource) throws LaunchException;
	
	public abstract void wantStop(T managedResource) throws StopException;
	
	public abstract void wantIncubate(T managedResource) throws IncubateException;

	
	
}
