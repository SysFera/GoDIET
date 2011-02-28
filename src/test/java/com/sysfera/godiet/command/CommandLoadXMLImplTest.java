package com.sysfera.godiet.command;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Utils.XMLParser;
import com.sysfera.godiet.Utils.XmlScannerJaxbImpl;
import com.sysfera.godiet.command.CommandLoadXMLImpl;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.managers.ResourcesManager;

public class CommandLoadXMLImplTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testCommand() {
		String testCaseFile = "exampleMultiDomainsNG.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XMLParser scanner = new XmlScannerJaxbImpl();

		CommandLoadXMLImpl xmlLoadingCommand = new CommandLoadXMLImpl();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();
		} catch (CommandExecutionException e) {
			log.error("Test fail",e);
			Assert.fail();

		}

	}
	
	@Test
	public void test() {
		String testCaseFile = "1D-3N-1MA-3LA-10SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		CommandLoadXMLImpl xmlLoadingCommand = new CommandLoadXMLImpl();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();
			Platform platform = rm.getPlatformModel();
			if(platform.getClusters().size()  != 0) Assert.fail();
			if(platform.getDomains().size()  != 1) Assert.fail();
			if(platform.getFrontends().size()  != 0) Assert.fail();
			if(platform.getGateways().size()  != 0) Assert.fail();
			if(platform. getLinks().size() != 0) Assert.fail();
			if(platform.getNodes().size()  != 3) Assert.fail(platform.getNodes().size()  +" != 3");
			
			
			Diet diet = rm.getDietModel();
			if(diet.getMasterAgents().size() != 1 ) Assert.fail();
			if(diet.getLocalAgents().size() != 3 ) Assert.fail();
			if(diet.getSeds().size() != 10 ) Assert.fail();
			
			//check if all Diet are pluged  on physical resources
			for (DietResourceManager ma : diet.getMasterAgents()) {
				if(ma.getPluggedOn() == null) Assert.fail();
			}
			for (DietResourceManager la : diet.getLocalAgents()) {
				if(la.getPluggedOn() == null) Assert.fail();
			}
			
			for (DietResourceManager sed : diet.getSeds()) {
				if(sed.getPluggedOn() == null) Assert.fail();
			}
			
			
			
		} catch (CommandExecutionException e) {
			log.error("Test Fail",e);
			Assert.fail();
		}

	}
}
