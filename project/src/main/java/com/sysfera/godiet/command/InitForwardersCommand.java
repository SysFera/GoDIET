package com.sysfera.godiet.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.factories.ForwardersFactory;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

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

	private RemoteAccess remoteAccess;

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
		if (rm == null || remoteAccess == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		RemoteConfigurationHelper softwareController = new RemoteConfigurationHelper(
				remoteAccess, rm.getGodietConfiguration()
						.getGoDietConfiguration(), rm.getPlatformModel());

		ForwardersFactory forwFactory = new ForwardersFactory(
				softwareController);

		// Search all domains where a software will be launched.
		Set<String> domains = new HashSet<String>();
		List<SoftwareManager> dietResourcesManaged = this.rm.getDietModel()
				.getAllManagedSoftware();
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
					Forwarders forwarders = create(link);

					OmniNames omniNamesServer = this.rm.getDietModel()
							.getOmniName(
									forwarders.getServer().getConfig()
											.getServer().getDomain());
					OmniNames omniNamesClient = this.rm.getDietModel()
							.getOmniName(
									forwarders.getClient().getConfig()
											.getServer().getDomain());
					DietResourceManaged[] forwarderManaged;
					try {
						forwarderManaged = forwFactory.create(forwarders,
								omniNamesClient, omniNamesServer);

						rm.getDietModel().addForwarders(forwarderManaged[0],
								forwarderManaged[1]);
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

	private static String FORWARDERBINARY = "dietForwarder";

	/**
	 * 
	 * 
	 * @param gateway
	 * @param type
	 * @return
	 */
	private Forwarders create(Link link) {
		ObjectFactory factory = new ObjectFactory();
		Forwarders forwarders = factory.createForwarders();

		/*
		 * Create managed client
		 */
		{
			Gateway clientGateway = link.getFrom();

			Forwarder clientForwarder = factory.createForwarder();

			Config clientconfig = factory.createConfig();
			clientconfig.setServer(clientGateway);
			clientconfig.setRemoteBinary(FORWARDERBINARY);
			clientForwarder.setConfig(clientconfig);
			clientForwarder.setId("DietForwarder-" + clientGateway.getId()
					+ "-CLIENT");
			clientForwarder.setType("CLIENT");
			forwarders.setClient(clientForwarder);

		}

		/*
		 * Create managed server
		 */
		{
			Gateway serverGateway = link.getTo();

			Forwarder serverForwarder = factory.createForwarder();

			Config serverconfig = factory.createConfig();
			serverconfig.setServer(serverGateway);
			serverconfig.setRemoteBinary(FORWARDERBINARY);
			serverForwarder.setConfig(serverconfig);
			serverForwarder.setId("DietForwarder-" + serverGateway.getId()
					+ "-SERVER");
			serverForwarder.setType("SERVER");
			forwarders.setServer(serverForwarder);

		}
		return forwarders;
	}

	public void setRemoteAccess(RemoteAccess remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

}
