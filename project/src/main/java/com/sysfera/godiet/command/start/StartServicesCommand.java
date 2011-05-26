package com.sysfera.godiet.command.start;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.generated.OmniNames;

/**
 * Launch diet services.
 * Throw an CommandExecutionException if one Omninames launching failed
 * @author phi
 * 
 */
public class StartServicesCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	@Override
	public String getDescription() {
		return "Launch diet services. Only OmniNames for now";
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		List<DietServiceManaged<OmniNames>> omniNames = rm.getDietModel().getOmninames();
		log.debug("Trying to run  " +omniNames.size() + " omniNames");
		boolean error = false;
		for (DietServiceManaged<OmniNames> omniName : omniNames) {
			try {
				omniName.start();
			} catch (LaunchException e) {
				log.error("Unable to run omniNames "+omniName.getSoftwareDescription().getId());
				error = true;
			}
		}
		if(error)
		{
			throw new CommandExecutionException("Error when launching an OmniNames");
		}
	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
