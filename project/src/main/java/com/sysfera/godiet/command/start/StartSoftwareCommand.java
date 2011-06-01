package com.sysfera.godiet.command.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.SoftwareManager;

/**
 * 
 * Run all Diet Agents and Seds contains in the data model. Throw an
 * CommandExecutionException if one agents or seds launching failed
 * 
 * @author phi
 * 
 */
public class StartSoftwareCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	private String softwareId;

	public void setSoftwareId(String softwareId) {
		this.softwareId = softwareId;
	}

	@Override
	public String getDescription() {
		return "Launch all forwarders. Begin with forwarders client";
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null || softwareId == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		SoftwareManager soft = this.rm.getDietModel().getManagedSoftware(
				softwareId);
		if (soft == null)
			throw new CommandExecutionException("Unable to find resource "
					+ softwareId);
		
		try {
			soft.start();
		} catch (LaunchException e) {
			throw new CommandExecutionException("Unable to start",e);
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
