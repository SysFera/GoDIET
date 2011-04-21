package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandInitForwardersTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	LoadXMLDietCommand xmlLoadingCommand;
	RemoteAccess remoteAccess = new RemoteAccessMock();

	@Before
	public void initRM() {
		
		rm = new ResourcesManager();
		
		String configurationFile = "configuration/configuration.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(configurationFile);
		InitUtil.initConfig(rm, inputStream);

		xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlParser(new XmlScannerJaxbImpl());
		xmlLoadingCommand.setRemoteAccess(remoteAccess);
		
	}

	@Test
	public void testCommandInitForwarder1() {
		
		String platformTestCase ="platform/3D-5N-3G-3L.xml";
		InputStream inputStreamPlatform  = getClass().getClassLoader()
		.getResourceAsStream(platformTestCase);
		InitUtil.initPlatform(rm, inputStreamPlatform);
		
		String testCaseFile = "diet/1MA-3SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);

		xmlLoadingCommand.setXmlInput(inputStream);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}
		InitForwardersCommand initForwardersInit = new InitForwardersCommand();
		initForwardersInit.setRm(rm);
		initForwardersInit.setRemoteAccess(remoteAccess);
		try {
			initForwardersInit.execute();
			List<DietResourceManaged> forwarders = rm.getDietModel()
					.getForwarders();
			if (forwarders.size() != 6)
				Assert.fail();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void testCommandInitForwarder2() {
		
		
		String platformTestCase ="platform/testbed-platform.xml";
		InputStream inputStreamPlatform  = getClass().getClassLoader()
		.getResourceAsStream(platformTestCase);
		InitUtil.initPlatform(rm, inputStreamPlatform);
		
		String testCaseFile = "diet/testbed-diet.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);

		xmlLoadingCommand.setXmlInput(inputStream);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}
		InitForwardersCommand initForwardersInit = new InitForwardersCommand();
		initForwardersInit.setRm(rm);
		initForwardersInit.setRemoteAccess(remoteAccess);

		try {
			initForwardersInit.execute();
			List<DietResourceManaged> forwarders = rm.getDietModel()
					.getForwarders();
			if (forwarders.size() != 6)
				Assert.fail();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());
		}

	}

}
