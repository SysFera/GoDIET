package com.sysfera.godiet.core.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.model.generated.DietPlatform;
import com.sysfera.godiet.common.model.generated.GoDietConfiguration;
import com.sysfera.godiet.common.model.generated.Infrastructure;

/**
 * 
 * XML reader based on JAXB
 * 
 * @author phi
 */

public class XmlScannerJaxbImpl implements XMLParser {

	private final static String GODIET_CONFIGURATION_SCHEMA_PATH = "/Configuration.xsd";
	private final static String GODIET_DIET_SCHEMA_PATH = "/Diet.xsd";
	private final static String GODIET_PLATFORM_SCHEMA_PATH = "/Infrastructure.xsd";
	private final static String MODEL_PACKAGE_NAME = "com.sysfera.godiet.common.model.generated";

	/**
	 */
	@Override
	final public DietPlatform buildDietModel(String xmlFile) throws IOException,
			XMLParseException {

		try {
			if(xmlFile == null) throw new XMLParseException("XML description empty");
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL schemaFile = this.getClass().getResource(
					GODIET_DIET_SCHEMA_PATH);
			if(schemaFile == null) throw new XMLParseException("Unable to find schema description: "+GODIET_DIET_SCHEMA_PATH);
			Schema schema = sf.newSchema(schemaFile);
			u.setSchema(schema);
			Object obj = u.unmarshal(new StringReader(xmlFile));
			DietPlatform diet = (DietPlatform)obj;
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
	final public Infrastructure buildInfrastructureModel(String xmlFile)
			throws IOException, XMLParseException {

		try {
			if(xmlFile == null) throw new XMLParseException("XML description empty");

			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL schemaFile = this.getClass().getResource(
					GODIET_PLATFORM_SCHEMA_PATH);
			if(schemaFile == null) throw new XMLParseException("Unable to find schema description: "+GODIET_PLATFORM_SCHEMA_PATH);
			Schema schema = sf.newSchema(schemaFile);
			u.setSchema(schema);

			Infrastructure platform = (Infrastructure) u.unmarshal(new StringReader(xmlFile));
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
	public GoDietConfiguration buildGodietConfiguration(String xmlInput)
			throws IOException, XMLParseException {

		try {
			if(xmlInput == null) throw new XMLParseException("XML description empty");

			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			URL schemaFile = this.getClass().getResource(
					GODIET_CONFIGURATION_SCHEMA_PATH);
			if(schemaFile == null) throw new XMLParseException("Unable to find schema description: "+GODIET_CONFIGURATION_SCHEMA_PATH);
			Schema schema = sf.newSchema(schemaFile);
			u.setSchema(schema);
			GoDietConfiguration config = (GoDietConfiguration)u.unmarshal(new StringReader(xmlInput));
			return config;
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
