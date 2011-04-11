package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;

/**
 * 
 * Stop all Agents and Seds contains in the data model. Iterative stopping all
 * MA,LA then Seds.
 * Throw an CommandExecutionException if one ma launching failed
 * 
 * @author phi
 * 
 */
public class StopAgentsCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	@Override
	public String getDescription() {
		return "Launch all forwarders. Begin with forwarders client";
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
		List<DietResourceManaged> mas = rm.getDietModel().getMasterAgents();
		log.debug("Try to stop  " + mas.size() + " Diet Master Agent");
		boolean error = false;
		for (DietResourceManaged ma : mas) {
			try {
				ma.stop();
			} catch (StopException e) {
				error = true;
				log.error("Unable to stop Ma"
						+ ma.getSoftwareDescription().getId());

			}
		}
		if (error) {
			throw new CommandExecutionException(
					"Error when launching a Master Agent");

		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
