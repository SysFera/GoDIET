package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
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
			RuntimeValidator forwardersValidator, RuntimeValidator maValidator,
			RuntimeValidator laValidator, RuntimeValidator sedValidator,
			RuntimeValidator omniNamesValidator) {
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

	public DietResourceManaged[] create(Forwarders forwarders,Resource pluggedOnClient,Resource pluggedOnServer,
			OmniNames omniNamesClient, OmniNames omniNamesServer)
			throws DietResourceCreationException {
		return forwardersFactory.create(forwarders, pluggedOnClient,pluggedOnServer,omniNamesClient,
				omniNamesServer);
	}

	public DietResourceManaged create(LocalAgent localAgentDescription,Resource pluggedOn,
			OmniNames omniNames) throws DietResourceCreationException {
		return localAgentFactory.create(localAgentDescription, pluggedOn,omniNames);
	}

	public DietResourceManaged create(MasterAgent masterAgentDescription,Resource pluggedOn,
			OmniNames omniNames) throws DietResourceCreationException {
		return masterAgentFactory.create(masterAgentDescription,pluggedOn, omniNames);
	}

	
	public DietResourceManaged create(Sed sedDescription,Resource pluggedOn, OmniNames omniNames) throws DietResourceCreationException {
		return sedFactory.create(sedDescription, pluggedOn,omniNames);
	}

	public DietServiceManaged create(OmniNames omniNamesDescription,Resource pluggedOn)
			throws DietResourceCreationException {
		return omniNamesFactory.create(omniNamesDescription,pluggedOn);
	}

}
