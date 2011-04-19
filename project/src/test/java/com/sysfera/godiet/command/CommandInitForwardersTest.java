package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandInitForwardersTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	private XmlScannerJaxbImpl scanner;
	LoadXMLImplCommand xmlLoadingCommand;
	RemoteAccess remoteAccess = new RemoteAccessMock();

	@Before
	public void initRM() {
		scanner = new XmlScannerJaxbImpl();
		xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

	}

	@Test
	public void testCommandInitForwarder1() {
		String testCaseFile = "3D-5N-3G-3L-1MA-3SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}
		InitForwardersCommand initForwardersInit = new InitForwardersCommand();
		initForwardersInit.setRm(rm);
		initForwardersInit.setRemoteAccess(remoteAccess);
		try {
			initForwardersInit.execute();
			List<DietResourceManaged> forwarders = rm.getDietModel()
					.getForwarders();
			if (forwarders.size() != 6)
				Assert.fail();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());
		}

	}
	
	@Test
	public void testCommandInitForwarder2() {
		String testCaseFile = "testbed.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}
		InitForwardersCommand initForwardersInit = new InitForwardersCommand();
		initForwardersInit.setRm(rm);
		initForwardersInit.setRemoteAccess(remoteAccess);

		try {
			initForwardersInit.execute();
			List<DietResourceManaged> forwarders = rm.getDietModel()
					.getForwarders();
			if (forwarders.size() != 6)
				Assert.fail();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());
		}

	}
	
	
}
