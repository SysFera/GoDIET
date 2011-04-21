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

import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.prepare.PrepareAgentsCommand;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.command.start.StartAgentsCommand;
import com.sysfera.godiet.command.start.StartForwardersCommand;
import com.sysfera.godiet.command.start.StartServicesCommand;
import com.sysfera.godiet.command.stop.StopAgentsCommand;
import com.sysfera.godiet.command.stop.StopForwardersCommand;
import com.sysfera.godiet.command.stop.StopServicesCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.managers.ResourcesManager;
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
		rm = new ResourcesManager();

		// Loading configuration
		{
			String configurationFile = "configuration/configuration.xml";

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);
			InitUtil.initConfig(rm, inputStream);
		}
		{
			String platformTestCase = "platform/testbed-platform.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			InitUtil.initPlatform(rm, inputStreamPlatform);
		}
		{
			// Init RM
			String testCaseFile = "diet/testbed-diet.xml";
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

			} catch (CommandExecutionException e) {
				log.error("Test Fail", e);
				Assert.fail(e.getMessage());
			}
		}
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
		initForwardersCommand.setRemoteAccess(remoteAccess);
		
		PrepareAgentsCommand prepareAgents = new PrepareAgentsCommand();
		prepareAgents.setRm(rm);

		StartForwardersCommand launchForwarders = new StartForwardersCommand();
		launchForwarders.setRm(rm);
		StopForwardersCommand stopForwarders = new StopForwardersCommand();
		stopForwarders.setRm(rm);

		StartAgentsCommand startAgent = new StartAgentsCommand();
		startAgent.setRm(rm);
		StopAgentsCommand stopAgents = new StopAgentsCommand();
		stopAgents.setRm(rm);

		try {
			prepareCommand.execute();
			launchServicesCommand.execute();

			initForwardersCommand.execute();

			prepareAgents.execute();

			launchForwarders.execute();
			startAgent.execute();
			// try {
			// char c = (char)System.in.read();
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
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
