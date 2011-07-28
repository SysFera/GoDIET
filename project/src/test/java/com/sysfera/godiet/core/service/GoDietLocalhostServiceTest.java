package com.sysfera.godiet.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.common.utils.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class GoDietLocalhostServiceTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;


	@Autowired
	private PlatformService platformService;

	@Before
	public void init() throws IncubateException {
		try {
			// Loading configuration
			// Loading configuration
			{
				String configurationFile = "configuration/localhost-configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerConfigurationFile(
						outputString);

			}
			{
				String platformTestCase = "infrastructure/localhost-infrastructure.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				String outputString = StringUtils.streamToString(inputStreamPlatform);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}
			{
				// Init RM
				String testCaseFile = "diet/localhost-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}

	
		} catch (IOException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (XMLParseException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (GoDietConfigurationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceValidationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceCreationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (GraphDataException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (ResourceAddException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		}
	}

	@Test
	public void testMockIntegrationTest() {

		List<SoftwareInterface<Forwarder>> forw = platformService.getForwarders();
		for (SoftwareInterface<Forwarder> dietResourceManaged : forw) {
			System.err.println(dietResourceManaged.getSoftwareDescription()
					.getId()
					+ " on "
					+ dietResourceManaged.getPluggedOn().getId());

		}
		try {
			launchOmniNames();
			launchForwarders();
			launchMasterAgents();
			launchLocalAgents();
			launchSedsAgents();
		} catch (PrepareException e) {
			Assert.fail(e.getMessage());
		} catch (LaunchException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	private void launchOmniNames() throws PrepareException, LaunchException {
		List<SoftwareInterface<OmniNames>> omniNames = platformService.getOmninames();
		for (SoftwareInterface<OmniNames> dietServiceManaged : omniNames) {
			platformService.prepareSoftware(dietServiceManaged.getId());
			platformService.startSoftware(dietServiceManaged.getId());	
			}

	}

	private void launchForwarders() throws PrepareException, LaunchException {
		List<SoftwareInterface<Forwarder>> forwarders = platformService
				.getForwarders();
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("SERVER")) {
				platformService.prepareSoftware(dietResourceManaged.getId());
				platformService.startSoftware(dietResourceManaged.getId());	
			}
		}
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("CLIENT")) {
				platformService.prepareSoftware(dietResourceManaged.getId());
				platformService.startSoftware(dietResourceManaged.getId());	
			}
		}
	}

	private void launchMasterAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<MasterAgent>> masterAgents = platformService.getMasterAgents();
		for (SoftwareInterface<MasterAgent> ma : masterAgents) {
			platformService.prepareSoftware(ma.getId());
			platformService.startSoftware(ma.getId());	
		}
	}

	private void launchLocalAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<LocalAgent>> localAgents = platformService.getLocalAgents();
		for (SoftwareInterface<LocalAgent> la : localAgents) {
			platformService.prepareSoftware(la.getId());
			platformService.startSoftware(la.getId());	
		}
	}

	private void launchSedsAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<Sed>> seds = platformService.getSeds();
		for (SoftwareInterface<Sed> sed : seds) {
			platformService.prepareSoftware(sed.getId());
			platformService.startSoftware(sed.getId());	
		}
	}

}
