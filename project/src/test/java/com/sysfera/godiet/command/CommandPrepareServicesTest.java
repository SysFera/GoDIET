package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietAbstractFactory;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandPrepareServicesTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	RemoteAccess remoteAccess = new RemoteAccessMock();

	@Before
	public void init() {
		rm = new ResourcesManager();
		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				XMLLoadingHelper.initConfig(rm, inputStream);
			}
			{
				String platformTestCase = "platform/testbed-platform.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initPlatform(rm, inputStreamPlatform);
			}
			{
				// Init RM
				String testCaseFile = "diet/testbed-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
				LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
				xmlLoadingCommand.setRm(rm);
				xmlLoadingCommand.setXmlInput(inputStream);
				xmlLoadingCommand.setXmlParser(scanner);
				SoftwareController softwareController = new RemoteConfigurationHelper(remoteAccess, rm.getGodietConfiguration().getGoDietConfiguration(), rm.getPlatformModel());
				GodietAbstractFactory godietAbstractFactory = new GodietAbstractFactory(softwareController);
				xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);


				xmlLoadingCommand.execute();

			}
		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testPrepareService() {
		PrepareServicesCommand prepareServicesCommand = new PrepareServicesCommand();
		prepareServicesCommand.setRm(rm);
		try {
			prepareServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
