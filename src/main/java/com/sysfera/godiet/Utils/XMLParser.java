package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.Model.xml.generated.DietDescription;
import com.sysfera.godiet.exceptions.XMLParseException;

public interface XMLParser {
	
	
	public DietDescription buildDietModel(InputStream xmlFile)
			throws IOException, XMLParseException;
}
