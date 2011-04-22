package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.command.start.StartServicesCommand;
import com.sysfera.godiet.command.stop.StopServicesCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class StopServicesCommandTest {

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
				xmlLoadingCommand.setRemoteAccess(remoteAccess);

				xmlLoadingCommand.execute();

			}
		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testLaunchBeforePrepare() {
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);

		Exception commandExecException = null;

		try {
			launchServicesCommand.execute();
		} catch (CommandExecutionException e) {
			commandExecException = e;
		}
		// ass0et the exception object
		Assert.assertNotNull("No expected exception", commandExecException);
	}

	@Test
	public void testLaunch() {
		PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
		prepareCommand.setRm(rm);
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);
		try {
			prepareCommand.execute();
			launchServicesCommand.execute();
			stopServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
