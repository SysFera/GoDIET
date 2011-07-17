/**
 * 
 */
package com.sysfera.godiet.model.softwares;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.validators.RuntimeValidator;

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
