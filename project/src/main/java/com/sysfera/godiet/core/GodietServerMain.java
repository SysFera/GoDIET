package com.sysfera.godiet.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
