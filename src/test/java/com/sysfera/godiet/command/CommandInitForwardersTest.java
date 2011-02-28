package com.sysfera.godiet.command;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Utils.XmlScannerJaxbImpl;
import com.sysfera.godiet.command.CommandInitForwarders;
import com.sysfera.godiet.command.CommandLoadXMLImpl;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;

public class CommandInitForwardersTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

	@Before
	public void initRM() {
		String testCaseFile = "3D-5N-3G-3L-1MA-3SED.xml";
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
			Assert.fail();
			log.error("Test Fail", e);
		}

	}

	@Test
	public void testCommandInit() {
		CommandInitForwarders initForwardersInit = new CommandInitForwarders();
		initForwardersInit.setRm(rm);

		try {
			initForwardersInit.execute();
			List<DietResourceManager> forwarders = rm.getDietModel().getForwarders();
			if(forwarders.size() != 6) Assert.fail();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());
		}

	}
}
