/**
 * 
 */
package com.sysfera.godiet.core.model.softwares;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * Use to manage and control all DIET services (OmniNames ...)
 * 
 * @author phi
 * 
 */
public class OmniNamesManaged extends SoftwareManager<OmniNames> {

	private List<Domain> domains = new ArrayList<Domain>();

	private String address = null;
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
			RuntimeValidator<OmniNamesManaged> validator )
			throws IncubateException {
		super(description, pluggedOn, softwareController, validator);
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
