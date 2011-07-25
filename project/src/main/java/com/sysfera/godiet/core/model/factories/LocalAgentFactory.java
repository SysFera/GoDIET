package com.sysfera.godiet.core.model.factories;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Binary;
import com.sysfera.godiet.common.model.generated.CommandLine;
import com.sysfera.godiet.common.model.generated.CommandLine.Parameter;
import com.sysfera.godiet.common.model.generated.LocalAgent;
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
 * Managed LA factory
 * 
 * @author phi
 * 
 */
public class LocalAgentFactory {
	private static final String DEFAULT_BINARYNAME = "dietAgent";

	private final SoftwareController softwareController;
	private final RuntimeValidator<DietResourceManaged<LocalAgent>> validator;
	private final ConfigurationFileBuilderService configurationFileBuilderService;

	public LocalAgentFactory(SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<LocalAgent>> laValidator,
			ConfigurationFileBuilderService configurationFileBuilderService) {

		this.softwareController = softwareController;
		this.validator = laValidator;
		this.configurationFileBuilderService = configurationFileBuilderService;

	}

	/**
	 * Create a managed LocalAgent given his description. Check validity. Set
	 * the default option if needed.
	 * 
	 * @param localAgentDescription
	 * @return The managed LocalAgent
	 * @throws IncubateException
	 * @throws DietResourceCreationException
	 */
	public DietResourceManaged<LocalAgent> create(
			LocalAgent localAgentDescription, Resource pluggedOn,
			OmniNamesManaged omniNames) throws IncubateException,
			DietResourceCreationException {

		DietResourceManaged<LocalAgent> localAgentManaged = new DietResourceManaged<LocalAgent>(
				localAgentDescription, pluggedOn, softwareController,
				validator, omniNames);

		// TODO:something better with optional overloading ...
		try {

			SoftwareFile sf = new ObjectFactory().createSoftwareFile();
			Template template = new ObjectFactory()
					.createSoftwareFileTemplate();
			template.setName("la_template.config");
			sf.setId(localAgentManaged.getSoftwareDescription().getId());
			sf.setTemplate(template);
			localAgentManaged.getSoftwareDescription().getFile().add(sf);
			

			configurationFileBuilderService.build(localAgentManaged);
			
			//Add a ref to the omniNames's config file
			localAgentManaged.getConfigurationFiles().putAll(omniNames.getConfigurationFiles());


		} catch (ConfigurationBuildingException e) {
			new IncubateException("Unable to create configurations file ", e);
		}
		// Add default commandLine parameter (configuration file)
		Binary b = new ObjectFactory().createBinary();
		CommandLine commandLine = new ObjectFactory().createCommandLine();
		Parameter paramConfigurationFile = new ObjectFactory()
				.createCommandLineParameter();
		paramConfigurationFile.setString(localAgentManaged.getConfigurationFiles().get(localAgentDescription.getId()).getAbsolutePath());
		commandLine.getParameter().add(paramConfigurationFile);
		b.setCommandLine(commandLine);
		b.setName(DEFAULT_BINARYNAME);

		localAgentDescription.setBinary(b);
		AgentFactoryUtil.settingRunningCommand(
				omniNames.getSoftwareDescription(), localAgentManaged);

		return localAgentManaged;
	}

}
