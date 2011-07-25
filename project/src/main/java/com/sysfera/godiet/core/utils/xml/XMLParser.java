package com.sysfera.godiet.core.utils.xml;

import java.io.IOException;

import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.model.generated.DietPlatform;
import com.sysfera.godiet.common.model.generated.GoDietConfiguration;
import com.sysfera.godiet.common.model.generated.Infrastructure;

public interface XMLParser {
	
	
	/**
	 * Create a DietDescription given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public DietPlatform buildDietModel(String xmlInput)
			throws IOException, XMLParseException;

	/**
	 * Create a goDiet Configuration given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public GoDietConfiguration buildGodietConfiguration(String xmlInput)throws IOException, XMLParseException;
	/**
	 * Create a infrastructure platform given a xml inpustream description. 
	 * @param xmlInput
	 * @return
	 * @throws IOException
	 * @throws XMLParseException
	 */
	public Infrastructure buildInfrastructureModel(String xmlFile) throws IOException,
			XMLParseException;
}
