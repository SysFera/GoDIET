package com.sysfera.godiet.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.LocalAgentFactory;
import com.sysfera.godiet.model.factories.MasterAgentFactory;
import com.sysfera.godiet.model.factories.OmniNamesFactory;
import com.sysfera.godiet.model.factories.SedFactory;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.DietDescription;
import com.sysfera.godiet.model.generated.DietInfrastructure;
import com.sysfera.godiet.model.generated.DietServices;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Infrastructure;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
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
public class LoadXMLImplCommand implements Command {
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
		if (rm == null || xmlScanner == null || xmlInput == null
				|| remoteAccess == null ) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		RemoteConfigurationHelper softwareController = new RemoteConfigurationHelper(remoteAccess);
		this.maFactory = new MasterAgentFactory(softwareController);
		this.laFactory = new LocalAgentFactory(softwareController);
		this.sedFactory = new SedFactory(softwareController);
		this.omFactory = new OmniNamesFactory(softwareController);
		try {
			DietDescription dietDescription = xmlScanner
					.buildDietModel(xmlInput);
			load(dietDescription);
			softwareController.setConfiguration(rm.getGodietConfiguration().getGoDietConfiguration());
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
	 * Initialize goDiet configuration Initialize platform (Physical resource)
	 * Initialize diet platform (diet agents, diet services, seds) Reset all
	 * model and load DietConfigurtion
	 * 
	 * @throws DietResourceCreationException
	 * @throws CommandExecutionException
	 */
	private void load(DietDescription dietConfiguration)
			throws DietResourceCreationException, CommandExecutionException {
		if (dietConfiguration != null) {
			this.rm.setGoDietConfiguration(dietConfiguration
					.getGoDietConfiguration());
			initPlatform(dietConfiguration.getInfrastructure());
			initDietPlatform(dietConfiguration.getDietInfrastructure(),
					dietConfiguration.getDietServices());
		}

	}

	/**
	 * 
	 * @param infrastructure
	 * @throws CommandExecutionException
	 */
	private void initPlatform(Infrastructure infrastructure)
			throws CommandExecutionException {
		List<Domain> domains = infrastructure.getDomain();
		this.rm.getPlatformModel().addDomains(domains);
		if (domains != null) {
			for (Domain domain : domains) {
				this.rm.getPlatformModel().addGateways(domain.getGateway());
				this.rm.getPlatformModel().addNodes(domain.getNode());
				List<Cluster> clusters = domain.getCluster();
				this.rm.getPlatformModel().addClusters(clusters);
				if (clusters != null) {
					for (Cluster cluster : clusters) {
						this.rm.getPlatformModel().addNodes(
								cluster.getComputingNode());
						this.rm.getPlatformModel().addFrontends(
								cluster.getFronted());
					}
				}
			}
		}

		List<Link> links = infrastructure.getLink();
		try {
			this.rm.getPlatformModel().addLinks(links);
		} catch (GraphDataException e) {

			throw new CommandExecutionException(
					"Unable to add links. Error in the model description", e);
		}
	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 */
	private void initDietPlatform(DietInfrastructure dietHierarchy,
			DietServices dietServices) throws DietResourceCreationException {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			this.rm.getDietModel().addOmniName(omFactory.create(omniName));

		}
		initMasterAgent(dietHierarchy.getMasterAgent());

	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 */
	private void initMasterAgent(List<MasterAgent> masterAgents)
			throws DietResourceCreationException {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				OmniNames omniNames = this.rm.getDietModel().getOmniName(
						masterAgent.getConfig().getServer().getDomain());
				this.rm.getDietModel().addMasterAgent(
						maFactory.create(masterAgent, omniNames));
				initSeds(masterAgent.getSed());
				initLocalAgents(masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call on LocalAgents.
	 * 
	 * @param localAgent
	 * @throws DietResourceCreationException
	 */
	private void initLocalAgents(List<LocalAgent> localAgents)
			throws DietResourceCreationException {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				OmniNames omniNames = this.rm.getDietModel().getOmniName(
						localAgent.getConfig().getServer().getDomain());

				this.rm.getDietModel().addLocalAgent(
						laFactory.create(localAgent, omniNames));
				initSeds(localAgent.getSed());
				initLocalAgents(localAgent.getLocalAgent());
			}

		}
	}

	/**
	 * 
	 * @param sed
	 * @throws DietResourceCreationException
	 */
	private void initSeds(List<Sed> seds) throws DietResourceCreationException {
		if (seds != null) {
			for (Sed sed : seds) {
				OmniNames omniNames = this.rm.getDietModel().getOmniName(
						sed.getConfig().getServer().getDomain());

				this.rm.getDietModel()
						.addSed(sedFactory.create(sed, omniNames));
			}

		}

	}

	public void setRemoteAccess(RemoteAccess remoteAccess) {
		this.remoteAccess = remoteAccess;
	}


}
