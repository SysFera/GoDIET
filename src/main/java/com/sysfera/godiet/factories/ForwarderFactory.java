package com.sysfera.godiet.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManager;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;

/**
 * Managed Forwarder factory
 * 
 * @author phi
 * 
 */
public class ForwarderFactory {
	private static String FORWARDERBINARY = "dietForwarder";

	public static enum ForwarderType {
		CLIENT("CLIENT"), SERVER("SERVER");
		public final String label;
		private ForwarderType(String label) {
			this.label = label;
		}	
		

	}


	/**
	 * TODO: Need to move this function
	 * 
	 * @param gateway
	 * @param type
	 * @return
	 */
	public Forwarder create(Gateway gateway, ForwarderFactory.ForwarderType type) {
		Forwarder forwarder = new Forwarder();
		Config config = new Config();
		config.setServer(gateway.getRef());
		config.setRemoteBinary(FORWARDERBINARY);
		forwarder.setConfig(config);
		forwarder.setId("DietForwarder-" + gateway.getId() + "-" + type);
		switch (type) {
		case CLIENT:
			forwarder.setType("CLIENT");
			break;

		case SERVER:
			forwarder.setType("SERVER");
			break;

		default:
			break;
		}
		return forwarder;
	}

	/**
	 * Create a managed diet resource. Check description validity and add
	 * default parameters if needed.
	 * 
	 * @param forwarder Forwarder description
	 * @return The Managed forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	public DietResourceManager create(Forwarder forwarder)
			throws DietResourceCreationException {

		DietResourceManager dietResourceManaged = new DietResourceManager();
		dietResourceManaged.setDietAgent(forwarder);
		settingConfigurationOptions(dietResourceManaged);

		return dietResourceManaged;
	}

	/**
	 * Init default value
	 * @param forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManager forwarder)
			throws DietResourceCreationException {
		if (forwarder.getPluggedOn() == null) {
			throw new DietResourceCreationException(forwarder.getDietAgent()
					.getId() + " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();

		Option accept = new Option();
		accept.setKey("accept");
		accept.setValue(".*");
		Option reject = new Option();
		reject.setKey("reject");
		reject.setValue(forwarder.getPluggedOn().getSsh().getServer());
		opts.getOption().add(accept);
		opts.getOption().add(reject);
		forwarder.getDietAgent().setCfgOptions(opts);

	}
}
