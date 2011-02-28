package com.sysfera.godiet.Utils;

import java.io.InputStream;



import junit.framework.Assert;

import org.junit.Test;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.exceptions.XMLReadException;
/**
 * Test of Old style parsing
 * @author phi
 *
 */
public class XMLScannerTest {

	@Test
	public void testParse() {
		String testCaseFile = "exampleMultiDomains.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		if (inputStream == null)
			Assert.fail("Unable to open test file" + testCaseFile);
		ConsoleController consoleController = new ConsoleController();
		try {
			consoleController.loadXmlFile(inputStream);
		} catch (XMLReadException e) {
			e.printStackTrace();
			Assert.fail(e.fillInStackTrace().toString());
		}
		
	
	}
}
