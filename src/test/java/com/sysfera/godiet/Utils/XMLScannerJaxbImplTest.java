package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.XMLReadException;
import com.sysfera.godiet.managers.ResourcesManager;

public class XMLScannerJaxbImplTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testFileNotFound() {
		String testCaseFile = "filefrommars.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		ResourcesManager rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();

		try {
			rm.load(scanner.buildDietModel(inputStream));
		} catch (IOException e) {
			log.error("",e);
			Assert.fail();
		} catch (XMLReadException e) {
			// Cool
		}

	}

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
			log.error("", e);
			Assert.fail();
		} catch (XMLReadException e) {
			log.error("", e);
			Assert.fail();
		}

	}
}
