/**
 * 
 */
package com.sysfera.godiet.model;

import com.sysfera.godiet.model.validators.RuntimeValidator;


/**
 * Use to manage and control all DIET services (OmniNames ...)
 * TODO: Delete or extend this class
 * @author phi
 * 
 */
public class DietServiceManaged extends DietResourceManaged {

	public DietServiceManaged(SoftwareController softwareController,RuntimeValidator validator) {
		super(softwareController,validator);
	}

}
