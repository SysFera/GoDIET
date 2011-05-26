package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietAbstractFactory;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandLoadXMLImplTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	RemoteAccess remoteAccess = new RemoteAccessMock();
	ResourcesManager rm;

	@Before
	public void initGodietConfig() {
		rm = new ResourcesManager();

		// Loading configuration
		{
			String configurationFile = "configuration/configuration.xml";

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);
			try {
				XMLLoadingHelper.initConfig(rm, inputStream);
			} catch (CommandExecutionException e) {
				Assert.fail(e.getCause().getMessage());
			}
		}

	}

	@Test
	public void testCommand() {
		List<String> testCaseFiles = Arrays.asList(new String[] {
				//"diet/2MA-1LA-6SED.xml", 
				"diet/1MA-3LA-10SED.xml",
				//"diet/1MA-3SED.xml"
				});
		String infraCaseFiles = "infrastructure/3D-5N-3G-3L.xml";
	
		XMLParser scanner = new XmlScannerJaxbImpl();

		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		SoftwareController softwareController = new RemoteConfigurationHelper(remoteAccess, rm.getGodietConfiguration().getGoDietConfiguration(), rm.getInfrastructureModel());
		DietManager dietModel = rm.getDietModel();
		GodietAbstractFactory godietAbstractFactory = new GodietAbstractFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietModel),
				new MasterAgentRuntimeValidatorImpl(dietModel),
				new LocalAgentRuntimeValidatorImpl(dietModel),
				new SedRuntimeValidatorImpl(dietModel),
				new OmniNamesRuntimeValidatorImpl(dietModel));

		xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);

		for (String testCaseFile : testCaseFiles) {
			// Retry with the same config
			GoDietConfiguration config = rm.getGodietConfiguration()
					.getGoDietConfiguration();
			// A Faire : Découper tous les fichiers en 2. Puis faire en sorte de
			// refaire les tests (Hashmap ?)
			rm = new ResourcesManager();
			rm.setGoDietConfiguration(config);
			// Load platform
			InputStream infrastructureInputStream = getClass().getClassLoader()
					.getResourceAsStream(infraCaseFiles);

			try {
				XMLLoadingHelper.initInfrastructure(rm, infrastructureInputStream);

				xmlLoadingCommand.setRm(rm);

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				if (inputStream == null)
					Assert.fail("Unable to find " + testCaseFile);
				xmlLoadingCommand.setXmlInput(inputStream);
				xmlLoadingCommand.execute();
			} catch (CommandExecutionException e) {
				log.error("Test fail for file: " + testCaseFile, e);
				Assert.fail();

			}
		}

	}

	@Test
	public void testCountDietElement1() {

		try {
			{
				String platformTestCase = "infrastructure/3D-5N-3G-3L.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
			}

			String testCaseFile = "diet/1MA-3LA-10SED.xml";
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(testCaseFile);

			XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
			LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
			xmlLoadingCommand.setRm(rm);
			xmlLoadingCommand.setXmlInput(inputStream);
			xmlLoadingCommand.setXmlParser(scanner);
			SoftwareController softwareController = new RemoteConfigurationHelper(remoteAccess, rm.getGodietConfiguration().getGoDietConfiguration(), rm.getInfrastructureModel());
			DietManager dietModel = rm.getDietModel();
		GodietAbstractFactory godietAbstractFactory = new GodietAbstractFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietModel),
				new MasterAgentRuntimeValidatorImpl(dietModel),
				new LocalAgentRuntimeValidatorImpl(dietModel),
				new SedRuntimeValidatorImpl(dietModel),
				new OmniNamesRuntimeValidatorImpl(dietModel));

			xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);


			xmlLoadingCommand.execute();
			InfrastructureManager platform = rm.getInfrastructureModel();
			if (platform.getClusters().size() != 0)
				Assert.fail();
			if (platform.getDomains().size() != 4)
				Assert.fail();
			if (platform.getFrontends().size() != 0)
				Assert.fail();
		
			if (platform.getLinks().size() != 0)
				Assert.fail();
			if (platform.getNodes().size() != 5)
				Assert.fail(platform.getNodes().size() + " != 5");

			DietManager diet = rm.getDietModel();
			if (diet.getMasterAgents().size() != 1)
				Assert.fail();
			if (diet.getLocalAgents().size() != 3)
				Assert.fail();
			if (diet.getSeds().size() != 10)
				Assert.fail();

			// check if all Diet are pluged on physical resources
			for (DietResourceManaged ma : diet.getMasterAgents()) {
				if (ma.getPluggedOn() == null)
					Assert.fail();
			}
			for (DietResourceManaged la : diet.getLocalAgents()) {
				if (la.getPluggedOn() == null)
					Assert.fail();
			}

			for (DietResourceManaged sed : diet.getSeds()) {
				if (sed.getPluggedOn() == null)
					Assert.fail();
			}

		} catch (CommandExecutionException e) {
			log.error("Test testCountDietElement1 Fail", e);
			Assert.fail();
		}

	}

	@Test
	public void testCountDietElement2() {

		try {
			{
				String platformTestCase = "infrastructure/3D-5N-3G-3L.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
			}

			String testCaseFile = "diet/2MA-1LA-6SED.xml";
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(testCaseFile);
			XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
			LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
			xmlLoadingCommand.setRm(rm);
			xmlLoadingCommand.setXmlInput(inputStream);
			xmlLoadingCommand.setXmlParser(scanner);
			SoftwareController softwareController = new RemoteConfigurationHelper(remoteAccess, rm.getGodietConfiguration().getGoDietConfiguration(), rm.getInfrastructureModel());
			DietManager dietModel = rm.getDietModel();
		GodietAbstractFactory godietAbstractFactory = new GodietAbstractFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietModel),
				new MasterAgentRuntimeValidatorImpl(dietModel),
				new LocalAgentRuntimeValidatorImpl(dietModel),
				new SedRuntimeValidatorImpl(dietModel),
				new OmniNamesRuntimeValidatorImpl(dietModel));

			xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);


			xmlLoadingCommand.execute();
			InfrastructureManager platform = rm.getInfrastructureModel();
			if (platform.getClusters().size() != 0)
				Assert.fail();
			if (platform.getDomains().size() != 4)
				Assert.fail();
			if (platform.getFrontends().size() != 0)
				Assert.fail();
		
			if (platform.getLinks().size() != 0)
				Assert.fail();
			if (platform.getNodes().size() != 5)
				Assert.fail(platform.getNodes().size() + " != 3");

			DietManager diet = rm.getDietModel();
			if (diet.getMasterAgents().size() != 2)
				Assert.fail();
			if (diet.getLocalAgents().size() != 1)
				Assert.fail();
			if (diet.getSeds().size() != 6)
				Assert.fail();

			// check if all Diet are pluged on physical resources
			for (DietResourceManaged ma : diet.getMasterAgents()) {
				if (ma.getPluggedOn() == null)
					Assert.fail();
			}
			for (DietResourceManaged la : diet.getLocalAgents()) {
				if (la.getPluggedOn() == null)
					Assert.fail();
			}

			for (DietResourceManaged sed : diet.getSeds()) {
				if (sed.getPluggedOn() == null)
					Assert.fail();
			}

		} catch (CommandExecutionException e) {
			log.error("Test testCountDietElement2 Fail", e);
			Assert.fail();
		}

	}
}
