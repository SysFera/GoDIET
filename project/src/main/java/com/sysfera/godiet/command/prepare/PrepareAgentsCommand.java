package com.sysfera.godiet.command.prepare;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;

/**
 * Launch diet services.
 * 
 * @author phi
 * 
 */
public class PrepareAgentsCommand implements Command {
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
		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}

		try {
			List<DietResourceManaged<Forwarder>> forw = rm.getDietModel()
					.getForwarders();
			for (DietResourceManaged<? extends Software> resourceManaged : forw) {
				resourceManaged.prepare();
			}

			List<DietResourceManaged<MasterAgent>> mas = rm.getDietModel()
					.getMasterAgents();
			for (DietResourceManaged<? extends Software> resourceManaged : mas) {
				resourceManaged.prepare();
			}

			List<DietResourceManaged<LocalAgent>> las = rm.getDietModel()
					.getLocalAgents();
			for (DietResourceManaged<? extends Software> resourceManaged : las) {
				resourceManaged.prepare();
			}

			List<DietResourceManaged<Sed>> seds = rm.getDietModel().getSeds();
			for (DietResourceManaged<? extends Software> resourceManaged : seds) {
				resourceManaged.prepare();
			}
		} catch (PrepareException e) {
			throw new CommandExecutionException("Error when prepare", e);
		}

	}

}
