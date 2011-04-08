package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietServiceManager;

/**
 * Launch diet services.
 * 
 * @author phi
 * 
 */
public class StopServicesCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	@Override
	public String getDescription() {
		return "Stop diet services. Only OmniNames for now";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		List<DietServiceManager> omniNames = rm.getDietModel().getOmninames();
		log.debug("Try to stop  " +omniNames.size() + " omniNames");
		for (DietServiceManager omniName : omniNames) {
			try {
				omniName.stop();
			} catch (StopException e) {
				log.error("Unable to stop omniNames "+omniName.getSoftwareDescription().getId());
				throw new CommandExecutionException("Stop "+ omniName.getSoftwareDescription().getId()+ " failed",e);
			}
		}
	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
