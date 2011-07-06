package com.sysfera.godiet.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietServiceException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.generics.StartException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.services.PlatformService;

@Component
public final class PlatformServiceImpl implements PlatformService {

	@Autowired
	private DietManager dietManager;
	@Autowired
	private InfrastructureManager infrastructureManager;

	@Autowired
	private GodietMetaFactory godietMetaFactory;

	@Autowired
	private ConfigurationManager goDietConfiguration;

	@Override
	@PostConstruct
	public void start() throws StartException {
		if (dietManager == null || infrastructureManager == null
				|| goDietConfiguration == null) {
			throw new StartException(getClass().getName(), "0",
					"Unable to init platform", null);
		}
		//
		// godietMetaFactory = new GodietMetaFactory(softwareController,
		// new ForwarderRuntimeValidatorImpl(dietManager),
		// new MasterAgentRuntimeValidatorImpl(dietManager),
		// new LocalAgentRuntimeValidatorImpl(dietManager),
		// new SedRuntimeValidatorImpl(dietManager),
		// new OmniNamesRuntimeValidatorImpl(dietManager));

	}

	@Override
	public void registerMasterAgent(MasterAgent masterAgent)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {

		Resource pluggedOn = infrastructureManager.getResource(masterAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ masterAgent.getConfig().getServer());
		OmniNamesManaged omniNames = getOmniNames(pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException("Unable to find omninames "
					+ masterAgent.getConfig().getServer());
		DietResourceManaged<MasterAgent> masterAgentManaged = godietMetaFactory
				.create(masterAgent, pluggedOn, omniNames);
		dietManager.addMasterAgent(masterAgentManaged);
	}

	@Override
	public void registerLocalAgent(LocalAgent localAgent)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		Resource pluggedOn = infrastructureManager.getResource(localAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ localAgent.getConfig().getServer());
		OmniNamesManaged omniNames = getOmniNames(pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException(
					"Unable to find omninames for"
							+ localAgent.getConfig().getServer());
		DietResourceManaged<LocalAgent> localAgentManaged = godietMetaFactory
				.create(localAgent, pluggedOn, omniNames);
		dietManager.addLocalAgent(localAgentManaged);
	}

	@Override
	public void registerSed(Sed sedAgent) throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		Resource pluggedOn = infrastructureManager.getResource(sedAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ sedAgent.getConfig().getServer());
		OmniNamesManaged omniNames = getOmniNames(pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException(
					"Unable to find omninames for"
							+ sedAgent.getConfig().getServer());
		DietResourceManaged<Sed> sedAgentManaged = godietMetaFactory.create(
				sedAgent, pluggedOn, omniNames);
		dietManager.addSed(sedAgentManaged);
	}

	@Override
	public void registerOmniNames(OmniNames omniNames)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		Resource pluggedOn = infrastructureManager.getResource(omniNames
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ omniNames.getConfig().getServer());
		List<Domain> domains = infrastructureManager.getDomains(pluggedOn);
		if (domains.size() == 0)
			throw new DietResourceCreationException(
					"Unable to find a registered domain for omninames with name: "
							+ omniNames.getId());

		OmniNamesManaged omniNameManaged = godietMetaFactory.create(omniNames,
				pluggedOn);

		omniNameManaged.getDomains().addAll(domains);

		dietManager.addOmniName(omniNameManaged);
	}

	// /**
	// * temporary method
	// */
	// public void buildForwarders()
	// {
	// InitForwardersCommand initForwarder = new InitForwardersCommand();
	// initForwarder.setForwarderFactory(godietMetaFactory);
	// initForwarder.setRm(dietManager);
	// }
	@Override
	public ResourceState getSoftwareController(String id) {
		return this.dietManager.getManagedSoftware(id).getState();
	}

	@Override
	public void unregisterSoftware(Software software)
			throws GoDietServiceException, StopException {

		SoftwareManager<? extends Software> softManaged = dietManager
				.getManagedSoftware(software.getId());
		if (softManaged == null)
			throw new GoDietServiceException("Unable to find :"
					+ software.getId());
		softManaged.stop();

	}

	private OmniNamesManaged getOmniNames(Resource resource)
			throws DietResourceCreationException {
		List<OmniNamesManaged> omniNamesManaged = dietManager.getOmninames();
		List<Domain> domains = infrastructureManager.getDomains(resource);

		for (Domain domain : domains) {
			for (OmniNamesManaged omniNameManaged : omniNamesManaged) {
				if (omniNameManaged.getDomains().contains(domain)) {
					return omniNameManaged;
				}
			}
		}
		throw new DietResourceCreationException(
				"Unable to find the omniNames for resource " + resource.getId());
	}

	@Override
	public void autoLoadForwarders(boolean value) {

	}

	@Override
	public void registerForwarders(Forwarder client, Forwarder server)
			throws DietResourceCreationException, IncubateException {

		Resource clietnPluggedOn = infrastructureManager.getResource(client
				.getConfig().getServer());
		if (clietnPluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ client.getConfig().getServer());
		OmniNamesManaged omniNamesClient = getOmniNames(clietnPluggedOn);
		if (omniNamesClient == null)
			throw new DietResourceCreationException(
					"Unable to find omninames for"
							+ client.getConfig().getServer());

		Resource serverPluggedOn = infrastructureManager.getResource(server
				.getConfig().getServer());
		if (serverPluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ server.getConfig().getServer());
		OmniNamesManaged omniNamesServer = getOmniNames(serverPluggedOn);
		if (omniNamesServer == null)
			throw new DietResourceCreationException(
					"Unable to find omninames for"
							+ server.getConfig().getServer());
		// Check if there is a connection between source and destination
		
		Path path;
		try {
			path = infrastructureManager.findPath(clietnPluggedOn,
					serverPluggedOn);
		} catch (PathException e) {
			throw new DietResourceCreationException(
					"Unable to create forwarders C :" + client.getId() + "S : "
							+ server.getId() + ". No path found between "
							+ clietnPluggedOn.getId() + " and "
							+ serverPluggedOn.getId(), e);
		}
		if (path.getPath().size() != 1) {
			throw new DietResourceCreationException(
					"Unable to create forwarders C :" + client.getId() + "S : "
							+ server.getId() + ". " + clietnPluggedOn.getId()
							+ " and " + serverPluggedOn.getId()
							+ " are not directly connected");
		}
	
		Ssh connection = ((Hop[])path.getPath().toArray(new Hop[0]))[0].getLink();
		Forwarders forwarders = new Forwarders();
		forwarders.setClient(client);
		forwarders.setServer(server);
		DietResourceManaged<Forwarder>[] forwardersManaged = godietMetaFactory
				.create(forwarders, clietnPluggedOn, omniNamesClient,
						serverPluggedOn, omniNamesServer, connection);
		dietManager.addForwarders(forwardersManaged[0], forwardersManaged[1]);

	}
}
