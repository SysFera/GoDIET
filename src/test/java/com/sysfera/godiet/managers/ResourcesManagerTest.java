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
		String testCaseFile = "1D-3N-1MA-2LA-10SED.xml";
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
