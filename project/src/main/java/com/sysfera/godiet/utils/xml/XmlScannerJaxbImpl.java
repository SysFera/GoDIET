package com.sysfera.godiet.utils.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.model.generated.Configuration;
import com.sysfera.godiet.model.generated.Diet;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Platform;

/**
 * 
 * XML reader based on JAXB
 * 
 * @author phi
 */

public class XmlScannerJaxbImpl implements XMLParser {

	private final static String GODIET_CONFIGURATION_SCHEMA_PATH = "/Configuration.xsd";
	private final static String GODIET_DIET_SCHEMA_PATH = "/Diet.xsd";
	private final static String GODIET_PLATFORM_SCHEMA_PATH = "/Platform.xsd";
	private final static String MODEL_PACKAGE_NAME = "com.sysfera.godiet.model.generated";

	/**
	 */
	@Override
	final public Diet buildDietModel(InputStream xmlFile) throws IOException,
			XMLParseException {

		try {
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(this.getClass().getResource(
					GODIET_DIET_SCHEMA_PATH));
			u.setSchema(schema);

			Diet diet = (Diet) u.unmarshal(xmlFile);
			return diet;
		} catch (JAXBException e) {
			throw new XMLParseException("Error when marshalling diet model", e);
		} catch (SAXException e) {
			throw new XMLParseException(
					"Error when marshalling diet model (unable to read schema)",
					e);
		} catch (IllegalArgumentException e) {
			throw new XMLParseException("File not found", e);
		}
	}



	@Override
	final public Platform buildPlatformModel(InputStream xmlFile)
			throws IOException, XMLParseException {

		try {
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(this.getClass().getResource(
					GODIET_PLATFORM_SCHEMA_PATH));
			u.setSchema(schema);

			Platform platform = (Platform) u.unmarshal(xmlFile);
			return platform;
		} catch (JAXBException e) {
			throw new XMLParseException("Error when marshalling diet model", e);
		} catch (SAXException e) {
			throw new XMLParseException(
					"Error when marshalling diet model (unable to read schema)",
					e);
		} catch (IllegalArgumentException e) {
			throw new XMLParseException("File not found", e);
		}
	}

	class JAXBValidator extends ValidationEventCollector {
		@Override
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == event.ERROR
					|| event.getSeverity() == event.FATAL_ERROR) {
				ValidationEventLocator locator = event.getLocator();
				// change RuntimeException to something more appropriate
				throw new RuntimeException("XML Validation Exception:  "
						+ event.getMessage() + " at row: "
						+ locator.getLineNumber() + " column: "
						+ locator.getColumnNumber());
			}

			return true;
		}
	}

	@Override
	public GoDietConfiguration buildGodietConfiguration(InputStream xmlInput)
			throws IOException, XMLParseException {

		try {
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(this.getClass().getResource(
					GODIET_CONFIGURATION_SCHEMA_PATH));
			u.setSchema(schema);
			Configuration config = (Configuration)u.unmarshal(xmlInput);
			return config.getGoDietConfiguration();
		} catch (JAXBException e) {
			throw new XMLParseException("Error when marshalling diet model", e);
		} catch (SAXException e) {
			throw new XMLParseException(
					"Error when marshalling diet model (unable to read schema)",
					e);
		} catch (IllegalArgumentException e) {
			throw new XMLParseException("File not found", e);
		}
	}

}
