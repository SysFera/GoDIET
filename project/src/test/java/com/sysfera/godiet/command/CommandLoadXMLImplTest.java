package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.PlatformManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
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
			InitUtil.initConfig(rm, inputStream);
		}

	}

	@Test
	public void testCommand() {
		List<String> testCaseFiles = Arrays.asList(new String[] {
				"diet/2MA-1LA-6SED.xml", "diet/1MA-3LA-10SED.xml",
				"diet/1MA-3SED.xml", });
		String platfomCaseFiles = "platform/3D-5N-3G-3L.xml";

		XMLParser scanner = new XmlScannerJaxbImpl();

		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);
		for (String testCaseFile : testCaseFiles) {
			// Retry with the same config
			GoDietConfiguration config = rm.getGodietConfiguration()
					.getGoDietConfiguration();
			// A Faire : Découper tous les fichiers en 2. Puis faire en sorte de
			// refaire les tests (Hashmap ?)
			rm = new ResourcesManager();
			rm.setGoDietConfiguration(config);
			// Load platform
			InputStream platformInputStream = getClass().getClassLoader()
					.getResourceAsStream(platfomCaseFiles);
			InitUtil.initPlatform(rm, platformInputStream);

			xmlLoadingCommand.setRm(rm);

			try {
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
		{
			String platformTestCase = "platform/3D-5N-3G-3L.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			InitUtil.initPlatform(rm, inputStreamPlatform);
		}

		String testCaseFile = "diet/1MA-3LA-10SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);

		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

		try {
			xmlLoadingCommand.execute();
			PlatformManager platform = rm.getPlatformModel();
			if (platform.getClusters().size() != 0)
				Assert.fail();
			if (platform.getDomains().size() != 3)
				Assert.fail();
			if (platform.getFrontends().size() != 0)
				Assert.fail();
			if (platform.getGateways().size() != 3)
				Assert.fail();
			if (platform.getLinks().size() != 3)
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
		{
			String platformTestCase = "platform/3D-5N-3G-3L.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			InitUtil.initPlatform(rm, inputStreamPlatform);
		}

		String testCaseFile = "diet/2MA-1LA-6SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

		try {
			xmlLoadingCommand.execute();
			PlatformManager platform = rm.getPlatformModel();
			if (platform.getClusters().size() != 0)
				Assert.fail();
			if (platform.getDomains().size() != 3)
				Assert.fail();
			if (platform.getFrontends().size() != 0)
				Assert.fail();
			if (platform.getGateways().size() != 3)
				Assert.fail();
			if (platform.getLinks().size() != 3)
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
