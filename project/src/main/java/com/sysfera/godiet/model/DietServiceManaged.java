/**
 * 
 */
package com.sysfera.godiet.model;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.validators.RuntimeValidator;


/**
 * Use to manage and control all DIET services (OmniNames ...)
 * TODO: Delete or extend this class
 * @author phi
 * 
 */
public class DietServiceManaged extends DietResourceManaged {

	public DietServiceManaged(Software software,SoftwareController softwareController,RuntimeValidator validator) throws IncubateException {
		super(software,softwareController,validator);
	}
	
	private Domain domain;
	public Domain getDomain()
	{
		return domain;
	}
	
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
}
