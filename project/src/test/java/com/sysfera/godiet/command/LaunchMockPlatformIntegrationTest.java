package com.sysfera.godiet.command;

import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.remote.ssh.RemoteAccessJschImpl;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;


public class LaunchMockPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;


	@Before
	public void init() {

		
		//Init RM
		String testCaseFile = "testbed.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		CommandLoadXMLImpl xmlLoadingCommand = new CommandLoadXMLImpl();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
		//Init Remote Access
		RemoteConfigurationHelper remoteHelper = RemoteConfigurationHelper.getInstance();
		remoteHelper.setConfiguration(rm.getGodietConfiguration().getGoDietConfiguration());
		remoteHelper.setPlatform(rm.getPlatformModel());
		RemoteAccess remoteAccess = new RemoteAccessMock();
		
		remoteHelper.setRemoteAccess(remoteAccess);
	}

	/***
	 * Launch all the elements described in the XML file
	 */
	@Test
	public void launchPlatform() {
		//Services Command
		CommandPrepareServices prepareCommand = new CommandPrepareServices();
		prepareCommand.setRm(rm);
		CommandLaunchServices launchServicesCommand = new CommandLaunchServices();
		launchServicesCommand.setRm(rm);
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);
		
		//Agent Services
		CommandPrepareAgents prepareAgents = new CommandPrepareAgents();
		prepareAgents.setRm(rm);
		CommandInitForwarders initForwardersCommand = new CommandInitForwarders();
		initForwardersCommand.setRm(rm);
		try {
			prepareCommand.execute();
			launchServicesCommand.execute();
			initForwardersCommand.execute();
			prepareAgents.execute();

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
