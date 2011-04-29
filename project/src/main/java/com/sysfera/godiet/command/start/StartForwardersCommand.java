package com.sysfera.godiet.command.start;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;

/**
 * 
 * Run all Diet forwarders contains in the data model. Iterative running all
 * Server then Client forwarders.
 * Throw an CommandExecutionException if one forwarder launching failed
 * @see Forwarders
 * @author phi
 * 
 */
public class StartForwardersCommand implements Command {
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
		List<DietResourceManaged> forwarders = rm.getDietModel()
				.getForwarders();
		log.debug("Trying to run  " + forwarders.size() + " Forwarders");
		boolean error = false;
		for (DietResourceManaged forwarder : forwarders) {
			try {
				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("SERVER")) {
					forwarder.start();
                                }
			} catch (LaunchException e) {
				log.error("Unable to run Forwarder "
						+ forwarder.getSoftwareDescription().getId());
				error = true;
			}
		}

		for (DietResourceManaged forwarder : forwarders) {
			try {
				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("CLIENT")) {
					forwarder.start();
                                }
			} catch (LaunchException e) {
				log.error("Unable to run Forwarder "
						+ forwarder.getSoftwareDescription().getId());
				error = true;

			}
		}

		if (error) {
			throw new CommandExecutionException(
					"Error when try to running a client forwarder");
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
