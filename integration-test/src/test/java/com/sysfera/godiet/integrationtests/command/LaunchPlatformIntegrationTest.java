package com.sysfera.godiet.integrationtests.command;

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

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.ModifyAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.generated.User;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.services.UserService;
import com.sysfera.godiet.common.utils.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/godiet-service.xml"
})
		
		//TODO: Unit test with rmi
		//"/spring/godiet-service-rmi.xml" })
public class LaunchPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;


	@Autowired
	private UserService userController;

	@Before
	public void init() throws IncubateException {
		try {

			// Loading configuration
			{
				String configurationFile = "configuration/configuration-local.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				String outputString = StringUtils.streamToString(inputStream);
				
				godiet.getXmlHelpService().registerConfigurationFile(
						outputString);

			}
			{
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				String outputString = StringUtils.streamToString(inputStreamPlatform);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}
			{
				// Init RM
				String testCaseFile = "diet/testbed-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}

			String fakeKey = "fakeuser/testbedKey";
			URL urlFile = getClass().getClassLoader().getResource(fakeKey);
			if (urlFile == null || urlFile.getFile().isEmpty())
				Assert.fail("SSH key not found");

			try {
				User.Ssh.Key sshDesc = new User.Ssh.Key();
				sshDesc.setPath(urlFile.getPath());
				Integer keyId = userController.addSSHKey(sshDesc);
				
				userController.modifyKey(keyId, sshDesc.getPath(), sshDesc.getPathPub(), "godiet");

			} catch (AddAuthentificationException e) {
				Assert.fail("Unable to load testbed key");
			} catch (ModifyAuthentificationException e) {
				Assert.fail("Unable to load testbed key");
			}
			User.Ssh.Key mykeyDesc = new User.Ssh.Key();
			try {
				// My local Graal access key
				mykeyDesc.setPath("/home/phi/tmp/id_dsa");
				Integer keyId = userController.addSSHKey(mykeyDesc);
				userController.modifyKey(keyId, mykeyDesc.getPath(), mykeyDesc.getPathPub(), "godiet");

			} catch (AddAuthentificationException e) {
				log.warn("Unable to load your key" + mykeyDesc.getPath());
			} catch (ModifyAuthentificationException e) {
				log.warn("Unable to load your key" + mykeyDesc.getPath());
			}
		
		} catch (IOException e) {
			log.error("", e);
			//Assert.fail("" + e.getMessage());

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
			godiet.getPlatformService().prepareSoftware(dietServiceManaged.getId());
			godiet.getPlatformService().startSoftware(dietServiceManaged.getId());	
			}

	}

	private void launchForwarders() throws PrepareException, LaunchException {
		List<SoftwareInterface<Forwarder>> forwarders = godiet.getPlatformService()
				.getForwarders();
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("SERVER")) {
				godiet.getPlatformService().prepareSoftware(dietResourceManaged.getId());
				godiet.getPlatformService().startSoftware(dietResourceManaged.getId());	
			}
		}
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("CLIENT")) {
				godiet.getPlatformService().prepareSoftware(dietResourceManaged.getId());
				godiet.getPlatformService().startSoftware(dietResourceManaged.getId());	
			}
		}
	}

	private void launchMasterAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<MasterAgent>> masterAgents = godiet.getPlatformService().getMasterAgents();
		for (SoftwareInterface<MasterAgent> ma : masterAgents) {
			godiet.getPlatformService().prepareSoftware(ma.getId());
			godiet.getPlatformService().startSoftware(ma.getId());	
		}
	}

	private void launchLocalAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<LocalAgent>> localAgents = godiet.getPlatformService().getLocalAgents();
		for (SoftwareInterface<LocalAgent> la : localAgents) {
			godiet.getPlatformService().prepareSoftware(la.getId());
			godiet.getPlatformService().startSoftware(la.getId());	
		}
	}

	private void launchSedsAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<Sed>> seds = godiet.getPlatformService().getSeds();
		for (SoftwareInterface<Sed> sed : seds) {
			godiet.getPlatformService().prepareSoftware(sed.getId());
			godiet.getPlatformService().startSoftware(sed.getId());	
		}
	}

	private boolean stopAll() {
		boolean failed = false;
		List<SoftwareInterface<? extends Software>> softwares = godiet.getPlatformService()
				.getAllSoftwares();

		for (SoftwareInterface<? extends Software> softwareManager : softwares) {
			try {
				godiet.getPlatformService().stopSoftware(softwareManager.getId());	
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
