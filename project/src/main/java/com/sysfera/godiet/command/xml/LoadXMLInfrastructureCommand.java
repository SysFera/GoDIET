package com.sysfera.godiet.command.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Infrastructure;
import com.sysfera.godiet.utils.xml.XMLParser;

/**
 * Initialize resource manager with the given XML description file. Load
 * configuration. Load physical platform. Load diet agents. Dummy diet forwarder
 * loading ( 2 diet forwarder for each declared link )
 * 
 * @author phi
 * 
 */
public class LoadXMLInfrastructureCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private XMLParser xmlScanner;
	private InputStream xmlInput;

	@Override
	public String getDescription() {
		return "Initialize platform manager given a  XML data source";
	}

	@Override
	public void execute() throws CommandExecutionException {
		if (rm == null || xmlScanner == null || xmlInput == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}

		try {
			Infrastructure infraDescription = xmlScanner
					.buildInfrastructureModel(xmlInput);
			initInfrastructureModel(infraDescription);
		} catch (IOException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (XMLParseException e) {
			throw new CommandExecutionException("XML read error", e);
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
	 * 
	 * @param infrastructure
	 * @throws CommandExecutionException
	 */
	private void initInfrastructureModel(Infrastructure infrastructure)
			throws CommandExecutionException {
		List<Domain> domains = infrastructure.getDomain();
		this.rm.getInfrastructureModel().addDomains(domains);
		this.rm.getInfrastructureModel().addNodes(infrastructure.getNode());
		this.rm.getInfrastructureModel().addClusters(infrastructure.getCluster());
		try {
			this.rm.getInfrastructureModel().addLinks(infrastructure.getLink());
		} catch (GraphDataException e) {
			throw new CommandExecutionException("Unable to initialize infrastructure",e);
		}
	}

}
