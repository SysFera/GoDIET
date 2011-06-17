package com.sysfera.godiet.utils.xml;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.model.generated.DietPlatform;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Infrastructure;

public interface XMLParser {
	
	
	/**
	 * Create a DietDescription given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public DietPlatform buildDietModel(InputStream xmlInput)
			throws IOException, XMLParseException;

	/**
	 * Create a goDiet Configuration given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public GoDietConfiguration buildGodietConfiguration(InputStream xmlInput)throws IOException, XMLParseException;
	/**
	 * Create a infrastructure platform given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public Infrastructure buildInfrastructureModel(InputStream xmlFile) throws IOException,
			XMLParseException;
}
