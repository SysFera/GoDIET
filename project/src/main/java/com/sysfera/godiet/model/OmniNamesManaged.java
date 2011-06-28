/**
 * 
 */
package com.sysfera.godiet.model;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.validators.RuntimeValidator;
import com.sysfera.godiet.model.generated.OmniNames;

/**
 * Use to manage and control all DIET services (OmniNames ...)
 * 
 * @author phi
 * 
 */
public class OmniNamesManaged extends SoftwareManager<OmniNames> {

	private List<Domain> domains = new ArrayList<Domain>();

	/**
	 * Return the list of domains covered by this OmninNames
	 * 
	 * @return a list of domains
	 */
	public List<Domain> getDomains() {
		return domains;
	}



	public OmniNamesManaged(OmniNames description, Resource pluggedOn,
			SoftwareController softwareController,
			RuntimeValidator<OmniNamesManaged> validator)
			throws IncubateException {
		super(description, pluggedOn, softwareController, validator);
	}
}
