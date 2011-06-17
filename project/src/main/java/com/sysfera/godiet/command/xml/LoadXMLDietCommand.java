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
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.DietHierarchy;
import com.sysfera.godiet.model.generated.DietPlatform;
import com.sysfera.godiet.model.generated.DietServices;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
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


	private GodietMetaFactory abstractFactory;
	@Override
	public String getDescription() {
		return "Initialize resource manager given a  XML data source";
	}

	@Override
	public void execute() throws CommandExecutionException {
	if (rm == null || abstractFactory == null || xmlScanner == null
				|| xmlInput == null || rm.getInfrastructureModel() == null
				|| rm.getGodietConfiguration() == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}

		try {
			DietPlatform dietDescription = xmlScanner.buildDietModel(xmlInput);
			load(rm.getDietModel(), dietDescription);
		} catch (IOException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (XMLParseException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (DietResourceCreationException e) {
			throw new CommandExecutionException("Unable load model", e);
		}catch (IncubateException e) {
			throw new CommandExecutionException("Incubation failed", e);
		}

	}
	
	public void setAbstractFactory(GodietMetaFactory abstractFactory) {
		this.abstractFactory = abstractFactory;
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
	 * @throws IncubateException 
	 */
	private void load(DietManager dietManager, DietPlatform platform)
			throws DietResourceCreationException, CommandExecutionException, IncubateException {

		initDietPlatform(dietManager, platform.getHierarchy(),
				platform.getServices());

	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure ann DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 * @throws IncubateException 
	 */
	private void initDietPlatform(DietManager dietManager,
			DietHierarchy dietHierarchy, DietServices dietServices)
			throws DietResourceCreationException, IncubateException {

		// Add omniNames in the dietManager
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			Config c = omniName.getConfig();
			
	Node r = this.rm.getInfrastructureModel().getResource(
					c.getServer());
			if (r == null)
				throw new DietResourceCreationException(
						"Unable to find the physical resource " + c.getServer());
			dietManager.addOmniName(abstractFactory.create(omniName,r));
		
		
		}

		// Initialize Hierarchy
		initMasterAgent(dietManager, dietHierarchy.getMasterAgent());

	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 * @throws IncubateException 
	 */
	private void initMasterAgent(DietManager dietManager,
			List<MasterAgent> masterAgents)
			throws DietResourceCreationException, IncubateException {
		InfrastructureManager infraManager = this.rm.getInfrastructureModel();
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				Config c = masterAgent.getConfig();
				Node r = infraManager.getResource(
						c.getServer());
				if (r == null)
					throw new DietResourceCreationException(
							"Unable to find the physical resource "
									+ c.getServer());
				
				OmniNamesManaged omniNames = dietManager.getManagedOmniName(r);
				if (omniNames == null)
					throw new DietResourceCreationException(
							"Unable to find the omniNames for node "
									+ r
									+ ". Master agent id: "
									+ masterAgent.getId());

				dietManager.addMasterAgent(abstractFactory.create(masterAgent,r,
						omniNames));
	
				initSeds(dietManager, masterAgent.getSed());
				initLocalAgents(dietManager, masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call.
	 * 
	 * @param localAgent
	 * @throws DietResourceCreationException
	 * @throws IncubateException 
	 */
	private void initLocalAgents(DietManager dietManager,
			List<LocalAgent> localAgents) throws DietResourceCreationException, IncubateException {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				Config c = localAgent.getConfig();
				Node r = this.rm.getInfrastructureModel().getResource(
						c.getServer());
	
				OmniNamesManaged omniNames = dietManager
						.getManagedOmniName(r);
				if (omniNames == null)
					throw new DietResourceCreationException(
							"Unable to find the node: "
									+ localAgent.getConfig().getServer()
									+ ". LocalAgent id: " + localAgent.getId());

				dietManager.addLocalAgent(abstractFactory.create(localAgent,r,
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
	 * @throws IncubateException 
	 */
	private void initSeds(DietManager dietManager, List<Sed> seds)
			throws DietResourceCreationException, IncubateException {
		if (seds != null) {
			for (Sed sed : seds) {
				Config c = sed.getConfig();
				Node r = this.rm.getInfrastructureModel().getResource(
						c.getServer());
			
				OmniNamesManaged omniNames = dietManager.getManagedOmniName(r);
				if (omniNames == null)
					throw new DietResourceCreationException(
							"Unable to find omninames for the node: "
									+ sed.getConfig().getServer()
									+ " Sed id: " + sed.getId());

				dietManager.addSed(abstractFactory.create(sed, r,omniNames));
			}

		}

	}

}
