package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Forwarder;

/**
 * 
 * A dummy way to initialize and add forwarders in data model. Create two
 * forwarders for each declared links.
 * 
 * @author phi
 * 
 */
public class StopForwardersCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;


	@Override
	public String getDescription() {
		return "Stop";
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
		List<DietResourceManaged> forwarders = rm.getDietModel().getForwarders();
		log.debug("Try to stop  " + forwarders.size() + " Forwarders");
		for (DietResourceManaged forwarder : forwarders) {
			try {
				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("CLIENT"))
					forwarder.stop();
			} catch (StopException e) {
				log.error("Unable to run Forwarder "
						+ forwarder.getSoftwareDescription().getId());
				throw new CommandExecutionException("Stop client forwarder"
						+ forwarder.getSoftwareDescription().getId()
						+ " failed", e);
			}
		}
		for (DietResourceManaged forwarder : forwarders) {
			try {
				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("SERVER"))
					forwarder.stop();
			} catch (StopException e) {
				log.error("Unable to run Forwarder "
						+ forwarder.getSoftwareDescription().getId());
				throw new CommandExecutionException("Stop server forwarder"
						+ forwarder.getSoftwareDescription().getId()
						+ " failed", e);
			}
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
