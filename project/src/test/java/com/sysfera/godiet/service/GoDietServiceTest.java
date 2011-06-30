package com.sysfera.godiet.service;

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

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.services.GoDietService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class GoDietServiceTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;


	@Autowired
	private DietManager dietModel;

	@Before
	public void init() throws IncubateException {
		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);

				godiet.getXmlHelpController().registerConfigurationFile(
						inputStream);

			}
			{
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				godiet.getXmlHelpController().registerInfrastructureElements(
						inputStreamPlatform);
			}
			{
				// Init RM
				String testCaseFile = "diet/testbed-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				godiet.getXmlHelpController().registerDietElements(inputStream);
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

		List<DietResourceManaged<Forwarder>> forw = dietModel.getForwarders();
		for (DietResourceManaged<Forwarder> dietResourceManaged : forw) {
			System.err.println(dietResourceManaged.getSoftwareDescription()
					.getId()
					+ " on "
					+ dietResourceManaged.getPluggedOn().getId());

		}
		Assert.assertEquals(3, dietModel.getOmninames().size());
		Assert.assertEquals(4, dietModel.getForwarders().size());
		Assert.assertEquals(1, dietModel.getMasterAgents().size());
		Assert.assertEquals(0, dietModel.getLocalAgents().size());
		Assert.assertEquals(3, dietModel.getSeds().size());

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
		List<OmniNamesManaged> omniNames = dietModel.getOmninames();
		for (OmniNamesManaged dietServiceManaged : omniNames) {
			dietServiceManaged.prepare();
			dietServiceManaged.start();
		}

	}

	private void launchForwarders() throws PrepareException, LaunchException {
		List<DietResourceManaged<Forwarder>> forwarders = dietModel
				.getForwarders();
		for (DietResourceManaged<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("SERVER")) {
				dietResourceManaged.prepare();
				dietResourceManaged.start();
			}
		}
		for (DietResourceManaged<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("CLIENT")) {
				dietResourceManaged.prepare();
				dietResourceManaged.start();
			}
		}
	}

	private void launchMasterAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<MasterAgent>> masterAgents = dietModel.getMasterAgents();
		for (DietResourceManaged<MasterAgent> ma : masterAgents) {
			ma.prepare();
			ma.start();
		}
	}

	private void launchLocalAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<LocalAgent>> localAgents = dietModel.getLocalAgents();
		for (DietResourceManaged<LocalAgent> la : localAgents) {
			la.prepare();
			la.start();
		}
	}

	private void launchSedsAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<Sed>> seds = dietModel.getSeds();
		for (DietResourceManaged<Sed> sed : seds) {
			sed.prepare();
			sed.start();
		}
	}

}
