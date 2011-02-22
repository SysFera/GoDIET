package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Model.Elements;
import com.sysfera.godiet.Model.Forwarder;
import com.sysfera.godiet.Model.xml.generated.*;
import com.sysfera.godiet.Model.physicalresources.GatewayResource;
import com.sysfera.godiet.Model.xml.generated.DietConfiguration;
import com.sysfera.godiet.factories.ForwarderFactory;

/**
 * NG Diet + Infrastructure manager.
 * 
 * @author phi
 * 
 */
public class ResourcesManager {

	//Root 
	private  DietConfiguration dietConfiguration;

	//Create the forwarder
	private ForwarderFactory forwarderFactory = new ForwarderFactory();

	public ResourcesManager() {

	}
	
	public DietConfiguration getDietConfiguration() {
		return dietConfiguration;
	}
	
	public void setDietConfiguration(DietConfiguration dietConfiguration) {
		this.dietConfiguration = dietConfiguration;
	}
	
	/**
	 * Reset and set the datamodel with the DietConfigurtion instance
	 */
	public void init()
	{
		if(dietConfiguration !=null)
		{
			
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
		return dietConfiguration.getInfrastructure().getLink();
	}

}
