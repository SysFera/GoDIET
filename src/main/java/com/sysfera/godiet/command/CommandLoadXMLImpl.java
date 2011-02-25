package com.sysfera.godiet.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Utils.XmlScanner;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;

/**
 * Initialize resource manager with the given XML description file
 * 
 * @author phi
 * 
 */
public class CommandLoadXMLImpl implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private XmlScanner xmlScanner;

	@Override
	public String getDescription() {
		return "Initialize resource manager with XML file";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (rm == null || xmlScanner == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
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

	/**
	 * Set the Xml Scanner
	 * 
	 * @param xmlScanner
	 */
	public void setXmlScanner(XmlScanner xmlScanner) {
		this.xmlScanner = xmlScanner;
	}

}
