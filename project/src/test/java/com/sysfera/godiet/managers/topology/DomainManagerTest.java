package com.sysfera.godiet.managers.topology;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.ExportException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DomainManager;
import com.sysfera.godiet.managers.topology.domain.DomainTopologyManager;
import com.sysfera.godiet.managers.topology.domain.DomainTopologyManagerGSImpl;
import com.sysfera.godiet.managers.topology.domain.Path;
import com.sysfera.godiet.managers.topology.domain.Path.Hop;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.services.GoDietService;
import com.sysfera.godiet.utils.graph.Draw;
import com.sysfera.godiet.utils.graph.DrawTopology;
//import com.sysfera.godiet.utils.graph.DrawTopology.Format;
//import com.sysfera.godiet.utils.graph.DrawTopology.Resos;

/**
 * Domains' infrastructure tests
 * 
 * @author Nicolas Quattropani
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class DomainManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;

	@Before
	public void init() {
		{
			// Loading configuration
			String configurationFile = "configuration/configuration.xml";
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);
			try {
				godiet.getXmlHelpController().registerConfigurationFile(
						inputStream);
			} catch (IOException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (XMLParseException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (GoDietConfigurationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			}
		}
		{
			// Load infrastructure
			String platformTestCase = "infrastructure/testbed.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			try {
				godiet.getXmlHelpController().registerInfrastructureElements(
						inputStreamPlatform); // lancement infrastructure
			} catch (IOException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (XMLParseException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (ResourceAddException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (GraphDataException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			}
		}
		// comment this part to execute the JUnit tests. 
		{
			// load diet
			String dietSoftwareCase = "diet/testbed-diet.xml";
			InputStream inputStreamDiet = getClass().getClassLoader()
					.getResourceAsStream(dietSoftwareCase);
			try {
				godiet.getXmlHelpController().registerDietElements(
						inputStreamDiet);
			} catch (IOException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (XMLParseException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (DietResourceCreationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (DietResourceValidationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (IncubateException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (GraphDataException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@DirtiesContext
	@Test
	public void searchPath1() {
		/**
		 * Calculation of the forwarders between those nodes. Here we have a
		 * graph with all its domains connected.
		 * 
		 * phi-laptop > graal / graal > testbedVM / testbedVM > Node1 / Node1 >
		 * Node3 / Node3 > Node5
		 */
		{
			// Load diet
			String dietSoftwareCase = "diet/testbed-diet.xml";
			InputStream inputStreamDiet = getClass().getClassLoader()
					.getResourceAsStream(dietSoftwareCase);
			try {
				godiet.getXmlHelpController().registerDietElements(
						inputStreamDiet);
				DomainManager dm = godiet.getModel().getDomainsManager();
				DomainTopologyManager dtm = dm.getDomainTopologyManager();

				Domain source;
				Domain destination;
				Path path = null;

				/** source = destination */
				source = dm.getDomains("DomainSysferaLB");
				destination = dm.getDomains("DomainSysferaLB");
				boolean exceptionPathNotExist = false;
				try {
					dtm.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.error(e.getMessage());
					exceptionPathNotExist = true;
				}
				Assert.assertEquals(exceptionPathNotExist, true);

				/** source and destination don't exist **/
				source = dm.getDomains("Fake1");
				destination = dm.getDomains("Fake2");
				boolean exceptionPathNotExist1 = false;
				try {
					dtm.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.error(e.getMessage());
					exceptionPathNotExist1 = true;
				}
				Assert.assertEquals(exceptionPathNotExist1, true);

				/** Source = destination / source and destination don't exist **/
				source = dm.getDomains("Fake1");
				destination = dm.getDomains("Fake1");
				boolean exceptionPathNotExist2 = false;
				try {
					dtm.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.error(e.getMessage());
					exceptionPathNotExist2 = true;
				}
				Assert.assertEquals(exceptionPathNotExist2, true);

				/** Source exists and destination doesn't **/
				source = dm.getDomains("DomainSysferaLB");
				destination = dm.getDomains("Fake");
				boolean exceptionPathNotExist3 = false;
				try {
					dtm.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.error(e.getMessage());
					exceptionPathNotExist3 = true;
				}
				Assert.assertEquals(exceptionPathNotExist3, true);

				/** Source doesn't exist and destination exists **/
				source = dm.getDomains("Fake");
				destination = dm.getDomains("Domain1");
				boolean exceptionPathNotExist4 = false;
				try {
					dtm.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.error(e.getMessage());
					exceptionPathNotExist4 = true;
				}
				Assert.assertEquals(exceptionPathNotExist4, true);

				/** Source and destination both exist. Test every hops **/
				source = dm.getDomains("DomainSysferaLB");
				destination = dm.getDomains("Domain3");
				try {
					path = dtm.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 4);
				Object[] o = path.getPath().toArray();
				Assert.assertEquals(((Hop) o[0]).getIdForwarder(),
						"DomainSysferaLB_DomainEnsDMZ");
				Assert.assertEquals(((Hop) o[0]).getIdDomain(), "DomainEnsDMZ");
				Assert.assertEquals(((Hop) o[1]).getIdForwarder(),
						"DomainEnsDMZ_DomainTestBed");
				Assert.assertEquals(((Hop) o[1]).getIdDomain(), "DomainTestBed");
				Assert.assertEquals(((Hop) o[2]).getIdForwarder(),
						"DomainTestBed_CommonDomain123");
				Assert.assertEquals(((Hop) o[2]).getIdDomain(),
						"CommonDomain123");
				Assert.assertEquals(((Hop) o[3]).getIdForwarder(),
						"CommonDomain123_Domain3");
				Assert.assertEquals(((Hop) o[3]).getIdDomain(), "Domain3");

			} catch (IOException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (XMLParseException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (DietResourceCreationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (DietResourceValidationException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (IncubateException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			} catch (GraphDataException e) {
				log.error("", e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@Test
	@DirtiesContext
	public void topologyTest() throws PathException, CommandExecutionException, ExportException {

		DomainManager dm = godiet.getModel().getDomainsManager();
		DomainTopologyManager dtm = dm.getDomainTopologyManager();
		Draw d = new DrawTopology((DomainTopologyManagerGSImpl) dtm);
		
		d.display();
		URL imageURL = getClass().getResource("/draw/");

		d.startSnapshot(imageURL.getPath());
		d.drawShortestPath("DomainSysferaLB", "Domain3");
		d.endSnapshot();

		d.exportJPG(imageURL.getPath());
		d.exportDOT(imageURL.getPath());
		d.exportDGS(imageURL.getPath());
		// d.exportSVG(imageURL.getPath());
	
	}

	public static void main(String[] args) throws PathException,
			CommandExecutionException, ExportException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "/spring/spring-config.xml",
						"/spring/ssh-context.xml", "/spring/godiet-service.xml" });
		DomainManagerTest test = new DomainManagerTest();
		GoDietService gs = (GoDietService) ctx.getBean("godiet");
		test.godiet = gs;
		System.setProperty("gs.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		test.init();
		test.topologyTest();
	}
}