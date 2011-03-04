package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.utils.RemoteAccess;
import com.sysfera.godiet.utils.RemoteAccessMock;
import com.sysfera.godiet.utils.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.XmlScannerJaxbImpl;

public class CommandPrepareAgentsTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

	@Before
	public void init() {
		
		//Init RM
		String testCaseFile = "exampleLocalhost.xml";
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
		RemoteAccess remoteAccess = new RemoteAccessMock();
		
		remoteHelper.setRemoteAccess(remoteAccess);

	}

	@Test
	public void testPrepare() {
		CommandPrepareAgents prepareCommand = new CommandPrepareAgents();
		prepareCommand.setRm(rm);
		try {
			prepareCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
