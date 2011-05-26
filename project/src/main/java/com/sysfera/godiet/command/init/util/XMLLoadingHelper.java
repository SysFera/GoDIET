package com.sysfera.godiet.command.init.util;

import java.io.InputStream;

import com.sysfera.godiet.command.xml.LoadXMLConfigurationCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.command.xml.LoadXMLInfrastructureCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.factories.GodietAbstractFactory;
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

	public static void initInfrastructure(ResourcesManager rm, InputStream inputStream) throws CommandExecutionException {
		LoadXMLInfrastructureCommand xmlLoadingCommand = new LoadXMLInfrastructureCommand();

		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.execute();
	}

	public static void initDiet(ResourcesManager rm, InputStream inputStream,GodietAbstractFactory abstractFactory) throws CommandExecutionException {
		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setAbstractFactory(abstractFactory);
		xmlLoadingCommand.execute();
	}
}
