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
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietAbstractFactory;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandLaunchServicesTest {

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
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
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
				SoftwareController softwareController = new RemoteConfigurationHelper(
						remoteAccess, rm.getGodietConfiguration()
								.getGoDietConfiguration(),
						rm.getInfrastructureModel());
				DietManager dietModel = rm.getDietModel();
				GodietAbstractFactory godietAbstractFactory = new GodietAbstractFactory(
						softwareController, new ForwarderRuntimeValidatorImpl(
								dietModel),
						new MasterAgentRuntimeValidatorImpl(dietModel),
						new LocalAgentRuntimeValidatorImpl(dietModel),
						new SedRuntimeValidatorImpl(dietModel),
						new OmniNamesRuntimeValidatorImpl(dietModel));

				xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);

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
		try {
			prepareCommand.execute();
			launchServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
