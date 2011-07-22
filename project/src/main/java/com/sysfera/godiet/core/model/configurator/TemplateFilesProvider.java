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
 * and {JAR}/configuration/templates directory.
 * Use Freemarker implementation
 * @author phi
 *
 */
@Service
public class TemplateFilesProvider {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final MultiTemplateLoader multiTemplateLoader;
	
	public TemplateFilesProvider() {
		List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
		String homeTemplatesDirPath = System.getProperty("user.home")+ "/.godiet/templates/";
		File homeTemplatesDirnew = new File(homeTemplatesDirPath);
		try {
			loaders.add(new FileTemplateLoader(homeTemplatesDirnew,true));
		} catch (IOException e) {
			log.warn("Unable to load template from user home " + e.getMessage());
		}
		loaders.add(new ClassTemplateLoader(getClass(),"/configuration/templates"));
		this.multiTemplateLoader = new MultiTemplateLoader(loaders.toArray(new TemplateLoader[0]));
		
	}
	


	public TemplateLoader getTemplateLoader() {
		return multiTemplateLoader;
	}
	
}
