package com.sysfera.godiet.command.start;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;

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
		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		List<DietResourceManaged<MasterAgent>> mas = rm.getDietModel().getMasterAgents();
		log.debug("Trying to run  " + mas.size() + " Diet master agent(s)");
		boolean error = false;
		for (DietResourceManaged<MasterAgent> ma : mas) {
			try {
				ma.start();
			} catch (LaunchException e) {
				error = true;
				log.error("Unable to run MA "
						+ ma.getSoftwareDescription().getId());

			}
		}
		List<DietResourceManaged<LocalAgent>> las = rm.getDietModel().getLocalAgents();
		log.debug("Trying to run  " + las.size() + " Diet local agent(s)");
		for (DietResourceManaged<LocalAgent> la : las) {
			try {
				la.start();
			} catch (LaunchException e) {
				error = true;
				log.error("Unable to run LA "
						+ la.getSoftwareDescription().getId());

			}
		}
		List<DietResourceManaged<Sed>> seds = rm.getDietModel().getSeds();
		log.debug("Trying to start  " + seds.size() + " Diet sed(s)");
		for (DietResourceManaged<Sed> sed : seds) {
			try {
				sed.start();
			} catch (LaunchException e) {
				error = true;
				log.error("Unable to start Sed: "
						+ sed.getSoftwareDescription().getId());

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
