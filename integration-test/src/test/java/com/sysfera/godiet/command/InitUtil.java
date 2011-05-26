package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import com.sysfera.godiet.command.xml.LoadXMLConfigurationCommand;
import com.sysfera.godiet.command.xml.LoadXMLInfrastructureCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class InitUtil {
	private static XMLParser scanner = new XmlScannerJaxbImpl();
	public static void initConfig(ResourcesManager rm, InputStream inputStream)
	{
		

		LoadXMLConfigurationCommand xmlConfigurationLoadingCommand = new LoadXMLConfigurationCommand();
	
		
		xmlConfigurationLoadingCommand.setXmlInput(inputStream);
		xmlConfigurationLoadingCommand.setXmlScanner(scanner);
		xmlConfigurationLoadingCommand.setRm(rm);
		try {
			xmlConfigurationLoadingCommand.execute();
		} catch (CommandExecutionException e) {

			Assert.fail(e.getMessage());
		}
	}
	
	public static void initPlatform(ResourcesManager rm, InputStream inputStream)
	{
		LoadXMLInfrastructureCommand xmlLoadingCommand = new LoadXMLInfrastructureCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		try {
			xmlLoadingCommand.setXmlInput(inputStream);
			xmlLoadingCommand.setRm(rm);
			xmlLoadingCommand.execute();
		} catch (CommandExecutionException e) {
			Assert.fail(e.getMessage());

		}
	}
}
