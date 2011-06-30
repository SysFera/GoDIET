package com.sysfera.godiet.model.factories;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;

@Component
public class GodietMetaFactory {

	private  OmniNamesFactory omniNamesFactory;
	private  ForwardersFactory forwardersFactory;
	private  LocalAgentFactory localAgentFactory;
	private  MasterAgentFactory masterAgentFactory;
	private  SedFactory sedFactory;


	@Autowired
	private InfrastructureManager infrastructureManager;
	
	@Autowired
	private DietManager dietManager;
	@Autowired
	private SoftwareController softwareController;

	@PostConstruct
	public void init()
	{
		this.forwardersFactory = new ForwardersFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietManager) );
		this.localAgentFactory = new LocalAgentFactory(softwareController,
				new LocalAgentRuntimeValidatorImpl(dietManager));
		this.masterAgentFactory = new MasterAgentFactory(softwareController,
				new MasterAgentRuntimeValidatorImpl(dietManager));
		this.sedFactory = new SedFactory(softwareController, new SedRuntimeValidatorImpl(dietManager));
		this.omniNamesFactory = new OmniNamesFactory(softwareController,
				new OmniNamesRuntimeValidatorImpl(dietManager));
		this.omniNamesFactory.setInfrastructureManager(infrastructureManager);
	}


	public DietResourceManaged<Forwarder>[] create(Forwarders forwarders,
			Resource clientPluggedOn, OmniNamesManaged omniNamesClient,
			Resource serverPluggedOn, OmniNamesManaged omniNamesServer, Ssh connection)
			throws DietResourceCreationException, IncubateException {
		return forwardersFactory.create(forwarders, clientPluggedOn, omniNamesClient, serverPluggedOn,
				omniNamesServer,connection);
	}

	public DietResourceManaged<LocalAgent> create(LocalAgent localAgentDescription,Resource pluggedOn,
			OmniNamesManaged omniNames) throws DietResourceCreationException, IncubateException {
		return localAgentFactory.create(localAgentDescription,pluggedOn,omniNames);
	}

	public DietResourceManaged<MasterAgent> create(MasterAgent masterAgentDescription,Resource pluggedOn,
			OmniNamesManaged omniNames) throws DietResourceCreationException, IncubateException {
		return masterAgentFactory.create(masterAgentDescription,pluggedOn, omniNames);
	}

	// TODO: Why no throws ?
	public DietResourceManaged<Sed> create(Sed sedDescription,Resource pluggedOn, OmniNamesManaged omniNames) throws IncubateException {
		return sedFactory.create(sedDescription, pluggedOn,omniNames);
	}

	public OmniNamesManaged create(OmniNames omniNamesDescription,Resource pluggedOn)
			throws DietResourceCreationException, IncubateException {
		return omniNamesFactory.create(omniNamesDescription,pluggedOn);
	}

}
