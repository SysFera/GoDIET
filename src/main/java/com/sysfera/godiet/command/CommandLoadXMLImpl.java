package com.sysfera.godiet.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Model.xml.DietResourceManager;
import com.sysfera.godiet.Model.xml.DietServiceManager;
import com.sysfera.godiet.Model.xml.generated.Cluster;
import com.sysfera.godiet.Model.xml.generated.DietDescription;
import com.sysfera.godiet.Model.xml.generated.DietInfrastructure;
import com.sysfera.godiet.Model.xml.generated.DietService;
import com.sysfera.godiet.Model.xml.generated.DietServices;
import com.sysfera.godiet.Model.xml.generated.Domain;
import com.sysfera.godiet.Model.xml.generated.Infrastructure;
import com.sysfera.godiet.Model.xml.generated.Link;
import com.sysfera.godiet.Model.xml.generated.LocalAgent;
import com.sysfera.godiet.Model.xml.generated.MasterAgent;
import com.sysfera.godiet.Model.xml.generated.OmniNames;
import com.sysfera.godiet.Model.xml.generated.Sed;
import com.sysfera.godiet.Utils.XMLParser;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.XMLReadException;
import com.sysfera.godiet.managers.ResourcesManager;

/**
 * Initialize resource manager with the given XML description file.
 * Load configuration.
 * Load physical platform.
 * Load diet agents.
 * Instantiate diet forwarder if needed
 * @author phi
 * 
 */
public class CommandLoadXMLImpl implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private XMLParser xmlScanner;
	private InputStream xmlInput;
	
	
	@Override
	public String getDescription() {
		return "Initialize resource manager given a  XML data source";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (rm == null || xmlScanner == null  || xmlInput == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		try {
			DietDescription dietDescription = xmlScanner.buildDietModel(xmlInput);
			load(dietDescription);
		} catch (IOException e) {
			throw new CommandExecutionException("XML read error",e);
		} catch (XMLReadException e) {
			// TODO Auto-generated catch block
			throw new CommandExecutionException("XML read error",e);
		}
		
	}

	/**
	 * Set the resource manager 
	 * @param rm
	 *            Resource Manager
	 */
	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

	/**
	 * Set the Xml Scanner
	 * @param xmlScanner
	 */
	public void setXmlParser(XMLParser parser) {
		this.xmlScanner = parser;
	}
	
	/**
	 * Input data source
	 * @param xmlInput the xmlInput to set
	 */
	public void setXmlInput(InputStream xmlInput) {
		this.xmlInput = xmlInput;
	}

	/**
	 * Initialize goDiet configuration
	 * Initialize platform (Physical resource)
	 * Initialize diet platform (diet agents, diet services, seds) 
	 * Reset all model and load DietConfigurtion
	 */
	private void load(DietDescription dietConfiguration) {
		if (dietConfiguration != null) {
			this.rm.setGoDietConfiguration(dietConfiguration.getGoDietConfiguration());
			initPlatform(dietConfiguration.getInfrastructure());
			initDietPlatform(dietConfiguration.getDietInfrastructure(),
					dietConfiguration.getDietServices());
		}
		
	}
	
	private void initPlatform(Infrastructure infrastructure) {
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
						this.rm.getPlatformModel().addNodes(cluster.getComputingNode());
						this.rm.getPlatformModel().addFrontends(cluster.getFrontend());
					}
				}
			}
		}

		List<Link> links = infrastructure.getLink();
		this.rm.getPlatformModel().addLinks(links);
	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * @param dietHierarchy
	 * @param dietServices
	 */
	private void initDietPlatform(DietInfrastructure dietHierarchy, DietServices dietServices) {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			DietServiceManager managedOmniName = new DietServiceManager();
			managedOmniName.setDietService(omniName);
			this.rm.getDietModel().addOmniName(managedOmniName);
				
		}
		initMasterAgent(dietHierarchy.getMasterAgent());
		
	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * @param List
	 *            of masterAgent
	 */
	private void initMasterAgent(List<MasterAgent> masterAgents) {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				DietResourceManager dietResource = new DietResourceManager();
				dietResource.setDietAgent(masterAgent);
				this.rm.getDietModel().addMasterAgent(dietResource);
				initSeds(masterAgent.getSed());
				initLocalAgents(masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call on LocalAgents.
	 * @param localAgent
	 */
	private void initLocalAgents(List<LocalAgent> localAgents) {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				DietResourceManager dietResource = new DietResourceManager();
				dietResource.setDietAgent(localAgent);
				this.rm.getDietModel().addLocalAgent(dietResource);
				initSeds(localAgent.getSed());
				initLocalAgents(localAgent.getLocalAgent());
			}

		}
	}

	/**
	 * 
	 * @param sed
	 */
	private void initSeds(List<Sed> seds) {
		if (seds != null) {
			for (Sed sed : seds) {
				DietResourceManager sedDiet = new DietResourceManager();
				sedDiet.setDietAgent(sed);
				this.rm.getDietModel().addSed(sedDiet);
			}

		}

	}




}
