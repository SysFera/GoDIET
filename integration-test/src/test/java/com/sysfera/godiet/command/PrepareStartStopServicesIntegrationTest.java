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
@ContextConfiguration(locations={"/spring/spring-config.xml","/spring/ssh-context.xml"})
public class PrepareStartStopServicesIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

	@Autowired
	private RemoteAccessJschImpl remoteAccess;
	
	
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

		//Real Remote SSH
		
		
		remoteAccess.debug(true);
		String fakeKey = "fakeuser/testbedKey";
		URL urlFile = getClass().getClassLoader().getResource(fakeKey);
		if (urlFile == null || urlFile.getFile().isEmpty())
			Assert.fail("SSH key not found");

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + fakeKey);
		try {
			remoteAccess.addItentity(urlFile.getFile(), null, "godiet");
			// Here add a key to access on testbed
			remoteAccess.addItentity("/home/phi/tmp/id_dsa", null, "");
		} catch (AddKeyException e) {
			Assert.fail(e.getMessage());
		}
		
		
		remoteHelper.setRemoteAccess(remoteAccess);

	}
	
	@Test
	public void testPrepareStartStopServices() {
		CommandPrepareServices prepareCommand = new CommandPrepareServices();
		prepareCommand.setRm(rm);
		CommandLaunchServices launchServicesCommand = new CommandLaunchServices();
		launchServicesCommand.setRm(rm);
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);
		try {
			prepareCommand.execute();
			launchServicesCommand.execute();
			stopServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	
	}
}
