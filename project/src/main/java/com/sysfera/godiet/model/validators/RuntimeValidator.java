package com.sysfera.godiet.model.validators;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.Software;

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
