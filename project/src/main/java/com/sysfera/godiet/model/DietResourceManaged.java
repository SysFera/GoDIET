package com.sysfera.godiet.model;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Use to manage and control all DIET elements (SeD, MA, LA, MA_DAG)
 * 
 * @author phi
 * 
 */
public class DietResourceManaged extends SoftwareManager {



	public DietResourceManaged(Software software,SoftwareController softwareController,RuntimeValidator validator) throws IncubateException {
		super(software,softwareController,validator);
	}







}
