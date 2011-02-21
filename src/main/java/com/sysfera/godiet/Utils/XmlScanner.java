package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Controller.DietPlatformController;
import com.sysfera.godiet.exceptions.XMLReadException;

public interface XmlScanner {

	public abstract void buildDietModel(InputStream xmlFile)
			throws IOException, XMLReadException;

}