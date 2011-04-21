package com.sysfera.godiet.command.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.factories.LocalAgentFactory;
import com.sysfera.godiet.model.factories.MasterAgentFactory;
import com.sysfera.godiet.model.factories.OmniNamesFactory;
import com.sysfera.godiet.model.factories.SedFactory;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Diet;
import com.sysfera.godiet.model.generated.DietInfrastructure;
import com.sysfera.godiet.model.generated.DietServices;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XMLParser;

/**
 * Initialize resource manager with the given XML description file. Load
 * configuration. Load physical platform. Load diet agents. Dummy diet forwarder
 * loading ( 2 diet forwarder for each declared link )
 * 
 * @author phi
 * 
 */
public class LoadXMLDietCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private XMLParser xmlScanner;
	private InputStream xmlInput;

	private MasterAgentFactory maFactory;
	private LocalAgentFactory laFactory;
	private SedFactory sedFactory;
	private OmniNamesFactory omFactory;

	private RemoteAccess remoteAccess;

	@Override
	public String getDescription() {
		return "Initialize resource manager given a  XML data source";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (rm == null
				|| rm.getGodietConfiguration().getGoDietConfiguration() == null
				|| xmlScanner == null || xmlInput == null
				|| remoteAccess == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		RemoteConfigurationHelper softwareController = new RemoteConfigurationHelper(
				remoteAccess, rm.getGodietConfiguration()
						.getGoDietConfiguration());
		this.maFactory = new MasterAgentFactory(softwareController);
		this.laFactory = new LocalAgentFactory(softwareController);
		this.sedFactory = new SedFactory(softwareController);
		this.omFactory = new OmniNamesFactory(softwareController);
		try {
			Diet dietDescription = xmlScanner.buildDietModel(xmlInput);
			load(rm.getDietModel(), dietDescription);
			softwareController.setPlatform(rm.getPlatformModel());
		} catch (IOException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (XMLParseException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (DietResourceCreationException e) {
			throw new CommandExecutionException("Unable load model", e);
		}

	}

	/**
	 * Set the resource manager
	 * 
	 * @param rm
	 *            Resource Manager
	 */
	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

	/**
	 * Set the Xml Scanner
	 * 
	 * @param xmlScanner
	 */
	public void setXmlParser(XMLParser parser) {
		this.xmlScanner = parser;
	}

	/**
	 * Input data source
	 * 
	 * @param xmlInput
	 *            the xmlInput to set
	 */
	public void setXmlInput(InputStream xmlInput) {
		this.xmlInput = xmlInput;
	}

	/**
	 * Initialize platform (Physical resource) Initialize diet platform (diet
	 * agents, diet services, seds) Reset all model .
	 * 
	 * @throws DietResourceCreationException
	 * @throws CommandExecutionException
	 */
	private void load(DietManager dietManager, Diet dietConfiguration)
			throws DietResourceCreationException, CommandExecutionException {

		initDietPlatform(dietManager,
				dietConfiguration.getDietInfrastructure(),
				dietConfiguration.getDietServices());

	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 */
	private void initDietPlatform(DietManager dietManager,
			DietInfrastructure dietHierarchy, DietServices dietServices)
			throws DietResourceCreationException {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			Config c = omniName.getConfig();
			Resource r = this.rm.getPlatformModel().getResource(
					c.getServer());
			if(r == null) throw new DietResourceCreationException("Unable to find the physical resource "+c.getServer());
			c.setServerNode(r);
			dietManager.addOmniName(omFactory.create(omniName));

		}
		initMasterAgent(dietManager, dietHierarchy.getMasterAgent());

	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 */
	private void initMasterAgent(DietManager dietManager,
			List<MasterAgent> masterAgents)
			throws DietResourceCreationException {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				Config c = masterAgent.getConfig();
				Resource r = this.rm.getPlatformModel().getResource(
						c.getServer());
				if(r == null) throw new DietResourceCreationException("Unable to find the physical resource "+c.getServer());
				c.setServerNode(r);
				OmniNames omniNames = dietManager.getOmniName(masterAgent
						.getConfig().getServerNode().getDomain());
				if(omniNames == null) throw new DietResourceCreationException("Unable to find the omniNames for domain "+masterAgent.getConfig()
						.getServerNode().getDomain().getLabel() + ". Master agent id: "+masterAgent.getId());


				dietManager.addMasterAgent(maFactory.create(masterAgent,
						omniNames));
				initSeds(dietManager, masterAgent.getSed());
				initLocalAgents(dietManager, masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call on LocalAgents.
	 * 
	 * @param localAgent
	 * @throws DietResourceCreationException
	 */
	private void initLocalAgents(DietManager dietManager,
			List<LocalAgent> localAgents) throws DietResourceCreationException {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				Config c = localAgent.getConfig();
				Resource r = this.rm.getPlatformModel().getResource(
						c.getServer());
				if(r == null) throw new DietResourceCreationException("Unable to find the physical resource "+c.getServer());
				c.setServerNode(r);
				OmniNames omniNames = dietManager.getOmniName(localAgent
						.getConfig().getServerNode().getDomain());
				if(omniNames == null) throw new DietResourceCreationException("Unable to find the omniNames for domain "+localAgent.getConfig()
						.getServerNode().getDomain().getLabel() + ". LocalAgent id: "+localAgent.getId());

				dietManager.addLocalAgent(laFactory.create(localAgent,
						omniNames));
				initSeds(dietManager, localAgent.getSed());
				initLocalAgents(dietManager, localAgent.getLocalAgent());
			}

		}
	}

	/**
	 * 
	 * @param sed
	 * @throws DietResourceCreationException
	 */
	private void initSeds(DietManager dietManager, List<Sed> seds)
			throws DietResourceCreationException {
		if (seds != null) {
			for (Sed sed : seds) {
				Config c = sed.getConfig();
				Resource r = this.rm.getPlatformModel().getResource(
						c.getServer());
				if(r == null) throw new DietResourceCreationException("Unable to find the physical resource "+c.getServer());
				c.setServerNode(r);
				OmniNames omniNames = dietManager.getOmniName(sed.getConfig()
						.getServerNode().getDomain());
				if(omniNames == null) throw new DietResourceCreationException("Unable to find the omniNames for domain "+sed.getConfig()
						.getServerNode().getDomain().getLabel() + " Sed id: "+sed.getId());

				dietManager.addSed(sedFactory.create(sed, omniNames));
			}

		}

	}

	public void setRemoteAccess(RemoteAccess remoteAccess) {
		this.remoteAccess = remoteAccess;
	}

}
