package com.sysfera.godiet.command.stop;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.generated.OmniNames;

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

		if (rm == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		List<DietServiceManaged<OmniNames>> omniNames = rm.getDietModel().getOmninames();
		log.debug("Try to stop  " +omniNames.size() + " OmniNames");
		boolean error = false;
		for (DietServiceManaged<OmniNames> omniName : omniNames) {
			try {
				omniName.stop();
			} catch (StopException e) {
				log.error("Unable to stop omniNames "+omniName.getSoftwareDescription().getId());
				error = true;
			}
		}
		if(error){
			throw new CommandExecutionException("Error when stopping OmniNames");
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
