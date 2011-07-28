package com.sysfera.godiet.core.model.configurator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

/**
 * Manage and provide the configuration files found in {HOME}/.godiet/templates
 * and {JAR}/configuration/templates directory. Use Freemarker implementation
 * 
 * @author phi
 * 
 */
@Service
public class TemplateFilesProvider {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static String TEMPLATE_DIRECTORY_PATH = System
			.getenv("TEMPLATE_DIRECTORY_PATH");
	private final MultiTemplateLoader multiTemplateLoader;

	public TemplateFilesProvider() {
		List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
		// Load from TEMPLATE_DIRECTORY_PATH
		if (TEMPLATE_DIRECTORY_PATH != null) {
			File homeTemplatesDirnew = new File(TEMPLATE_DIRECTORY_PATH);
			try {
				loaders.add(new FileTemplateLoader(homeTemplatesDirnew, true));
			} catch (IOException e) {
				log.warn("Unable to load template from directory. Perhaps  TEMPLATE_DIRECTORY_PATH env var not initialized correctly ? Currently is "+ TEMPLATE_DIRECTORY_PATH);
				log.debug(e.getMessage());
			}
		}else{
			log.warn("TEMPLATE_DIRECTORY_PATH not set. Only take the defaults template");
		}
		// Load defaults templates from classpath
		loaders.add(new ClassTemplateLoader(getClass(),
				"/configuration/templates"));
		this.multiTemplateLoader = new MultiTemplateLoader(
				loaders.toArray(new TemplateLoader[0]));

	}

	public TemplateLoader getTemplateLoader() {
		return multiTemplateLoader;
	}

}
