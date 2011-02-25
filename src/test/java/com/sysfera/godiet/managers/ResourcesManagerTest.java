package com.sysfera.godiet.managers;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.sysfera.godiet.Utils.XmlScannerJaxbImpl;
import com.sysfera.godiet.exceptions.XMLReadException;

public class ResourcesManagerTest {

	
	@Test
	public void test() {
		String testCaseFile = "1D-3N-1MA-3LA-10SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();

		try {
			rm.load(scanner.buildDietModel(inputStream));
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
			
			
		} catch (IOException e) {
			Assert.fail();
			e.printStackTrace();
		} catch (XMLReadException e) {
			Assert.fail();
			e.printStackTrace();
		}

	}
}
