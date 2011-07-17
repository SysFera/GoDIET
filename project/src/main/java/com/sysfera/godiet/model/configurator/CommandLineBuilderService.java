package com.sysfera.godiet.model.configurator;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sysfera.godiet.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.model.generated.CommandLine;
import com.sysfera.godiet.model.generated.CommandLine.Parameter;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.SoftwareManager;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class CommandLineBuilderService {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final Configuration freemarkerConfiguration;

	public CommandLineBuilderService() {
		this.freemarkerConfiguration = new Configuration();
		this.freemarkerConfiguration
				.setObjectWrapper(new DefaultObjectWrapper());
		this.freemarkerConfiguration.setNumberFormat("0.######");
	}

	/**
	 * 
	 * @param managedSoftware
	 * @return
	 * @throws ConfigurationBuildingException
	 *             if template not found.
	 */
	public void build(SoftwareManager<? extends Software> managedSoftware)
			throws ConfigurationBuildingException {
		CommandLine commandLine = managedSoftware.getSoftwareDescription()
				.getBinary().getCommandLine();
		if (commandLine == null) {
			log.debug("No configuration file to create for "
					+ managedSoftware.getSoftwareDescription().getId());
			return;
		}
		// build templated command line
		String templatedCommandLineString = managedSoftware
				.getSoftwareDescription().getBinary().getName()
				+ " ";
		for (Parameter param : commandLine.getParameter()) {
			templatedCommandLineString += param.getString() + " ";
		}

		// build final command line ( templated command line + templatemodel);
		// build configuration file
		StringWriter command = new StringWriter();
		// Build the model associated to the software
		Map<Object, Object> templateModel = TemplateModel
				.buildModel(managedSoftware);

		try {
			Template template = new Template("cmdline", new StringReader(
					templatedCommandLineString), freemarkerConfiguration);
			template.process(templateModel, command);
			managedSoftware.setRunningCommand(command.toString());
		} catch (TemplateException e) {
			throw new ConfigurationBuildingException(
					"Error during building command line. Software: "
							+ managedSoftware.getSoftwareDescription().getId(),
					e);
		} catch (IOException e) {
			throw new ConfigurationBuildingException(
					"Error during building command line. Software: "
							+ managedSoftware.getSoftwareDescription().getId(),
					e);
		}

	}
}
