package com.sysfera.godiet.model.factories;

import java.util.List;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Env;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Var;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.utils.ResourceUtil;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed OmniNames factory
 * 
 * @author phi
 * 
 */
public class OmniNamesFactory {

	private final SoftwareController softwareController;
	private final RuntimeValidator validator;

	public OmniNamesFactory(SoftwareController softwareController, RuntimeValidator omniNamesValidator) {
		this.softwareController = softwareController;
		this.validator = omniNamesValidator;
	}

	/**
	 * Create a managed omninames given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * @param omniNamesDescription
	 * @return The managed omniNames
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	public DietServiceManaged create(OmniNames omniNamesDescription)
			throws DietResourceCreationException {
		DietServiceManaged omniNamesManaged = new DietServiceManaged(softwareController,validator);

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
	 * 
	 */
	private void settingOmniNamesRunningCommand(SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
		//Add all environment node
		Env env = softManaged.getPluggedOn().getEnv();
		if(env != null) {
			List<Var> vars = env.getVar();
			if(vars != null)
			{
				for (Var var : vars) {
					command+= " " + var.getName() +"=" +var.getValue()+" "; 
				}
			}
		}
		
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
	private void settingConfigurationOptions(DietServiceManaged omniNamesManaged)
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
