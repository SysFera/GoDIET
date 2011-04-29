package com.sysfera.godiet.command.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.utils.xml.XMLParser;

/**
 * Initialize Godiet configuration given an XML file
 * 
 * @author phi
 * 
 */
public class LoadXMLConfigurationCommand implements Command {
	private ResourcesManager rm;

	private XMLParser xmlScanner;
	private InputStream xmlInput;

	@Override
	public String getDescription() {
		return "Initialize Godiet configuration given an XML file";
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null || xmlScanner == null || xmlInput == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		try {
			GoDietConfiguration goDietConfiguration = xmlScanner
					.buildGodietConfiguration(xmlInput);
			rm.setGoDietConfiguration(goDietConfiguration);
			initUserManager();
		} catch (IOException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (XMLParseException e) {
			throw new CommandExecutionException("XML read error", e);
		}

	}

	/**
	 * Initialize user manager whith key list in the xml desc file
	 */
	private void initUserManager() {
		GoDietConfiguration goDietConfiguration = this.rm
				.getGodietConfiguration().getGoDietConfiguration();
		UserManager um = this.rm.getUserManager();
		if (goDietConfiguration.getUser() != null
				&& goDietConfiguration.getUser().getSsh() != null
				&& goDietConfiguration.getUser().getSsh().getKey() != null) {
			List<Key> sshKeys = goDietConfiguration.getUser().getSsh().getKey();
			for (Key key : sshKeys) {
				SSHKeyManager managedKey = new SSHKeyManager(key);
				um.addManagedSSHKey(managedKey);
			}
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
