package com.sysfera.godiet.Utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.sysfera.godiet.Model.xml.generated.DietDescription;
import com.sysfera.godiet.exceptions.XMLReadException;
/**
 * 
 * XML reader based on JAXB
 * 
 * @author phi
 */

public class XmlScannerJaxbImpl {

	private String GODIET_SCHEMA_PATH = "/GoDietNG.xsd";
	private String MODEL_PACKAGE_NAME = "com.sysfera.godiet.Model.xml.generated";


	
	public DietDescription buildDietModel(InputStream xmlFile)
			throws IOException, XMLReadException {

		try {
			JAXBContext jaxc = JAXBContext.newInstance(MODEL_PACKAGE_NAME);
			Unmarshaller u = jaxc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(this.getClass().getResource(
					GODIET_SCHEMA_PATH));
			u.setSchema(schema);

			DietDescription dietConfiguration = (DietDescription) u
					.unmarshal(xmlFile);
			 return dietConfiguration;
		} catch (JAXBException e) {
			throw new XMLReadException("Error when marshalling diet model", e);
		} catch (SAXException e) {
			throw new XMLReadException(
					"Error when marshalling diet model (unable to read schema)",
					e);

		}catch(IllegalArgumentException e){
			throw new XMLReadException("File not found",e);
		}
	}

	/**
	 * Hack.  Remove when have time
	 * @throws XMLReadException 
	 */
//	private void initDataModel(DietConfiguration dietConfiguration) throws XMLReadException
//	{
//		
////		//GoDietConfig
////		GoDiet goDiet = dietConfiguration.getGoDiet();
////		RunConfig runCfg = consoleCtrl.getRunConfig();
////		runCfg.setDebugLevel(goDiet.getDebug());
////		runCfg.setSaveStdOut(goDiet.isSaveStdOut());
////		runCfg.setSaveStdErr(goDiet.isSaveStdErr());
////		runCfg.setWatcherPeriod(goDiet.getWatcherPeriod());
////		runCfg.setLocalScratch(goDiet.getScratch().getDir());
////		//Control isLog is set cause setLogFile have business code.
////		if(goDiet.isLog())consoleCtrl.setLogFile("GoDIET.log");
////		
////		
////		List<Domain> domains = dietConfiguration.getInfrastructure().getDomain();
////		processDomains(domains);
////		for (Domain domain : domains) {
////			procesStorages(domain.getStorage(),domain);
////			processComputes(domain.getNode(),domain);
////			processGateways(domain.getGateway(),domain);
////			processClusters(domain.getCluster(),domain);
////		}
//		
//		
//		
//	}
	
	
//	private void processClusters(List<Cluster> clusters, Domain domain) throws XMLReadException {
//		if(clusters !=null) throw new XMLReadException("Not yet implemented");
//		
//	}
//
//	private void processGateways(List<Gateway> gateways, Domain domain) throws XMLReadException {
//		if(gateways !=null) throw new XMLReadException("Not yet implemented");		
//	}
//
//	private void processComputes(List<Node> computes, Domain domain) throws XMLReadException {
//		if(computes !=null) throw new XMLReadException("Not yet implemented");		
//	}
//
//	/**
//	 * Create and add domains in the resource manager
//	 * @param domains Domains to process
//	 */
//	private void processDomains(List<Domain> domains) {
//		for (Domain domain : domains) {
//			com.sysfera.godiet.Model.Domain domainResource = new com.sysfera.godiet.Model.Domain();
//			domainResource.setId(domain.getLabel());
//			this.mainController.addDomain(domainResource);
//		}
//		
//		
//	}
//
//	/**
//	 * 
//	 * @param storages the storages list to process
//	 * @throws XMLReadException 
//	 */
//	private void procesStorages(List<Storage> storages,Domain domain) throws XMLReadException{
//		if(storages != null)
//		{
//			for (Storage storage : storages) {
//				processStorage(storage,domain);
//			}
//		}
//	}
//	/**
//	 * Process storage node. Control and add a Storage resource in DietPlatformController
//	 * @param storage The XML node storage
//	 * @param domain the requested domain
//	 * @throws XMLReadException if domain not declared before.
//	 */
//	private void processStorage(Storage storage,Domain domain) throws XMLReadException{
//		com.sysfera.godiet.Model.Domain domainDiet =  mainController.getResourcePlatform().getDomain(domain.getLabel());
//		if(domainDiet == null) throw new XMLReadException("Oooups Runtime parsing problem. Domain not found ");
//		StorageResource sr = new StorageResource(storage.getLabel(),domainDiet );
//		
//	}
}
