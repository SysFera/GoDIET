package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.validators.RuntimeValidator;

public class GodietAbstractFactory {

	private final OmniNamesFactory omniNamesFactory;
	private final ForwardersFactory forwardersFactory;
	private final LocalAgentFactory localAgentFactory;
	private final MasterAgentFactory masterAgentFactory;
	private final SedFactory sedFactory;



	public GodietAbstractFactory(SoftwareController softwareController,
			RuntimeValidator<DietResourceManaged<Forwarder>> forwardersValidator, RuntimeValidator<DietResourceManaged<MasterAgent>> maValidator,
			RuntimeValidator<DietResourceManaged<LocalAgent>> laValidator, RuntimeValidator<DietResourceManaged<Sed>> sedValidator,
			RuntimeValidator<DietServiceManaged<OmniNames>> omniNamesValidator) {
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
			Node clientPluggedOn, DietServiceManaged<OmniNames> omniNamesClient,
			Node serverPluggedOn, DietServiceManaged<OmniNames> omniNamesServer)
			throws DietResourceCreationException {
		return forwardersFactory.create(forwarders, clientPluggedOn, omniNamesClient, serverPluggedOn,
				omniNamesServer);
	}

	public DietResourceManaged<LocalAgent> create(LocalAgent localAgentDescription,Node pluggedOn,
			DietServiceManaged<OmniNames> omniNames) throws DietResourceCreationException {
		return localAgentFactory.create(localAgentDescription,pluggedOn,omniNames);
	}

	public DietResourceManaged<MasterAgent> create(MasterAgent masterAgentDescription,Node pluggedOn,
			DietServiceManaged<OmniNames> omniNames) throws DietResourceCreationException {
		return masterAgentFactory.create(masterAgentDescription,pluggedOn, omniNames);
	}

	// TODO: Why no throws ?
	public DietResourceManaged<Sed> create(Sed sedDescription,Node pluggedOn, DietServiceManaged<OmniNames> omniNames) {
		return sedFactory.create(sedDescription, pluggedOn,omniNames);
	}

	public DietServiceManaged<OmniNames> create(OmniNames omniNamesDescription,Node pluggedOn)
			throws DietResourceCreationException {
		return omniNamesFactory.create(omniNamesDescription,pluggedOn);
	}

}
