package com.sysfera.godiet.utils.xml;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class XMLScannerJaxbImplTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testFileNotFound() {
		String testCaseFile = "filefromMars.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();

		try {
			scanner.buildDietModel(inputStream);
		} catch (IOException e) {
			log.error("", e);
			Assert.fail();
		} catch (XMLParseException e) {
			// Cool
		}

	}
}
