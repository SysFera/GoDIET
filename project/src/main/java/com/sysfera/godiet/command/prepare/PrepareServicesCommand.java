package com.sysfera.godiet.command.prepare;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietServiceManaged;

/**
 * Launch diet services.
 * 
 * @author phi
 * 
 */
public class PrepareServicesCommand implements Command {
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
		List<DietServiceManaged> omniNames = rm.getDietModel().getOmninames();
		log.debug("Will prepare  " +omniNames.size() + " omniNames");
		for (DietServiceManaged omniName : omniNames) {
			try {
				omniName.prepare();
			} catch (PrepareException e) {
				log.error("Error when prepare omniNames: " + omniName.getSoftwareDescription().getId(),e);
			}
		}
	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
