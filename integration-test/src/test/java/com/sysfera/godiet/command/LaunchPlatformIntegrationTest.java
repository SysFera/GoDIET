package com.sysfera.godiet.command;

import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.remote.ssh.RemoteAccessJschImpl;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml" })
public class LaunchPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

	@Autowired
	private RemoteAccessJschImpl remoteAccess;

	@Before
	public void init() {

		// Init RM
		String testCaseFile = "testbed.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
		// Init Remote Access
		RemoteConfigurationHelper remoteHelper = RemoteConfigurationHelper
				.getInstance();
		remoteHelper.setConfiguration(rm.getGodietConfiguration()
				.getGoDietConfiguration());
		remoteHelper.setPlatform(rm.getPlatformModel());

		// Real Remote SSH

		remoteAccess.debug(true);
		String fakeKey = "fakeuser/testbedKey";
		URL urlFile = getClass().getClassLoader().getResource(fakeKey);
		if (urlFile == null || urlFile.getFile().isEmpty())
			Assert.fail("SSH key not found");

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + fakeKey);
		try {
			remoteAccess.addItentity(urlFile.getFile(), null, "godiet");
		} catch (AddKeyException e) {
			Assert.fail("Unable to load testbed key");
		}

		try {
			// Here add a key to access on testbed
			remoteAccess.addItentity("/home/phi/tmp/id_dsa", null, "");
		} catch (AddKeyException e) {
			log.error("unable to load your key");
		}

		remoteHelper.setRemoteAccess(remoteAccess);

	}

	/***
	 * Launch all the elements described in the XML file
	 */
	@Test
	public void launchPlatform() {
		// Services commands
		PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
		prepareCommand.setRm(rm);
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);

		// Agents commands
		InitForwardersCommand initForwardersCommand = new InitForwardersCommand();
		initForwardersCommand.setRm(rm);

		PrepareAgentsCommand prepareAgents = new PrepareAgentsCommand();
		prepareAgents.setRm(rm);

		StartForwardersCommand launchForwarders = new StartForwardersCommand();
		launchForwarders.setRm(rm);
		StopForwardersCommand stopForwarders = new StopForwardersCommand();
		stopForwarders.setRm(rm);

		StartAgentsCommand startMas = new StartAgentsCommand();
		startMas.setRm(rm);
		StopAgentsCommand stopAgents = new StopAgentsCommand();
		stopAgents.setRm(rm);

		try {
			prepareCommand.execute();
			launchServicesCommand.execute();

			initForwardersCommand.execute();

			prepareAgents.execute();
		
			launchForwarders.execute();
			startMas.execute();
			
		} catch (CommandExecutionException e) {
			log.error(e.getMessage());
			Assert.fail(e.getMessage());
		} finally {
			try {
				stopAgents.execute();
			} catch (CommandExecutionException e) {
				log.error(e.getMessage());
				Assert.fail(e.getMessage());

			} finally {
				try {
					stopForwarders.execute();
				} catch (CommandExecutionException e) {
					log.error(e.getMessage());
					Assert.fail(e.getMessage());
				} finally {
					try {
						stopServicesCommand.execute();
					} catch (CommandExecutionException e) {
						log.error(e.getMessage());
						Assert.fail(e.getMessage());
					}
				}
			}
		}

	}
}
