package com.sysfera.godiet.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.factories.ForwardersFactory;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.Link;

/**
 * 
 * A dummy way to initialize and add forwarders in data model. Create two
 * forwarders for each declared links.
 * 
 * @author phi
 * 
 */
public class InitForwardersCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;

	@Override
	public String getDescription() {
		return "Add forwarders in data model if needed (In each domains where there are Diet Services Agents and Seds";
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

		// Create a list of all domains where are Diet Services Agents and Seds.
		Set<String> domains = new HashSet<String>();
		List<SoftwareManager> dietResourcesManaged = this.rm.getDietModel()
				.getAllDietSoftwareManaged();
		for (SoftwareManager softwareManaged : dietResourcesManaged) {
			domains.add(softwareManaged.getSoftwareDescription().getConfig()
					.getServer().getDomain().getLabel());
		}
		List<Link> links = rm.getPlatformModel().getLinks();
		if (links != null) {
			for (Link link : links) {
				if (domains.contains(link.getFrom().getDomain().getLabel())
						&& domains
								.contains(link.getTo().getDomain().getLabel())) {
					Forwarders forwarders= ForwardersFactory.create(link);
				

					try {
						rm.getDietModel().addForwarders(forwarders);

					} catch (DietResourceCreationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

}
