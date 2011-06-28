package com.sysfera.godiet.command.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.factories.GodietMetaFactory;

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
	private GodietMetaFactory forwFactory;

	@Override
	public String getDescription() {
		return "Add forwarders in data model if needed (In each domains where there are Diet Services Agents and Seds";
	}

	public void setForwarderFactory(GodietMetaFactory forwFactory) {
		this.forwFactory = forwFactory;
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null || forwFactory == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		//FIXME
		throw new IllegalAccessError("Not implemented");
		// Search all domains where a software will be launched.
//		Set<String> domains = new HashSet<String>();
//		List<SoftwareManager<? extends Software>> dietResourcesManaged = this.rm.getDietModel()
//				.getAllManagedSoftware();
//		for (SoftwareManager<? extends Software> softwareManaged : dietResourcesManaged) {
//			domains.add(softwareManaged.getSoftwareDescription().getConfig()
//					.getServerNode().getDomain().getLabel());
//		}
//		List<Link> links = rm.getInfrastructureModel().getLinks();
//		boolean error = false;
//		if (links != null) {
//			for (Link link : links) {
//				if (domains.contains(link.getFrom().getDomain().getLabel())
//						&& domains
//								.contains(link.getTo().getDomain().getLabel())) {
//					Forwarders forwarders = create(link);
//
//					OmniNames omniNamesServer = this.rm.getDietModel()
//							.getOmniName(
//									forwarders.getServer().getConfig()
//											.getServerNode().getDomain());
//					OmniNames omniNamesClient = this.rm.getDietModel()
//							.getOmniName(
//									forwarders.getClient().getConfig()
//											.getServerNode().getDomain());
//					DietResourceManaged[] forwarderManaged;
//					try {
//						forwarderManaged = forwFactory.create(forwarders,
//								omniNamesClient, omniNamesServer);
//
//						rm.getDietModel().addForwarders(forwarderManaged[0],
//								forwarderManaged[1]);
//					} catch (DietResourceCreationException e) {
//						error = true;
//					}
//
//				}
//			}
//			if(error) throw new CommandExecutionException("Error when init forwarders");
//		}

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
//	private Forwarders create(Link link) {
//		ObjectFactory factory = new ObjectFactory();
//		Forwarders forwarders = factory.createForwarders();
//
//		/*
//		 * Create managed client
//		 */
//		{
//			Gateway clientGateway = link.getFrom();
//
//			Forwarder clientForwarder = factory.createForwarder();
//
//			Config clientconfig = factory.createConfig();
//			clientconfig.setServerNode(clientGateway);
//			clientconfig.setRemoteBinary(FORWARDERBINARY);
//			clientForwarder.setConfig(clientconfig);
//			clientForwarder.setId("DietForwarder-" + clientGateway.getId()
//					+ "-CLIENT");
//			clientForwarder.setType("CLIENT");
//			forwarders.setClient(clientForwarder);
//
//		}
//
//		/*
//		 * Create managed server
//		 */
//		{
//			Gateway serverGateway = link.getTo();
//
//			Forwarder serverForwarder = factory.createForwarder();
//
//			Config serverconfig = factory.createConfig();
//			serverconfig.setServerNode(serverGateway);
//			serverconfig.setRemoteBinary(FORWARDERBINARY);
//			serverForwarder.setConfig(serverconfig);
//			serverForwarder.setId("DietForwarder-" + serverGateway.getId()
//					+ "-SERVER");
//			serverForwarder.setType("SERVER");
//			forwarders.setServer(serverForwarder);
//
//		}
//		return forwarders;
//	}

}
