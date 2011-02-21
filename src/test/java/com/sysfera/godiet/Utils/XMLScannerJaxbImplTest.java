package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Controller.DietPlatformController;
import com.sysfera.godiet.exceptions.XMLReadException;

public class XMLScannerJaxbImplTest {

	@Test
	public void testParse() {
		String testCaseFile = "exampleMultiDomainsNG.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ConsoleController cc = new ConsoleController();
		DietPlatformController dietPlatform = new DietPlatformController(cc);
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl(dietPlatform, cc);

		try {
			scanner.buildDietModel(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		
		} catch (XMLReadException e) {
			//e.printStackTrace();
			
			
		}

	}
}
