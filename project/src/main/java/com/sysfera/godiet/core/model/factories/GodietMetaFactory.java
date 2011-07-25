package com.sysfera.godiet.core.model.factories;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.Forwarders;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Ssh;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.managers.InfrastructureManager;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.core.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.core.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.core.model.validators.OmniNamesRuntimeValidatorImpl;

/**
 * Just a class to initialize and store the factories. All business code are
 * delegate to the dedicate factory
 * 
 * @author phi
 * 
 */
@Component
public class GodietMetaFactory {

	private OmniNamesFactory omniNamesFactory;
	private ForwardersFactory forwardersFactory;
	private LocalAgentFactory localAgentFactory;
	private MasterAgentFactory masterAgentFactory;
	@Autowired
	private SedFactory sedFactory;

	@Autowired
	private ConfigurationFileBuilderService configurationFileBuilderService;

	@Autowired
	private InfrastructureManager infrastructureManager;

	@Autowired
	private DietManager dietManager;
	@Autowired
	private SoftwareController softwareController;

	@PostConstruct
	public void init() {
		this.forwardersFactory = new ForwardersFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietManager),
				configurationFileBuilderService);
		this.localAgentFactory = new LocalAgentFactory(softwareController,
				new LocalAgentRuntimeValidatorImpl(dietManager),
				configurationFileBuilderService);
		this.masterAgentFactory = new MasterAgentFactory(softwareController,
				new MasterAgentRuntimeValidatorImpl(dietManager),
				configurationFileBuilderService);
		this.omniNamesFactory = new OmniNamesFactory(softwareController,
				new OmniNamesRuntimeValidatorImpl(dietManager),
				configurationFileBuilderService);
		this.omniNamesFactory.setInfrastructureManager(infrastructureManager);
	}

	public DietResourceManaged<Forwarder>[] create(Forwarders forwarders,
			Resource clientPluggedOn, OmniNamesManaged omniNamesClient,
			Resource serverPluggedOn, OmniNamesManaged omniNamesServer,
			Ssh connection) throws DietResourceCreationException,
			IncubateException {
		return forwardersFactory.create(forwarders, clientPluggedOn,
				omniNamesClient, serverPluggedOn, omniNamesServer, connection);
	}

	public DietResourceManaged<LocalAgent> create(
			LocalAgent localAgentDescription, Resource pluggedOn,
			OmniNamesManaged omniNames) throws DietResourceCreationException,
			IncubateException {
		return localAgentFactory.create(localAgentDescription, pluggedOn,
				omniNames);
	}

	public DietResourceManaged<MasterAgent> create(
			MasterAgent masterAgentDescription, Resource pluggedOn,
			OmniNamesManaged omniNames) throws DietResourceCreationException,
			IncubateException {
		return masterAgentFactory.create(masterAgentDescription, pluggedOn,
				omniNames);
	}

	// TODO: Why do not throw dietresourcecreationexception ?
	public DietResourceManaged<Sed> create(Sed sedDescription,
			Resource pluggedOn, OmniNamesManaged omniNames)
			throws IncubateException {
		return sedFactory.create(sedDescription, pluggedOn, omniNames);
	}

	public OmniNamesManaged create(OmniNames omniNamesDescription,
			Resource pluggedOn) throws DietResourceCreationException,
			IncubateException {
		return omniNamesFactory.create(omniNamesDescription, pluggedOn);
	}

}
