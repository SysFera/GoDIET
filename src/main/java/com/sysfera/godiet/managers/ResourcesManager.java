package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Model.xml.generated.*;
import com.sysfera.godiet.Model.deprecated.Elements;
import com.sysfera.godiet.Model.deprecated.Forwarder;
import com.sysfera.godiet.Model.physicalresources.deprecated.GatewayResource;
import com.sysfera.godiet.factories.ForwarderFactory;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	// Root level of goDiet configuration
	private DietConfiguration goDiet;

	// Other model representation to help and improve resources access. All
	// objects are reference of goDiet field
	
	/**
	 * Set the datamodel with the DietConfigurtion instance
	 */
	public void init(DietConfiguration dietConfiguration) {
		this.goDiet = dietConfiguration;
		if (goDiet != null) {

		}
	}

	/**
	 * Create forwarders. 1 link = 1 forwarder
	 */
	private void initForwarders() {
		List<Elements> forwarders = new ArrayList<Elements>();
		List<Link> links = getAllLinks();

		for (Link link : links) {

			{
				Gateway gatewayFrom = link.getFrom();
				Forwarder forwarderFrom = forwarderFactory.create(gatewayFrom,
						Forwarder.ForwarderType.CLIENT);
				forwarders.add(forwarderFrom);
				addForwarder(forwarderFrom);
			}
			{
				Gateway gatewayTo = link.getTo();
				Forwarder forwarderTo = forwarderFactory.create(gatewayTo,
						Forwarder.ForwarderType.SERVER);
				forwarders.add(forwarderTo);
				addForwarder(forwarderTo);

			}

		}

	}

	private void addForwarder(Forwarder forwarderFrom) {
		// TODO Auto-generated method stub

	}

	private List<Link> getAllLinks() {
		return goDiet.getInfrastructure().getLink();
	}

}
