package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class LaunchMockPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

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
		RemoteAccess remoteAccess = new RemoteAccessMock();

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
