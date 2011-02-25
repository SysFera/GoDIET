package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.sysfera.godiet.exceptions.XMLReadException;
import com.sysfera.godiet.managers.ResourcesManager;

public class XMLScannerJaxbImplTest {

	@Test
	public void testParse() {
		String testCaseFile = "exampleMultiDomainsNG.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();

			try {
				rm.load(scanner.buildDietModel(inputStream));
			} catch (IOException e) {
				Assert.fail();
				e.printStackTrace();
			} catch (XMLReadException e) {
				Assert.fail();
				e.printStackTrace();
			}
				
	

	}
}
