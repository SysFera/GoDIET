package com.sysfera.godiet.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Diet;
import com.sysfera.godiet.model.generated.DietInfrastructure;
import com.sysfera.godiet.model.generated.DietServices;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.Infrastructure;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Platform;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.utils.xml.XMLParser;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

@Component
public class XMLHelpControllerImpl {
	@Autowired
	private  ConfigurationManager configurationManager;
	@Autowired
	private  PlatformController platformController;
	@Autowired
	private  InfrastructureController infrastructureController;
	@Autowired
	private  UserController userController;
	
	private final XMLParser xmlScanner;

	public XMLHelpControllerImpl() {
		
		this.xmlScanner = new XmlScannerJaxbImpl();
	}

	public void registerConfigurationFile(InputStream xmlInput)
			throws IOException, XMLParseException,GoDietConfigurationException {
		GoDietConfiguration goDietConfiguration = xmlScanner
				.buildGodietConfiguration(xmlInput);
		List<Key> sshKeys = goDietConfiguration.getUser().getSsh().getKey();
		// Initialize user manager whith key list in the xml desc file
		for (Key key : sshKeys) {
			userController.registerSSHKey(key);
		}
		
		configurationManager.setLocalNodeId(goDietConfiguration.getLocalNode());
		configurationManager.setLocalScratch(goDietConfiguration.getLocalscratch());
	}

	// TODO: Transactionnal
	public void registerDietElements(InputStream xmlInput) throws IOException,
			XMLParseException, DietResourceCreationException,
			DietResourceValidationException {
		Diet dietDescription = xmlScanner.buildDietModel(xmlInput);
		load(dietDescription);
	}

	// TODO: Transactionnal
	public void registerInfrastructureElements(InputStream xmlInput)
			throws IOException, XMLParseException, ResourceAddException,
			GraphDataException {
		Platform platformDescription = xmlScanner.buildPlatformModel(xmlInput);
		loadInfrastructure(platformDescription.getInfrastructure());
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
		List<Domain> domains = infrastructure.getDomain();
		infrastructureController.registerDomains(domains);
		if (domains != null) {
			for (Domain domain : domains) {
				infrastructureController.registerGateways(domain.getGateway());
				infrastructureController.registerNodes(domain.getNode());
				List<Cluster> clusters = domain.getCluster();
				infrastructureController.registerClusters(clusters);
				if (clusters != null) {
					for (Cluster cluster : clusters) {
						infrastructureController.registerNodes(cluster
								.getComputingNode());
						infrastructureController.registerFrontends(cluster
								.getFronted());
					}
				}
			}
		}

		List<Link> links = infrastructure.getLink();
		infrastructureController.registerLinks(links);
	}

	/**
	 * TODO: Move this code to an auto servcies manager handler Initialize
	 * platform (Physical resource) Initialize diet platform (diet agents, diet
	 * services, seds) Reset all model .
	 * 
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 * @throws CommandExecutionException
	 */
	private void load(Diet diet) throws DietResourceCreationException,
			DietResourceValidationException {

		initDietPlatform(diet.getDietInfrastructure(), diet.getDietServices());

	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 */
	private void initDietPlatform(DietInfrastructure dietHierarchy,
			DietServices dietServices) throws DietResourceCreationException,
			DietResourceValidationException {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			platformController.registerOmniNames(omniName);

		}
		initMasterAgents(dietHierarchy.getMasterAgent());

	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 * @throws DietResourceValidationException
	 */
	private void initMasterAgents(List<MasterAgent> masterAgents)
			throws DietResourceCreationException,
			DietResourceValidationException {
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
	 */
	private void initLocalAgents(List<LocalAgent> localAgents)
			throws DietResourceCreationException,
			DietResourceValidationException {
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
	 */
	private void initSeds(List<Sed> seds) throws DietResourceCreationException,
			DietResourceValidationException {
		if (seds != null) {
			for (Sed sed : seds) {
				platformController.registerSed(sed);
			}

		}

	}

}
