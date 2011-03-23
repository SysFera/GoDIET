package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.factories.ForwarderFactory;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Forwarder;

/**
 * Launch diet services.
 * 
 * @author phi
 * 
 */
public class CommandPrepareAgents implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	@Override
	public String getDescription() {
		return "Launch diet services. Only OmniNames for now";
	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
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
		List<DietResourceManaged> forw = rm.getDietModel()
				.getForwarders();
		prepareAgents(forw);
		List<DietResourceManaged> mas= rm.getDietModel()
		.getMasterAgents();
		prepareAgents(mas);
		List<DietResourceManaged> las= rm.getDietModel()
		.getLocalAgents();
		prepareAgents(las);
		List<DietResourceManaged> seds= rm.getDietModel()
		.getSeds();
		prepareAgents(seds);
		
	}

	private void prepareAgents(List<DietResourceManaged>  agents) throws CommandExecutionException {
		try {
			for (DietResourceManaged resourceManaged : agents) {
				resourceManaged.prepare();
			}

		} catch (PrepareException e) {
			throw new CommandExecutionException(
					"Error when prepare", e);
		}
	}

}
