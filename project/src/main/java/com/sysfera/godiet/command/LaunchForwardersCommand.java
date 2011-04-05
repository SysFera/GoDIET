package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManager;
import com.sysfera.godiet.model.factories.ForwarderFactory;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Link;

/**
 * 
 * A dummy way to initialize and add forwarders in data model. Create two
 * forwarders for each declared links.
 * 
 * @author phi
 * 
 */
public class LaunchForwardersCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private final ForwarderFactory forwarderFactory;

	public LaunchForwardersCommand() {
		this.forwarderFactory = new ForwarderFactory();
	}

	@Override
	public String getDescription() {
		return "A dummy way to to initialize and add forwarders in data model";
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
		log.debug("Try to launch  " +forwarders.size() + " omniNames");
		for (DietResourceManaged forwarder : forwarders) {
			try {
				forwarder.start();
			} catch (LaunchException e) {
				log.error("Unable to run Forwarder "+forwarder.getManagedSoftwareDescription().getId());
				throw new CommandExecutionException("Launch "+ forwarder.getSoftwareDescription().getId()+ " failed",e);
			}
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
