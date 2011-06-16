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
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.services.GoDietService;
import com.sysfera.godiet.services.PlatformController;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class GoDietServiceTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;
	@Autowired
	RemoteAccess remoteAccess;

	private PlatformController platformController;
	private DietManager dietModel;

	@Before
	public void init() {
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
				String platformTestCase = "infrastructure/testbed-platform.xml";
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

			dietModel = godiet.getModel().getDietModel();
			platformController = godiet.getPlatformController();

		} catch (IOException e) {
			log.error("",e);
			Assert.fail("" + e.getMessage());

		} catch (XMLParseException e) {
			log.error("",e);
			Assert.fail("" + e.getMessage());

		} catch (GoDietConfigurationException e) {
			log.error("",e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceValidationException e) {
			log.error("",e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceCreationException e) {
			log.error("",e);
			Assert.fail("" + e.getMessage());

		} catch (GraphDataException e) {
			Assert.fail("" + e.getMessage());

		} catch (ResourceAddException e) {
			Assert.fail("" + e.getMessage());

		}
	}

	@Test
	public void testMockIntegrationTest() {
		Assert.assertEquals(3,dietModel.getOmninames().size());
	//TODO	Assert.assertEquals(6,dietModel.getForwarders().size());
		Assert.assertEquals(1,dietModel.getMasterAgents().size());
		Assert.assertEquals(0,dietModel.getLocalAgents().size());
		Assert.assertEquals(3,dietModel.getSeds().size());

		try {
			launchServices();
			launchForwarders();
		} catch (PrepareException e) {
			Assert.fail(e.getMessage());
		} catch (LaunchException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	private void launchServices() throws PrepareException, LaunchException {
		List<DietServiceManaged> omniNames = dietModel.getOmninames();
		for (DietServiceManaged dietServiceManaged : omniNames) {
			platformController.getSoftwareController(
					dietServiceManaged.getSoftwareDescription().getId())
					.prepare();
			platformController.getSoftwareController(
					dietServiceManaged.getSoftwareDescription().getId())
					.start();
		}

	}

	private void launchForwarders() {

	}

	private void launchMasterAgents() {

	}

	private void launchLocalAgents() {

	}

	private void launchSedsAgents() {

	}

}
