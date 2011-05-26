/**
 * 
 */
package com.sysfera.godiet.model;

import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.validators.RuntimeValidator;


/**
 * Use to manage and control all DIET services (OmniNames ...)
 * @author phi
 * 
 */
public class DietServiceManaged<T extends Software> extends SoftwareManager<T> {

	public DietServiceManaged(Resource pluggedOn, SoftwareController softwareController,RuntimeValidator validator) {
		super(pluggedOn,softwareController,validator);
	}

}
