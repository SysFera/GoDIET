package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietServiceManager;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;

/**
 * Launch diet services.
 * @author phi
 *
 */
public class CommandLaunchServices implements Command{
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;


	@Override
	public String getDescription() {
		return "Launch diet services. Only OmniNames for now";
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
		List<DietServiceManager> omniNames = rm.getDietModel().getOmninames();
		for (DietServiceManager omniName : omniNames) {
			//omniName.getStateController().start();
		}
	}

	
	public void setRm(ResourcesManager rm)
	{
		this.rm = rm;
	}
	
}
