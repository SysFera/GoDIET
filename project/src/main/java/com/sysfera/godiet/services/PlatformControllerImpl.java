package com.sysfera.godiet.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietServiceException;
import com.sysfera.godiet.exceptions.generics.StartException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.PlatformManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.DietServiceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

@Component
public final class PlatformControllerImpl implements PlatformController {

	@Autowired
	private DietManager dietManager;
	@Autowired
	private PlatformManager infrastructureManager;

	private GodietMetaFactory godietMetaFactory;

	@Autowired
	private ConfigurationManager goDietConfiguration;

	@Autowired
	private RemoteAccess remoteAccess;
	@Override
	@PostConstruct
	public void start() throws StartException {
		if (dietManager == null || infrastructureManager == null
				|| goDietConfiguration == null) {
			throw new StartException(getClass().getName(), "0",
					"Unable to init platform", null);
		}
		RemoteConfigurationHelper softwareController = new RemoteConfigurationHelper(
				goDietConfiguration, infrastructureManager);
		softwareController.setRemoteAccess(remoteAccess);
		godietMetaFactory = new GodietMetaFactory(softwareController,
				new ForwarderRuntimeValidatorImpl(dietManager),
				new MasterAgentRuntimeValidatorImpl(dietManager),
				new LocalAgentRuntimeValidatorImpl(dietManager),
				new SedRuntimeValidatorImpl(dietManager),
				new OmniNamesRuntimeValidatorImpl(dietManager));

	}

	@Override
	public void registerMasterAgent(MasterAgent masterAgent)
			throws DietResourceCreationException,
			DietResourceValidationException {

		Resource pluggedOn = infrastructureManager.getResource(masterAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ masterAgent.getConfig().getServer());
		OmniNames omniNames = InfrastructureUtil.getOmniNames(dietManager,
				pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException("Unable to find omninames "
					+ masterAgent.getConfig().getServer());
		DietResourceManaged masterAgentManaged = godietMetaFactory.create(
				masterAgent, pluggedOn, omniNames);
		dietManager.addMasterAgent(masterAgentManaged);
	}

	@Override
	public void registerLocalAgent(LocalAgent localAgent)
			throws DietResourceCreationException,
			DietResourceValidationException {
		Resource pluggedOn = infrastructureManager.getResource(localAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ localAgent.getConfig().getServer());
		OmniNames omniNames = InfrastructureUtil.getOmniNames(dietManager,
				pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException("Unable to find omninames for"
					+ localAgent.getConfig().getServer());
		DietResourceManaged localAgentManaged = godietMetaFactory.create(
				localAgent, pluggedOn, omniNames);
		dietManager.addLocalAgent(localAgentManaged);
	}

	@Override
	public void registerSed(Sed sedAgent) throws DietResourceCreationException,
			DietResourceValidationException {
		Resource pluggedOn = infrastructureManager.getResource(sedAgent
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ sedAgent.getConfig().getServer());
		OmniNames omniNames = InfrastructureUtil.getOmniNames(dietManager,
				pluggedOn);
		if (omniNames == null)
			throw new DietResourceCreationException("Unable to find omninames for"
					+ sedAgent.getConfig().getServer());
		DietResourceManaged sedAgentManaged = godietMetaFactory.create(
				sedAgent, pluggedOn, omniNames);
		dietManager.addSed(sedAgentManaged);
	}

	@Override
	public void registerOmniNames(OmniNames omniNames)
			throws DietResourceCreationException,
			DietResourceValidationException {
		Resource pluggedOn = infrastructureManager.getResource(omniNames
				.getConfig().getServer());
		if (pluggedOn == null)
			throw new DietResourceCreationException(
					"Unable to find the physical resource "
							+ omniNames.getConfig().getServer());
		Domain domain = infrastructureManager.getDomain(omniNames.getConfig()
				.getServer());
		if (domain == null)
			throw new DietResourceCreationException("Unable to find a registered domain for omninames with name: "
					+ omniNames.getId());

		DietServiceManaged OmniNamesAgentManaged = godietMetaFactory.create(omniNames, pluggedOn);
		OmniNamesAgentManaged.setDomain(domain);
		dietManager.addOmniName(OmniNamesAgentManaged);
	}

//	/**
//	 * temporary method
//	 */
//	public void buildForwarders()
//	{
//		InitForwardersCommand initForwarder = new InitForwardersCommand();
//		initForwarder.setForwarderFactory(godietMetaFactory);
//		initForwarder.setRm(dietManager);
//	}
	@Override
	public ResourceState getSoftwareController(String id) {
		return this.dietManager.getManagedSoftware(id).getState();
	}

	@Override
	public void unregisterSoftware(Software software)
			throws GoDietServiceException, StopException {

		SoftwareManager softManaged = dietManager.getManagedSoftware(software
				.getId());
		if (softManaged == null)
			throw new GoDietServiceException("Unable to find :"
					+ software.getId());
		softManaged.stop();

	}

}
