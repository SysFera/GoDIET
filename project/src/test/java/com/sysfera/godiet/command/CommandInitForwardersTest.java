package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietAbstractFactory;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml","/spring/ssh-context.xml" })
public class CommandInitForwardersTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ResourcesManager rm;
	LoadXMLDietCommand xmlLoadingCommand;
	
	GodietAbstractFactory godietAbstractFactory;

	@Before
	public void initRM() {

		String configurationFile = "configuration/configuration.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(configurationFile);
		try {
			XMLLoadingHelper.initConfig(rm, inputStream);
		} catch (CommandExecutionException e) {
			Assert.fail();

		}
		SoftwareController softwareController = new RemoteConfigurationHelper(
				rm.getGodietConfiguration()
						.getGoDietConfiguration(), rm.getPlatformModel());
		DietManager dietModel = rm.getDietModel();
		godietAbstractFactory = new GodietAbstractFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietModel),
				new MasterAgentRuntimeValidatorImpl(dietModel),
				new LocalAgentRuntimeValidatorImpl(dietModel),
				new SedRuntimeValidatorImpl(dietModel),
				new OmniNamesRuntimeValidatorImpl(dietModel));

		xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlParser(new XmlScannerJaxbImpl());
		xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);

	}

	@After
	public void after() {
		this.rm.getPlatformModel().destroy();
	}

	@Test
	@DirtiesContext
	public void testCommandInitForwarder1() {

		String platformTestCase = "infrastructure/3D-5N-3G-3L.xml";
		InputStream inputStreamPlatform = getClass().getClassLoader()
				.getResourceAsStream(platformTestCase);
		try {
			XMLLoadingHelper.initPlatform(rm, inputStreamPlatform);

			String testCaseFile = "diet/1MA-3SED.xml";
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(testCaseFile);

			xmlLoadingCommand.setXmlInput(inputStream);

			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
		}

		InitForwardersCommand initForwardersInit = new InitForwardersCommand();
		initForwardersInit.setRm(rm);
		initForwardersInit.setForwarderFactory(godietAbstractFactory);
		try {
			initForwardersInit.execute();
			List<DietResourceManaged> forwarders = rm.getDietModel()
					.getForwarders();
			if (forwarders.size() != 6)
				Assert.fail();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Test
	@DirtiesContext
	public void testCommandInitForwarder2() {

		try {
			String platformTestCase = "infrastructure/testbed-platform.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			XMLLoadingHelper.initPlatform(rm, inputStreamPlatform);

			String testCaseFile = "diet/testbed-diet.xml";
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(testCaseFile);

			xmlLoadingCommand.setXmlInput(inputStream);

			xmlLoadingCommand.execute();

			InitForwardersCommand initForwardersInit = new InitForwardersCommand();
			initForwardersInit.setRm(rm);
			initForwardersInit.setForwarderFactory(godietAbstractFactory);

			try {
				initForwardersInit.execute();
				List<DietResourceManaged> forwarders = rm.getDietModel()
						.getForwarders();
				if (forwarders.size() != 6)
					Assert.fail();
			} catch (CommandExecutionException e) {
				Assert.fail(e.getMessage());
			}
		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}

	}

}
