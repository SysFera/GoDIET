package com.sysfera.godiet.core.model.factories;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Binary;
import com.sysfera.godiet.common.model.generated.CommandLine;
import com.sysfera.godiet.common.model.generated.CommandLine.Parameter;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.ObjectFactory;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.SoftwareFile;
import com.sysfera.godiet.common.model.generated.SoftwareFile.Template;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * Managed MA factory
 * 
 * @author phi
 * 
 */
public class MasterAgentFactory {
	private static final String DEFAULT_BINARYNAME = "dietAgent";

	private final SoftwareController softwareController;
	private final ConfigurationFileBuilderService configurationFileBuilderService;

	private final RuntimeValidator<DietResourceManaged<MasterAgent>> validator;

	public MasterAgentFactory(SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<MasterAgent>> maValidator,
			ConfigurationFileBuilderService configurationFileBuilderService) {
		this.softwareController = softwareController;
		this.validator = maValidator;
		this.configurationFileBuilderService = configurationFileBuilderService;

	}

	/**
	 * Create a managed MasterAgent given his description. Check validity. Set
	 * the default option if needed (like command launch).
	 * 
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException
	 * @throws IncubateException
	 */
	public DietResourceManaged<MasterAgent> create(
			MasterAgent masterAgentDescription, Resource pluggedOn,
			OmniNamesManaged omniNames) throws DietResourceCreationException,
			IncubateException {

		DietResourceManaged<MasterAgent> masterAgentManaged = new DietResourceManaged<MasterAgent>(
				masterAgentDescription, pluggedOn, softwareController,
				validator, omniNames);

		// TODO:something better with optional overloading ...
		// Add default configuration file
		try {

			SoftwareFile sf = new ObjectFactory().createSoftwareFile();
			Template template = new ObjectFactory()
					.createSoftwareFileTemplate();
			template.setName("ma_template.config");
			sf.setId(masterAgentManaged.getSoftwareDescription().getId());
			sf.setTemplate(template);
			masterAgentManaged.getSoftwareDescription().getFile().add(sf);

			configurationFileBuilderService.build(masterAgentManaged);
			//Add a ref to the omniNames's config file
			masterAgentManaged.getConfigurationFiles().putAll(omniNames.getConfigurationFiles());

		} catch (ConfigurationBuildingException e) {
			new IncubateException("Unable to create configurations file ", e);
		}
		// Add default commandLine parameter (configuration file)
		Binary b = new ObjectFactory().createBinary();
		CommandLine commandLine = new ObjectFactory().createCommandLine();
		Parameter paramConfigurationFile = new ObjectFactory()
				.createCommandLineParameter();
		paramConfigurationFile.setString(masterAgentManaged.getConfigurationFiles().get(masterAgentDescription.getId()).getAbsolutePath());
		commandLine.getParameter().add(paramConfigurationFile);
		b.setCommandLine(commandLine);
		b.setName(DEFAULT_BINARYNAME);

		masterAgentDescription.setBinary(b);
		AgentFactoryUtil.settingRunningCommand(
				omniNames.getSoftwareDescription(), masterAgentManaged);
		return masterAgentManaged;
	}

}
