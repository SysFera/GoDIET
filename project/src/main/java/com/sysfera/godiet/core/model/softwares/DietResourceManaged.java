package com.sysfera.godiet.core.model.softwares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */

public class DietResourceManaged<T extends Software> extends SoftwareManager<T> {
	private Logger log = LoggerFactory.getLogger(getClass());

	private OmniNamesManaged omniNames;

	public DietResourceManaged(T description,Resource pluggedOn,
			SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<T>> validator,
			OmniNamesManaged omniNames) throws IncubateException {
		super(description, pluggedOn, softwareController, validator);
		this.omniNames = omniNames;

	}

	public OmniNamesManaged getOmniNames() {
		return this.omniNames;
	}

}
