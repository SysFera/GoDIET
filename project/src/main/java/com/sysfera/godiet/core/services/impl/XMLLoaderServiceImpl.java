package com.sysfera.godiet.core.services.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.CommandExecutionException;
import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.DietHierarchy;
import com.sysfera.godiet.common.model.generated.DietPlatform;
import com.sysfera.godiet.common.model.generated.DietServices;
import com.sysfera.godiet.common.model.generated.Forwarders;
import com.sysfera.godiet.common.model.generated.GoDietConfiguration;
import com.sysfera.godiet.common.model.generated.Infrastructure;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;
import com.sysfera.godiet.common.services.InfrastructureService;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.common.services.UserService;
import com.sysfera.godiet.common.services.XMLLoaderService;
import com.sysfera.godiet.core.managers.ConfigurationManager;
import com.sysfera.godiet.core.utils.xml.XMLParser;
import com.sysfera.godiet.core.utils.xml.XmlScannerJaxbImpl;

@Component
public class XMLLoaderServiceImpl implements XMLLoaderService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigurationManager configurationManager;
	@Autowired
	private PlatformService platformController;
	@Autowired
	private InfrastructureService infrastructureController;
	@Autowired
	private UserService userController;

	private final XMLParser xmlScanner;

	public XMLLoaderServiceImpl() {

		this.xmlScanner = new XmlScannerJaxbImpl();
	}

	@Override
	public void registerConfigurationFile(String xmlInput)
			throws IOException, XMLParseException, GoDietConfigurationException {
		GoDietConfiguration goDietConfiguration = xmlScanner
				.buildGodietConfiguration(xmlInput);
		if (goDietConfiguration.getUser() != null
				&& goDietConfiguration.getUser().getSsh() != null
				&& goDietConfiguration.getUser().getSsh().getKey() != null) {
			List<Key> sshKeys = goDietConfiguration.getUser().getSsh().getKey();
			// Initialize user manager whith key list in the xml desc file
			for (Key key : sshKeys) {
				try {
					userController.addSSHKey(key);
				} catch (AddAuthentificationException e) {
					log.error("Unable to register ssh key " + key.getPathPub());
					e.printStackTrace();
				}
			}
		}
		configurationManager.setLocalNodeId(goDietConfiguration.getLocalNode());
		configurationManager.setLocalScratch(goDietConfiguration
				.getLocalscratch());
	}

	// TODO: Transactionnal
	@Override
	public void registerDietElements(String xmlInput) throws IOException,
			XMLParseException, DietResourceCreationException,
			DietResourceValidationException, IncubateException, GraphDataException {

		DietPlatform dietDescription = xmlScanner.buildDietModel(xmlInput);
		load(dietDescription);
	}

	// TODO: Transactionnal
	@Override
	public void registerInfrastructureElements(String xmlInput)
			throws IOException, XMLParseException, ResourceAddException,
			GraphDataException {

		Infrastructure platformDescription = xmlScanner
				.buildInfrastructureModel(xmlInput);
		loadInfrastructure(platformDescription);
	}

	/**
	 * TODO Transactionnal
	 * 
	 * @param infrastructure
	 * @throws ResourceAddException
	 * @throws CommandExecutionException
	 */
	private void loadInfrastructure(Infrastructure infrastructure)
			throws ResourceAddException, GraphDataException {
		this.infrastructureController.registerDomains(infrastructure
				.getDomain());
		this.infrastructureController.registerNodes(infrastructure.getNode());
		// FIXME
		// this.rm.getInfrastructureModel().addClusters(infrastructure.getCluster());

		this.infrastructureController.registerLinks(infrastructure.getLink());

	}

	/**
	 * TODO: Move this code to an auto servcies manager handler Initialize
	 * platform (Physical resource) Initialize diet platform (diet agents, diet
	 * services, seds) Reset all model .
	 * 
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 * @throws IncubateException
	 * @throws GraphDataException 
	 * @throws CommandExecutionException
	 */
	private void load(DietPlatform diet) throws DietResourceCreationException,
			DietResourceValidationException, IncubateException, GraphDataException {

		initDietPlatform(diet.getHierarchy(), diet.getServices());

	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 * @throws IncubateException
	 * @throws GraphDataException 
	 */
	private void initDietPlatform(DietHierarchy dietHierarchy,
			DietServices dietServices) throws DietResourceCreationException,
			DietResourceValidationException, IncubateException, GraphDataException {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			platformController.registerOmniNames(omniName);

		}
		initForwarders(dietServices.getForwarders());
		initMasterAgents(dietHierarchy.getMasterAgent());

	}

	/**
	 * Init forwarders
	 * 
	 * @param forwarders
	 * @throws IncubateException
	 * @throws DietResourceCreationException
	 * @throws GraphDataException 
	 */
	private void initForwarders(List<Forwarders> forwarders)
			throws DietResourceCreationException, IncubateException, GraphDataException {
		if (forwarders != null) {
			for (Forwarders forwarder : forwarders) {
				platformController.registerForwarders(forwarder.getClient(),
						forwarder.getServer());
			}
		}
	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 * @throws IncubateException
	 */
	private void initMasterAgents(List<MasterAgent> masterAgents)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				platformController.registerMasterAgent(masterAgent);
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
	 * @throws DietResourceValidationException
	 * @throws IncubateException
	 */
	private void initLocalAgents(List<LocalAgent> localAgents)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				platformController.registerLocalAgent(localAgent);
				initSeds(localAgent.getSed());
				initLocalAgents(localAgent.getLocalAgent());
			}

		}
	}

	/**
	 * 
	 * @param sed
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 * @throws IncubateException
	 */
	private void initSeds(List<Sed> seds) throws DietResourceCreationException,
			DietResourceValidationException, IncubateException {
		if (seds != null) {
			for (Sed sed : seds) {
				platformController.registerSed(sed);
			}

		}

	}

}
