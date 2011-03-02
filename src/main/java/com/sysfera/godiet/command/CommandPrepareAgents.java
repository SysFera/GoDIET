package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Model.xml.generated.Forwarder;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;
import com.sysfera.godiet.factories.ForwarderFactory;
import com.sysfera.godiet.managers.ResourcesManager;

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
		List<DietResourceManager> forw = rm.getDietModel()
				.getForwarders();
		prepareAgents(forw);
		List<DietResourceManager> mas= rm.getDietModel()
		.getMasterAgents();
		prepareAgents(mas);
		List<DietResourceManager> las= rm.getDietModel()
		.getLocalAgents();
		prepareAgents(las);
		List<DietResourceManager> seds= rm.getDietModel()
		.getSeds();
		prepareAgents(seds);
		
	}

	private void prepareAgents(List<DietResourceManager>  agents) throws CommandExecutionException {
		try {
			for (DietResourceManager resourceManaged : agents) {
				resourceManaged.prepare();
			}

		} catch (PrepareException e) {
			throw new CommandExecutionException(
					"Error when prepare", e);
		}
	}

}
