package com.sysfera.godiet.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.utils.StringUtils;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.managers.InfrastructureManager;
import com.sysfera.godiet.core.managers.ResourcesManager;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class LoadXMLServiceTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;
	@Autowired
	private ResourcesManager rm ;


	@Before
	public void init() throws IncubateException {

		{
			// Loading configuration
			String configurationFile = "configuration/configuration.xml";

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);

			try {
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerConfigurationFile(
						outputString);
			} catch (IOException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (XMLParseException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (GoDietConfigurationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@DirtiesContext
	@Test
	public void testCommand() {
		List<String> testCaseFiles = Arrays
				.asList(new String[] { "diet/2MA-1LA-6SED.xml",
				// "diet/1MA-3LA-10SED.xml",
				// "diet/1MA-3SED.xml"
				});
		String infraCaseFiles = "infrastructure/3D-5N-3G-3L.xml";

		InputStream inputStreamPlatform = getClass().getClassLoader()
				.getResourceAsStream(infraCaseFiles);
		try {
			String outputString = StringUtils.streamToString(inputStreamPlatform);

			godiet.getXmlHelpService().registerInfrastructureElements(
					outputString);
		} catch (IOException e) {
			log.error("", e);
			Assert.fail(e.getMessage());
		} catch (XMLParseException e) {
			log.error("", e);
			Assert.fail(e.getMessage());
		} catch (ResourceAddException e) {
			log.error("", e);
			Assert.fail(e.getMessage());
		} catch (GraphDataException e) {
			log.error("", e);
			Assert.fail(e.getMessage());
		}

		for (String testCaseFile : testCaseFiles) {
			// Retry with the same config

			try {

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			} catch (Exception e) {
				log.error("Test fail for file: " + testCaseFile, e);
				Assert.fail();

			}
		}

	}

	@DirtiesContext
	@Test
	public void testCountDietElement1() {

		try{
			{
				String infra = "infrastructure/3D-5N-3G-3L.xml";
				InputStream inputStreamInfra = getClass().getClassLoader()
						.getResourceAsStream(infra);
				String outputString = StringUtils.streamToString(inputStreamInfra);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}

			
			{
				// Load Diet scenarii
				String testCaseFile = "diet/1MA-3LA-10SED.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}
			
			InfrastructureManager platform = rm.getInfrastructureModel();
			// if (platform.getClusters().size() != 0)
			// Assert.fail();
			if (platform.getDomains().size() != 4)
				Assert.fail();
			// if (platform.getFrontends().size() != 0)
			// Assert.fail();

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
			for (SoftwareInterface<MasterAgent> ma : diet.getMasterAgents()) {
				if (ma.getPluggedOn() == null)
					Assert.fail();
			}
			for (SoftwareInterface<LocalAgent> la : diet.getLocalAgents()) {
				if (la.getPluggedOn() == null)
					Assert.fail();
			}

			for (SoftwareInterface<Sed> sed : diet.getSeds()) {
				if (sed.getPluggedOn() == null)
					Assert.fail();
			}
			

		}catch(Exception e )
		{
			Assert.fail(e.getMessage());
			log.error("",e);
		}

	}

	@Test
	@DirtiesContext
	public void testCountDietElement2() {
		try{
			{
				String infra = "infrastructure/3D-5N-3G-3L.xml";
				InputStream inputStreamInfra = getClass().getClassLoader()
						.getResourceAsStream(infra);
				String outputString = StringUtils.streamToString(inputStreamInfra);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}

			
			{
				// Load Diet scenarii
				String testCaseFile = "diet/2MA-1LA-6SED.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}
			InfrastructureManager platform = rm.getInfrastructureModel();
	

		
		
			// if (platform.getClusters().size() != 0)
			// Assert.fail();
			if (platform.getDomains().size() != 4)
				Assert.fail();
			// if (platform.getFrontends().size() != 0)
			// Assert.fail();
			//
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
			for (SoftwareInterface<MasterAgent> ma : diet.getMasterAgents()) {
				if (ma.getPluggedOn() == null)
					Assert.fail();
			}
			for (SoftwareInterface<LocalAgent> la : diet.getLocalAgents()) {
				if (la.getPluggedOn() == null)
					Assert.fail();
			}

			for (SoftwareInterface<Sed> sed : diet.getSeds()) {
				if (sed.getPluggedOn() == null)
					Assert.fail();
			}

		} catch (Exception e) {
			log.error("Test testCountDietElement2 Fail", e);
			Assert.fail();
		}

	}
}
