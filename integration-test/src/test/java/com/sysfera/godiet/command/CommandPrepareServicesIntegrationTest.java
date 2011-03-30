package com.sysfera.godiet.command;

import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.remote.ssh.RemoteAccessJschImpl;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandPrepareServicesIntegrationTest {
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
		//Real Remote SSH
		RemoteAccessJschImpl remoteAccess = new RemoteAccessJschImpl();
		remoteAccess.jschDebug(true);
		String fakeKey = "fakeuser/testbedKey";
		URL urlFile = getClass().getClassLoader().getResource(fakeKey);
		if (urlFile == null || urlFile.getFile().isEmpty())
			Assert.fail("SSH key not found");

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + fakeKey);
		try {
			remoteAccess.addKey(urlFile.getFile(), null, "godiet");
			// Here add a key to access on testbed
			remoteAccess.addKey("/home/phi/tmp/id_dsa", null, "");
		} catch (AddKeyException e) {
			Assert.fail(e.getMessage());
		}
		
		
		remoteHelper.setRemoteAccess(remoteAccess);

	}
	
	@Test
	public void testPrepareService() {
		CommandPrepareServices prepareServicesCommand = new CommandPrepareServices();
		prepareServicesCommand.setRm(rm);
		try {
			prepareServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	
	}
}
