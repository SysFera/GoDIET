package com.sysfera.godiet.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */
public class DietResourceManaged<T extends Software> extends SoftwareManager<T> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private DietServiceManaged<OmniNames> omniNames;

	public DietResourceManaged(Resource pluggedOn, SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<T>> validator, DietServiceManaged<OmniNames> omniNames) {
		super(pluggedOn, softwareController, validator);
	}

	public DietServiceManaged<OmniNames> getOmniNames() {
		return this.omniNames;
	}

}
