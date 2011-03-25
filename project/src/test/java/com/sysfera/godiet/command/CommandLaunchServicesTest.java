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

public class CommandLaunchServicesTest {

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


	
	@Test
	public void testLaunchService() {
		CommandLaunchServices launchServicesCommand = new CommandLaunchServices();
		launchServicesCommand.setRm(rm);
		try {
			launchServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
