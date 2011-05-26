package com.sysfera.godiet.command.stop;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;

/**
 * 
 * Stop all Agents and Seds contains in the data model. Iterative stopping all
 * MA,LA then Seds. Throw an CommandExecutionException if one ma launching
 * failed
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

		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		boolean error = false;

		List<DietResourceManaged<Sed>> seds = rm.getDietModel().getSeds();
		log.debug("Trying to stop  " + seds.size() + " Sed Agent");
		for (DietResourceManaged<Sed> sed : seds) {
			try {
				sed.stop();
			} catch (StopException e) {
				error = true;
				log.error("Unable to stop Sed: "
						+ sed.getSoftwareDescription().getId());

			}

			List<DietResourceManaged<LocalAgent>> las = rm.getDietModel().getLocalAgents();
			log.debug("Trying to stop  " + las.size() + " Diet Local Agent");
			for (DietResourceManaged<LocalAgent> la : las) {
				try {
					la.stop();
				} catch (StopException e) {
					error = true;
					log.error("Unable to stop LA"
							+ la.getSoftwareDescription().getId());

				}
			}

			List<DietResourceManaged<MasterAgent>> mas = rm.getDietModel().getMasterAgents();
			log.debug("Trying to stop  " + mas.size() + " Diet Master Agent");
			for (DietResourceManaged<MasterAgent> ma : mas) {
				try {
					ma.stop();
				} catch (StopException e) {
					error = true;
					log.error("Unable to stop MA"
							+ ma.getSoftwareDescription().getId());

				}
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
