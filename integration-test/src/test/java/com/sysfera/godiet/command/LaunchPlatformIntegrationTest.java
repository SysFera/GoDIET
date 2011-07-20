package com.sysfera.godiet.command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

import com.sysfera.godiet.controllers.SSHKeyController;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.User;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.services.GoDietService;
import com.sysfera.godiet.services.PlatformService;
import com.sysfera.godiet.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class LaunchPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;


	@Autowired
	private UserService userController;

	@Before
	public void init() throws IncubateException {
		try {
			{
				// Loading configuration
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);

				godiet.getXmlHelpService().registerConfigurationFile(
						inputStream);
			}
			{
				// Load infrastructure
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				godiet.getXmlHelpService().registerInfrastructureElements(
						inputStreamPlatform);
			}
			{
				// Load Diet scenarii
				String testCaseFile = "diet/testbed-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				godiet.getXmlHelpService().registerDietElements(inputStream);
			}
			String fakeKey = "fakeuser/testbedKey";
			URL urlFile = getClass().getClassLoader().getResource(fakeKey);
			if (urlFile == null || urlFile.getFile().isEmpty())
				Assert.fail("SSH key not found");

			try {
				User.Ssh.Key sshDesc = new User.Ssh.Key();
				sshDesc.setPath(urlFile.getPath());
				SSHKeyController managedKey = userController.addSSHKey(sshDesc);
				managedKey.setPassword("godiet"); 
				userController.registerSSHKey(managedKey);

			} catch (AddAuthentificationException e) {
				Assert.fail("Unable to load testbed key");
			}
			User.Ssh.Key mykeyDesc = new User.Ssh.Key();
			try {
				// My local Graal access key
				mykeyDesc.setPath("/home/phi/tmp/id_dsa");
				SSHKeyController managedKey = userController.addSSHKey(mykeyDesc);
				managedKey.setPassword("godiet");
				userController.registerSSHKey(managedKey);

			} catch (AddAuthentificationException e) {
				log.warn("Unable to load your key" + mykeyDesc.getPath());
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

	/***
	 * Launch all the elements described in the XML file
	 */
	@Test
	public void launchPlatform() {
		try {
			launchOmniNames();
			launchForwarders();
			launchMasterAgents();
			launchLocalAgents();
			launchSedsAgents();
		} catch (PrepareException e) {
			log.error("",e);
			Assert.fail(e.getMessage());
		} catch (LaunchException e) {
			log.error("",e);
			Assert.fail(e.getMessage());
		} finally {
			if (stopAll())
				Assert.fail("Erreur when stop a software");
		}

	}

	private void launchOmniNames() throws PrepareException, LaunchException {
		List<SoftwareInterface<OmniNames>> omniNames = godiet.getPlatformService().getOmninames();
		for (SoftwareInterface<OmniNames> dietServiceManaged : omniNames) {
			dietServiceManaged.prepare();
			dietServiceManaged.start();
		}

	}

	private void launchForwarders() throws PrepareException, LaunchException {
		List<SoftwareInterface<Forwarder>> forwarders = godiet.getPlatformService()
				.getForwarders();
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("SERVER")) {
				dietResourceManaged.prepare();
				dietResourceManaged.start();
			}
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("Test interruped",e);
			Assert.fail("Interrupted ??");
		}
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("CLIENT")) {
				dietResourceManaged.prepare();
				dietResourceManaged.start();
			}
		}
	}

	private void launchMasterAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<MasterAgent>> masterAgents = godiet.getPlatformService()
				.getMasterAgents();
		for (SoftwareInterface<MasterAgent> ma : masterAgents) {
			ma.prepare();
			ma.start();
		}
	}

	private void launchLocalAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<LocalAgent>> localAgents = godiet.getPlatformService()
				.getLocalAgents();
		for (SoftwareInterface<LocalAgent> la : localAgents) {
			la.prepare();
			la.start();
		}
	}

	private void launchSedsAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<Sed>> seds = godiet.getPlatformService().getSeds();
		for (SoftwareInterface<Sed> sed : seds) {
			sed.prepare();
			sed.start();
		}
	}

	private boolean stopAll() {
		boolean failed = false;
		List<SoftwareInterface<? extends Software>> softwares = godiet.getPlatformService()
				.getAllSoftwares();

		for (SoftwareInterface<? extends Software> softwareManager : softwares) {
			try {
				softwareManager.stop();
			} catch (StopException e) {
				// Try to stop everything
				log.error("Unable to stop "
						+ softwareManager.getSoftwareDescription().getId());
				failed = true;
			}
		}

		return failed;
	}
}
