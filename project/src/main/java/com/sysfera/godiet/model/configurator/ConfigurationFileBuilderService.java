package com.sysfera.godiet.model.configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sysfera.godiet.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.SoftwareFile;
import com.sysfera.godiet.model.softwares.SoftwareManager;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ConfigurationFileBuilderService {
	private Logger log = LoggerFactory.getLogger(getClass());
	private final Configuration freemarkerConfiguration;

	@Autowired
	private ConfigurationManager configurationManager;

	@Autowired
	private TemplateFilesProvider templateManager;

	public ConfigurationFileBuilderService() {
		this.freemarkerConfiguration = new Configuration();

		this.freemarkerConfiguration
				.setObjectWrapper(new DefaultObjectWrapper());
		this.freemarkerConfiguration.setNumberFormat("0.######");
	}

	@PostConstruct
	public void postConstruct() {
		this.freemarkerConfiguration.setTemplateLoader(templateManager
				.getTemplateLoader());
	}

	/**
	 * Scan all declared file in software description. For each file, create a
	 * configuration file. This files could be templated or just a declared
	 * (copy).
	 * 
	 * A Map of configuration files are created and set to managedSoftware
	 * 
	 * @param managedSoftware
	 * @return
	 * @throws ConfigurationBuildingException
	 *             if template not found.
	 */
	public void build(SoftwareManager<? extends Software> managedSoftware)
			throws ConfigurationBuildingException {

		// Get all configurations files
		List<SoftwareFile> templatedConfigurationFiles = managedSoftware
				.getSoftwareDescription().getFile();
		if (templatedConfigurationFiles == null) {
			log.debug("No configuration file to create for "
					+ managedSoftware.getSoftwareDescription().getId());
			return;
		}

		Map<String, ConfigurationFile> configurationFiles = new HashMap<String, ConfigurationFile>();
		managedSoftware.setConfigurationFiles(configurationFiles);
		for (SoftwareFile softwareFile : templatedConfigurationFiles) {
			if (softwareFile.getTemplate() != null) {
				ConfigurationFile configurationFile = buildTemplatedFile(
						managedSoftware, softwareFile);
				configurationFiles.put(softwareFile.getId(), configurationFile);
			} else if (softwareFile.getCopy() != null) {
				ConfigurationFile configurationFile = buildCopyFile(
						managedSoftware, softwareFile);
				configurationFiles.put(softwareFile.getId(), configurationFile);
			} else {
				log.error("Software file is not correctly describe. This is neither a copy nor a templated file. File id : "
						+ softwareFile.getId());
			}
		}

	}

	/**
	 * 
	 /** Create a templated file using freemarker
	 * 
	 * @param managedSoftware
	 * @param softwareFile
	 * @return
	 * @throws ConfigurationBuildingException
	 */
	private ConfigurationFile buildTemplatedFile(
			SoftwareManager<? extends Software> managedSoftware,
			SoftwareFile softwareFile) throws ConfigurationBuildingException {
		ConfigurationFile configurationFile = new ConfigurationFile();
		configurationFile.setId(softwareFile.getId());
		try {
			// Search the template in templateManager
			Template template = this.freemarkerConfiguration
					.getTemplate(softwareFile.getTemplate().getName());
			if (template == null) {
				throw new ConfigurationBuildingException(
						"Unable to find template " + softwareFile.getTemplate()
								+ ".");
			}
			// Build the model associated to the software
			Map<Object, Object> templateModel = TemplateModel
					.buildModel(managedSoftware);

			// build configuration file
			StringWriter contentsWriter = new StringWriter();
			try {
				template.process(templateModel, contentsWriter);
			} catch (TemplateException e) {
				throw new ConfigurationBuildingException(
						"Error during building configuration file procession. File : "
								+ softwareFile.getId() + ". Template : "
								+ softwareFile.getTemplate().getName(), e);
			}

			configurationFile.setContents(contentsWriter.toString());

			// set the path file. Relative to the plugged node scratch dir
			String absolutePath = managedSoftware.getPluggedOn().getScratch()
					.getDir()
					+ "/" + softwareFile.getId() + ".cfg";
			configurationFile.setAbsolutePath(absolutePath);
			return configurationFile;
		} catch (IOException e) {
			log.debug("Unable to read template : ", e);
			throw new ConfigurationBuildingException("Unable to read template "
					+ softwareFile.getTemplate().getName() + ".");
		}

	}

	/**
	 * TODO: Get files from configuration manager TODO: Use URL not unix
	 * notation Search the file in scratch directory
	 * 
	 * @param managedSoftware
	 * @param softwareFile
	 * @return
	 * @throws ConfigurationBuildingException
	 */
	private ConfigurationFile buildCopyFile(
			SoftwareManager<? extends Software> managedSoftware,
			SoftwareFile softwareFile) throws ConfigurationBuildingException {
		ConfigurationFile configurationFile = new ConfigurationFile();
		configurationFile.setId(softwareFile.getId());

		// Read and copy contents
		// USE URL HERE
		String path = configurationManager.getLocalScratch().getDir() + "/"
				+ softwareFile.getCopy().getName();
		try {
			FileInputStream stream = new FileInputStream(new File(path));
			try {
				FileChannel fc = stream.getChannel();
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
						fc.size());
				String result = Charset.defaultCharset().decode(bb).toString();
				configurationFile.setContents(result);
			} finally {
				stream.close();
			}

		} catch (IOException e) {
			log.error("Unable to read file " + path);
			throw new ConfigurationBuildingException(
					"Unable to read 'copy' file "
							+ softwareFile.getCopy().getName() + ". Path : "
							+ path);
		}

		// set the path file. Relative to the plugged node scratch dir
		String absolutePath = managedSoftware.getPluggedOn().getScratch()
				.getDir()
				+ "/" + softwareFile.getCopy().getName();
		configurationFile.setAbsolutePath(absolutePath);
		return configurationFile;
	}

}
