package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.validators.RuntimeValidator;

public class GodietMetaFactory {

	private final OmniNamesFactory omniNamesFactory;
	private final ForwardersFactory forwardersFactory;
	private final LocalAgentFactory localAgentFactory;
	private final MasterAgentFactory masterAgentFactory;
	private final SedFactory sedFactory;




	public GodietMetaFactory(SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<Forwarder>> forwardersValidator, RuntimeValidator<DietResourceManaged<MasterAgent>> maValidator,
			RuntimeValidator<DietResourceManaged<LocalAgent>> laValidator, RuntimeValidator<DietResourceManaged<Sed>> sedValidator,
			RuntimeValidator<OmniNamesManaged> omniNamesValidator) {
		this.forwardersFactory = new ForwardersFactory(softwareController,
				forwardersValidator);
		this.localAgentFactory = new LocalAgentFactory(softwareController,
				laValidator);
		this.masterAgentFactory = new MasterAgentFactory(softwareController,
				maValidator);
		this.sedFactory = new SedFactory(softwareController, sedValidator);
		this.omniNamesFactory = new OmniNamesFactory(softwareController,
				omniNamesValidator);
	}




	public DietResourceManaged<Forwarder>[] create(Forwarders forwarders,
			Node clientPluggedOn, OmniNamesManaged omniNamesClient,
			Node serverPluggedOn, OmniNamesManaged omniNamesServer)
			throws DietResourceCreationException, IncubateException {
		return forwardersFactory.create(forwarders, clientPluggedOn, omniNamesClient, serverPluggedOn,
				omniNamesServer);
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
