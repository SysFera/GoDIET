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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.common.exceptions.CommandExecutionException;
import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ExportException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.utils.StringUtils;
import com.sysfera.godiet.core.managers.DomainManager;
import com.sysfera.godiet.core.managers.topology.domain.DomainTopologyManager;
import com.sysfera.godiet.core.managers.topology.domain.DomainTopologyManagerGSImpl;
import com.sysfera.godiet.core.managers.topology.domain.Path;
import com.sysfera.godiet.core.managers.topology.domain.Path.Hop;
import com.sysfera.godiet.core.utils.graph.Draw;
import com.sysfera.godiet.core.utils.graph.DrawTopology;

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
	@Autowired
	private DomainManager dm;

	@Before
	public void init() throws IncubateException {
		try {
			// Loading configuration
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerConfigurationFile(
						outputString);

			}
			{
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				String outputString = StringUtils
						.streamToString(inputStreamPlatform);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}
			{
				// Init RM
				String testCaseFile = "diet/testbed6Domains.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}

		} catch (IOException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (XMLParseException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (GoDietConfigurationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceValidationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (DietResourceCreationException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (GraphDataException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		} catch (ResourceAddException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

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

			log.debug(e.getMessage());

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

			log.debug(e.getMessage());

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
			log.debug(e.getMessage());

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
			log.debug(e.getMessage());
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
			log.debug(e.getMessage());

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
		Assert.assertEquals(((Hop) o[2]).getIdDomain(), "CommonDomain123");
		Assert.assertEquals(((Hop) o[3]).getIdForwarder(),
				"CommonDomain123_Domain3");
		Assert.assertEquals(((Hop) o[3]).getIdDomain(), "Domain3");

	}

	@Test
	@DirtiesContext
	public void topologyTest() throws PathException, CommandExecutionException,
			ExportException {

		DomainTopologyManager dtm = dm.getDomainTopologyManager();
		Draw d = new DrawTopology((DomainTopologyManagerGSImpl) dtm);

		//d.display();
		URL imageURL = getClass().getResource("/draw/");

		d.startSnapshot(imageURL.getPath());
		d.drawShortestPath("DomainSysferaLB", "Domain3");
		d.endSnapshot();

		d.exportJPG(imageURL.getPath());
		d.exportDOT(imageURL.getPath());
		d.exportDGS(imageURL.getPath());
		// d.exportSVG(imageURL.getPath());

	}

	// public static void main(String[] args) throws PathException,
	// CommandExecutionException, ExportException {
	// =======
	// public void topologyTest() throws PathException,
	// CommandExecutionException {
	//
	// DomainManager dm = godiet.getModel().getDomainsManager();
	// DomainTopologyManager dtm = dm.getDomainTopologyManager();
	// DrawTopology d = new DrawTopology((DomainTopologyManagerGSImpl) dtm);
	//
	// d.display();
	//
	// // d.startSnapshot();
	// d.drawShortestPath("DomainSysferaLB", "Domain2");
	// // d.endSnapshot();
	//
	// // d.takeSnapshot();
	//
	// // d.exportJPG();
	// d.exportSnapshotJPG();
	// // d.exportDOT();
	// // d.exportDGS();
	// // d.exportSVG();
	// }
	//
	// public static void main(String[] args) throws PathException,
	// CommandExecutionException {
	// >>>>>>> 0aae24e... Nouvelle gestion des domaines et ajout de
	// DomainTopologyManager
	// ApplicationContext ctx = new ClassPathXmlApplicationContext(
	// new String[] { "/spring/spring-config.xml",
	// "/spring/ssh-context.xml", "/spring/godiet-service.xml" });
	// DomainManagerTest test = new DomainManagerTest();
	// GoDietService gs = (GoDietService) ctx.getBean("godiet");
	// test.godiet = gs;
	// System.setProperty("gs.ui.renderer",
	// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	// test.init();
	// test.topologyTest();
	// }
}