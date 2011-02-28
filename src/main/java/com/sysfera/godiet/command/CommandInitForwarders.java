package com.sysfera.godiet.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Model.xml.generated.Forwarder;
import com.sysfera.godiet.Model.xml.generated.Link;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.factories.ForwarderFactory;
import com.sysfera.godiet.managers.ResourcesManager;

/**
 * 
 * A dummy way to to initialize and add forwarders in data model. 
 * Create  two forwarders for each declared links.
 * @author phi
 *
 */
public class CommandInitForwarders implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	
	private ResourcesManager rm;
	private final ForwarderFactory forwarderFactory;
	
	public CommandInitForwarders() {
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

		List<Link> links = rm.getPlatformModel().getLinks();
		if(links != null)
		{
			for (Link link : links) {
				Forwarder forwarderClient = forwarderFactory.create(link.getFrom(),ForwarderFactory.ForwarderType.CLIENT);
				Forwarder forwarderServer = forwarderFactory.create(link.getTo(),ForwarderFactory.ForwarderType.SERVER);
				DietResourceManager resForwarderClient = new DietResourceManager();
				resForwarderClient.setDietAgent(forwarderClient);
				DietResourceManager resForwarderServer = new DietResourceManager();
				resForwarderServer.setDietAgent(forwarderServer);
				rm.getDietModel().addForwarder(resForwarderClient);
				rm.getDietModel().addForwarder(resForwarderServer);
			}
		}
		
	}
	
	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}