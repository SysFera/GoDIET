package com.sysfera.godiet.core;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import scala.reflect.generic.Trees.This;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.utils.StringUtils;

public class GodietServerMain {
	

	

	public static void main(String[] args) {
		 Logger log = LoggerFactory.getLogger("BLA");
		String[] springFiles = { "/spring/spring-config.xml",
				"/spring/ssh-context.xml", "/spring/godiet-service.xml" };
		log.info("Info");
		log.info("Info");
		AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext(
				springFiles);


	}
}
