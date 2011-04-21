package com.sysfera.godiet.command;

import java.io.IOException;
import java.io.InputStream;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.generated.DietDescription;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.utils.xml.XMLParser;

/**
 * Initialize Godiet configuration given an XML file
 * @author phi
 *
 */
public class LoadXMLConfigurationCommand implements Command{
	private ResourcesManager rm;

	private XMLParser xmlScanner;
	private InputStream xmlInput;
	@Override
	public String getDescription() {
		return "Initialize Godiet configuration given an XML file";
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null || xmlScanner == null || xmlInput == null
				) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		
		try {
			GoDietConfiguration goDietConfiguration = xmlScanner
			.buildGodietConfiguration(xmlInput);
			rm.setGoDietConfiguration(goDietConfiguration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Set the resource manager
	 * 
	 * @param rm
	 *            Resource Manager
	 */
	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

	public void setXmlInput(InputStream xmlInput) {
		this.xmlInput = xmlInput;
	}
	

	public void setXmlScanner(XMLParser xmlScanner) {
		this.xmlScanner = xmlScanner;
	}
}
