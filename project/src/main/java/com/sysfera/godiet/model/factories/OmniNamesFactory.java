package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;

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
		settingOmniNamesRunningCommand(omniNamesManaged);
		return omniNamesManaged;
	}

	/**
	 * Build the omniNames running command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg + nohup
	 * {OmniNamesBinary} + -start -always > {scratch_runtime}/OmniNames.out 2>
	 * {scratch_runtime}/OmniNames.err &
	 * 
	 * @param softManaged
	 * @return
	 */
	private void settingOmniNamesRunningCommand(SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
		Software softwareDescription = softManaged.getSoftwareDescription();
		command += "OMNINAMES_LOGDIR=" + scratchDir + "/";
		command += " ";

		command += "OMNIORB_CONFIG=" + scratchDir + "/"
				+ softwareDescription.getId() + ".cfg";
		command += " nohup ";
		command += softwareDescription.getConfig().getRemoteBinary();

		command += " ";
		command += "-start -always >";
		command += scratchDir + "/OmniNames.out 2> ";
		command += scratchDir + "/OmniNames.err &";
		softManaged.setRunningCommand(command);
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
		Resource plugged = omniNamesManaged.getPluggedOn();
		if (plugged == null) {
			throw new DietResourceCreationException(omniNamesManaged
					.getSoftwareDescription().getId()
					+ " not plugged on physical resource");
		}

		ObjectFactory factory = new ObjectFactory();
		Options opts = factory.createOptions();

		Option nameService = factory.createOptionsOption();
		nameService.setKey("InitRef");
		nameService.setValue("NameService=corbaname::"
				+ plugged.getSsh().getServer()
				+ ":"
				+ ((OmniNames) omniNamesManaged.getSoftwareDescription())
						.getPort());
		Option supportBootstrapAgent = factory.createOptionsOption();
		supportBootstrapAgent.setKey("supportBootstrapAgent");
		supportBootstrapAgent.setValue("1");
		opts.getOption().add(nameService);
		opts.getOption().add(supportBootstrapAgent);
		omniNamesManaged.getSoftwareDescription().setCfgOptions(opts);

	}
}
