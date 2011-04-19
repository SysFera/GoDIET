package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandLoadXMLImplTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	RemoteAccess remoteAccess = new RemoteAccessMock();

	@Test
	public void testCommand() {
		List<String> testCaseFiles = Arrays.asList(new String[] {
				"exampleMultiDomainsNG.xml", "3D-5N-3G-3L-2MA-1LA-6SED.xml",
				 "1D-3N-1MA-3LA-10SED.xml", "3D-5N-3G-3L-1MA-3SED.xml",
				 "testbed.xml" });

		XMLParser scanner = new XmlScannerJaxbImpl();

		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);
		for (String testCaseFile : testCaseFiles) {
			ResourcesManager rm = new ResourcesManager();
			xmlLoadingCommand.setRm(rm);

			try {
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
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
		String testCaseFile = "1D-3N-1MA-3LA-10SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

		try {
			xmlLoadingCommand.execute();
			Platform platform = rm.getPlatformModel();
			if (platform.getClusters().size() != 0)
				Assert.fail();
			if (platform.getDomains().size() != 1)
				Assert.fail();
			if (platform.getFrontends().size() != 0)
				Assert.fail();
			if (platform.getGateways().size() != 0)
				Assert.fail();
			if (platform.getLinks().size() != 0)
				Assert.fail();
			if (platform.getNodes().size() != 3)
				Assert.fail(platform.getNodes().size() + " != 3");

			Diet diet = rm.getDietModel();
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
		String testCaseFile = "3D-5N-3G-3L-2MA-1LA-6SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

		try {
			xmlLoadingCommand.execute();
			Platform platform = rm.getPlatformModel();
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

			Diet diet = rm.getDietModel();
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
