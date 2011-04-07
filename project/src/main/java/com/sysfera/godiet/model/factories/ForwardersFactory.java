package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;

/**
 * Managed Forwarder factory
 * 
 * @author phi
 * 
 */
public class ForwardersFactory {
	private static String FORWARDERBINARY = "dietForwarder";

	// Needed to find the omniNames of the Domain
	private final Diet dietPlatform;

	public static enum ForwarderType {
		CLIENT("CLIENT"), SERVER("SERVER");
		public final String label;

		private ForwarderType(String label) {
			this.label = label;
		}

	}

	public ForwardersFactory(Diet dietPlatform) {
		this.dietPlatform = dietPlatform;
	}

	/**
	 * TODO: Need to move this function. Describe Forwarder in XSD and use the
	 * Jaxb factory
	 * 
	 * @param gateway
	 * @param type
	 * @return
	 */
	public static Forwarder create(Gateway gateway,
			ForwardersFactory.ForwarderType type) {
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
	 * @param forwarder
	 *            Forwarder description
	 * @return The Managed forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	public DietResourceManaged create(Forwarder forwarder)
			throws DietResourceCreationException {

		DietResourceManaged dietResourceManaged = new DietResourceManaged();
		dietResourceManaged.setManagedSoftware(forwarder);
		settingConfigurationOptions(dietResourceManaged);
		buildForwarderCommand(dietResourceManaged);
		return dietResourceManaged;
	}

	/**
	 * Init default value
	 * 
	 * @param forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManaged forwarder)
			throws DietResourceCreationException {
		if (forwarder.getPluggedOn() == null) {
			throw new DietResourceCreationException(forwarder
					.getSoftwareDescription().getId()
					+ " not plugged on physical resource");
		}

		ObjectFactory factory = new ObjectFactory();
		Options opts = factory.createOptions();

		Option accept = factory.createOptionsOption();
		accept.setKey("accept");
		accept.setValue(".*");
		Option reject = factory.createOptionsOption();
		reject.setKey("reject");
		reject.setValue(forwarder.getPluggedOn().getSsh().getServer());
		opts.getOption().add(accept);
		opts.getOption().add(reject);
		forwarder.getSoftwareDescription().setCfgOptions(opts);

	}

	/**
	 * Build the diet forwarder running command
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg
	 * 
	 * @param softManaged
	 * @return
	 */
	private void buildForwarderCommand(SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
		.getDir();
		Forwarder forwarderDescription = (Forwarder) softManaged.getSoftwareDescription();
		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		OmniNames omniName = dietPlatform.getOmniName(softManaged);
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniName.getId() + ".cfg";
		command += omniOrbconfig + " ";
		//specific command if it's a CLIENT or SERVER
		if(forwarderDescription.getType().equals("SERVER")){
			
		}
		else{
			
		}
	
		

		command += command += " ";

		softManaged.setRunningCommand(command);
	}
}
