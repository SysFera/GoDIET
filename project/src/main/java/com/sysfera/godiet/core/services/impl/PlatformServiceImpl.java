package com.sysfera.godiet.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietServiceException;
import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.generics.StartException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.Forwarders;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.generated.Ssh;
import com.sysfera.godiet.common.rmi.model.SoftwareInterfaceSerializable;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.core.managers.ConfigurationManager;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.managers.InfrastructureManager;
import com.sysfera.godiet.core.managers.topology.infrastructure.Path;
import com.sysfera.godiet.core.managers.topology.infrastructure.Path.Hop;
import com.sysfera.godiet.core.model.factories.GodietMetaFactory;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.core.model.softwares.SoftwareManager;

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
		if (dietManager == null || infrastructureManager == null) {
			// || goDietConfiguration == null) {
			throw new StartException(getClass().getName(), "0",
					"Unable to init platform", null);
		}
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
	// @Override
	// public ResourceState getSoftwareController(String id) {
	// return this.dietManager.getManagedSoftware(id).getState();
	// }

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
			throws DietResourceCreationException, IncubateException, GraphDataException {

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

		Ssh connection = ((Hop[]) path.getPath().toArray(new Hop[0]))[0]
				.getLink();
		Forwarders forwarders = new Forwarders();
		forwarders.setClient(client);
		forwarders.setServer(server);
		DietResourceManaged<Forwarder>[] forwardersManaged = godietMetaFactory
				.create(forwarders, clietnPluggedOn, omniNamesClient,
						serverPluggedOn, omniNamesServer, connection);
		dietManager.addForwarders(forwardersManaged[0], forwardersManaged[1]);

	}

	@Override
	public List<SoftwareInterface<Sed>> getSeds() {
		List<SoftwareInterface<Sed>> copy = new ArrayList<SoftwareInterface<Sed>>();
		for (SoftwareInterface<Sed> softwareInterface : this.dietManager
				.getSeds()) {
			copy.add(new SoftwareInterfaceSerializable<Sed>(softwareInterface));
		}
		return copy;
	}

	@Override
	public List<SoftwareInterface<MasterAgent>> getMasterAgents() {
		List<SoftwareInterface<MasterAgent>> copy = new ArrayList<SoftwareInterface<MasterAgent>>();
		for (SoftwareInterface<MasterAgent> softwareInterface : this.dietManager
				.getMasterAgents()) {
			copy.add(new SoftwareInterfaceSerializable<MasterAgent>(
					softwareInterface));
		}
		return copy;
	}

	@Override
	public List<SoftwareInterface<LocalAgent>> getLocalAgents() {
		List<SoftwareInterface<LocalAgent>> copy = new ArrayList<SoftwareInterface<LocalAgent>>();
		for (SoftwareInterface<LocalAgent> softwareInterface : this.dietManager
				.getLocalAgents()) {
			copy.add(new SoftwareInterfaceSerializable<LocalAgent>(
					softwareInterface));
		}
		return copy;
	}

	@Override
	public List<SoftwareInterface<Forwarder>> getForwarders() {
		try {
			List<SoftwareInterface<Forwarder>> copy = new ArrayList<SoftwareInterface<Forwarder>>();
			for (SoftwareInterface<Forwarder> softwareInterface : this.dietManager
					.getForwarders()) {
				copy.add(new SoftwareInterfaceSerializable<Forwarder>(
						softwareInterface));
			}
			return copy;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public List<SoftwareInterface<Forwarder>> getForwardersServer() {
		List<SoftwareInterface<Forwarder>> forwardersServer = new ArrayList<SoftwareInterface<Forwarder>>();
		List<DietResourceManaged<Forwarder>> forwarders = this.dietManager
				.getForwarders();
		for (DietResourceManaged<Forwarder> forwarder : forwarders) {
			if (forwarder.getSoftwareDescription().getType().equals("SERVER")) {
				forwardersServer
						.add(new SoftwareInterfaceSerializable<Forwarder>(
								forwarder));
			}
		}

		return forwardersServer;
	}

	@Override
	public List<SoftwareInterface<Forwarder>> getForwardersClient() {
		List<SoftwareInterface<Forwarder>> forwardersClient = new ArrayList<SoftwareInterface<Forwarder>>();
		List<DietResourceManaged<Forwarder>> forwarders = this.dietManager
				.getForwarders();
		for (DietResourceManaged<Forwarder> forwarder : forwarders) {
			if (forwarder.getSoftwareDescription().getType().equals("CLIENT")) {
				forwardersClient
						.add(new SoftwareInterfaceSerializable<Forwarder>(
								forwarder));
			}
		}
		return forwardersClient;
	}

	@Override
	public List<SoftwareInterface<OmniNames>> getOmninames() {
		List<SoftwareInterface<OmniNames>> copy = new ArrayList<SoftwareInterface<OmniNames>>();
		for (SoftwareInterface<OmniNames> softwareInterface : this.dietManager
				.getOmninames()) {
			copy.add(new SoftwareInterfaceSerializable<OmniNames>(
					softwareInterface));
		}
		return copy;
	}

	@Override
	public List<SoftwareInterface<? extends Software>> getAllSoftwares() {
		List<SoftwareInterface<? extends Software>> copy = new ArrayList<SoftwareInterface<? extends Software>>();
		for (SoftwareInterface<? extends Software> softwareInterface : this.dietManager
				.getAllManagedSoftware()) {
			copy.add(new SoftwareInterfaceSerializable(softwareInterface));
		}
		return copy;
	}

	@Override
	public SoftwareInterface<? extends Software> getManagedSoftware(String id) {
		return this.dietManager.getManagedSoftware(id);
	}

	@Override
	public void startSoftware(String id) throws LaunchException {
		SoftwareManager<? extends Software> s = this.dietManager
				.getManagedSoftware(id);
		if (s == null)
			throw new LaunchException("Unable to find " + id);
		s.start();
	}

	@Override
	public void prepareSoftware(String id) throws PrepareException {
		SoftwareManager<? extends Software> s = this.dietManager
				.getManagedSoftware(id);
		if (s == null)
			throw new PrepareException("Unable to find " + id);
		s.prepare();

	}

	@Override
	public void stopSoftware(String id) throws StopException {
		SoftwareManager<? extends Software> s = this.dietManager
				.getManagedSoftware(id);
		if (s == null)
			throw new StopException("Unable to find " + id);
		s.stop();
	}



}
