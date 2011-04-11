package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;

/**
 * 
 * Run all Diet Agents and Seds contains in the data model.
 * Throw an CommandExecutionException if one agents or seds launching failed
 * 
 * @author phi
 * 
 */
public class StartAgentsCommand implements Command {
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
		log.debug("Try to launch  " + mas.size() + " Diet Master Agent");
		boolean error = false;
		for (DietResourceManaged ma : mas) {
			try {
				ma.start();
			} catch (LaunchException e) {
				error = true;
				log.error("Unable to run Forwarder "
						+ ma.getSoftwareDescription().getId());

			}
		}
		log.debug("TODO implement start and stop for LA and SEDS");
		if (error) {
			throw new CommandExecutionException(
					"Error when launching a Master Agent");

		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
