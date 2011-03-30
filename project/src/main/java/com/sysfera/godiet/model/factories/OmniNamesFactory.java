package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;

/**
 * Managed OmniNames factory
 * 
 * @author phi
 * 
 */
public class OmniNamesFactory {

	private static String OMNINAMESBINARY = "omniNames";

	/**
	 * Create a managed omninames given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * @param omniNamesDescription
	 * @return The managed omniNames
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	public DietServiceManager create(OmniNames omniNamesDescription)
			throws DietResourceCreationException {
		DietServiceManager omniNamesManaged = new DietServiceManager();

		omniNamesManaged.setManagedSoftware(omniNamesDescription);
		settingConfigurationOptions(omniNamesManaged);

		return omniNamesManaged;
	}

	/**
	 * Init default value
	 * 
	 * @param omniNamesManaged
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietServiceManager omniNamesManaged)
			throws DietResourceCreationException {
		Node plugged = omniNamesManaged.getPluggedOn();
		if (plugged == null) {
			throw new DietResourceCreationException(omniNamesManaged
					.getManagedSoftwareDescription().getId()
					+ " not plugged on physical resource");
		}

		ObjectFactory factory = new ObjectFactory();
		Options opts = factory.createOptions();
		
		Option nameService = factory.createOptionsOption();
		nameService.setKey("InitRef");
		nameService.setValue("NameService=corbaname::"+plugged.getSsh().getServer()+":" + ((OmniNames)omniNamesManaged.getManagedSoftwareDescription()).getPort());
		Option supportBootstrapAgent = factory.createOptionsOption();
		supportBootstrapAgent.setKey("supportBootstrapAgent");
		supportBootstrapAgent.setValue("1");
		opts.getOption().add(nameService);
		opts.getOption().add(supportBootstrapAgent);
		omniNamesManaged.getManagedSoftwareDescription().setCfgOptions(opts);

	}
}
