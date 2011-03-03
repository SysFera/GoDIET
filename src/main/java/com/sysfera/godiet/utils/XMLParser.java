package com.sysfera.godiet.utils;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.model.xml.generated.DietDescription;

public interface XMLParser {
	
	
	public DietDescription buildDietModel(InputStream xmlFile)
			throws IOException, XMLParseException;
}
