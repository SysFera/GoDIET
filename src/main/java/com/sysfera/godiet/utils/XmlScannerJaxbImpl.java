package com.sysfera.godiet.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.model.xml.generated.DietDescription;

/**
 * 
 * XML reader based on JAXB
 * 
 * @author phi
 */

public class XmlScannerJaxbImpl implements XMLParser{ 

	private String GODIET_SCHEMA_PATH = "/GoDietNG.xsd";
	private String MODEL_PACKAGE_NAME = "com.sysfera.godiet.Model.xml.generated";

	/**
	 */
	@Override
	final public DietDescription buildDietModel(InputStream xmlFile)
			throws IOException, XMLParseException {

		try {
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(this.getClass().getResource(
					GODIET_SCHEMA_PATH));
			u.setSchema(schema);

			DietDescription dietConfiguration = (DietDescription) u
					.unmarshal(xmlFile);
			return dietConfiguration;
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
