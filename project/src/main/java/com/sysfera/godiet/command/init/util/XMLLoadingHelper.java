package com.sysfera.godiet.command.init.util;

import java.io.InputStream;

import com.sysfera.godiet.command.xml.LoadXMLConfigurationCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.command.xml.LoadXMLPlatformCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class XMLLoadingHelper  {
	private static XMLParser scanner = new XmlScannerJaxbImpl();

	public static void initConfig(ResourcesManager rm, InputStream inputStream) throws CommandExecutionException {

		LoadXMLConfigurationCommand xmlConfigurationLoadingCommand = new LoadXMLConfigurationCommand();

		xmlConfigurationLoadingCommand.setXmlInput(inputStream);
		xmlConfigurationLoadingCommand.setXmlScanner(scanner);
		xmlConfigurationLoadingCommand.setRm(rm);

		xmlConfigurationLoadingCommand.execute();

	}

	public static void initPlatform(ResourcesManager rm, InputStream inputStream) throws CommandExecutionException {
		LoadXMLPlatformCommand xmlLoadingCommand = new LoadXMLPlatformCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.execute();
	}

	public static void initDiet(ResourcesManager rm, InputStream inputStream,GodietMetaFactory abstractFactory) throws CommandExecutionException {
		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setAbstractFactory(abstractFactory);
		xmlLoadingCommand.execute();
	}
}
