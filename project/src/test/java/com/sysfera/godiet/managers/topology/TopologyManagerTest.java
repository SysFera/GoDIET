package com.sysfera.godiet.managers.topology;

import java.io.IOException;
import java.io.InputStream;

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

import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.services.GoDietService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class TopologyManagerTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;
	@Autowired
	InfrastructureManager infrastructureModel;

	@Before
	public void init() throws IncubateException {

		{
			// Loading configuration
			String configurationFile = "configuration/configuration.xml";

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);

			try {
				godiet.getXmlHelpService().registerConfigurationFile(
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
	}

	@DirtiesContext
	@Test
	public void searchPath1() {
		{
			String infrastructureTestCase = "infrastructure/6D-10N-7G-3L.xml";
			InputStream infrastructureInputStream = getClass().getClassLoader()
					.getResourceAsStream(infrastructureTestCase);

			try {
				godiet.getXmlHelpService().registerInfrastructureElements(
						infrastructureInputStream);

				TopologyManager topologyManager = infrastructureModel
						.getTopologyManager();
				Resource source;
				Resource destination;
				Path path = null;

				/** Source = Destination **/
				source = infrastructureModel.getResource("Node1");
				destination = infrastructureModel.getResource("Node1");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				// We expect to find a path'size of 1 pointed at the source node
				Assert.assertEquals(path.getPath().size(), 1);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "Node1");

				/** Source et Dest n'existent pas **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Fake2");
				boolean exceptionPathNotExist = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					exceptionPathNotExist = true;
				}
				Assert.assertEquals(exceptionPathNotExist, true);

				/** Source = destination et source et destination n'existent pas **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Fake1");
				boolean exceptionPathNotExist2 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist2 = true;
				}
				Assert.assertEquals(exceptionPathNotExist2, true);

				/** Source qui existe dest qui n'existe pas **/
				source = infrastructureModel.getResource("Node1");
				destination = infrastructureModel.getResource("Fake1");
				boolean exceptionPathNotExist3 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist3 = true;
				}
				Assert.assertEquals(exceptionPathNotExist3, true);

				/** Source qui n'existe pas et dest qui existe **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Node6");
				boolean exceptionPathNotExist4 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist4 = true;
				}
				Assert.assertEquals(exceptionPathNotExist4, true);

				/**
				 * Source dest joignable domain différent. Tester tous les hops
				 **/
				source = infrastructureModel.getResource("Node1");
				destination = infrastructureModel.getResource("Node8");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 4);
				Object[] o = path.getPath().toArray();
				Assert.assertEquals(((Hop) o[0]).getDestination().getId(),
						"Node2");
				Assert.assertEquals(((Hop) o[0]).getLink().getId(),
						"node2interface1");
				Assert.assertEquals(((Hop) o[1]).getDestination().getId(),
						"Node4");
				Assert.assertEquals(((Hop) o[1]).getLink().getId(),
						"node4interface1");
				Assert.assertEquals(((Hop) o[2]).getDestination().getId(),
						"Node6");
				Assert.assertEquals(((Hop) o[2]).getLink().getId(),
						"node6interface1");
				Assert.assertEquals(((Hop) o[3]).getDestination().getId(),
						"Node8");
				Assert.assertEquals(((Hop) o[3]).getLink().getId(),
						"node8interface1");

				/** Source dest joignable même domaine. Tester tous les hops **/
				source = infrastructureModel.getResource("Node4");
				destination = infrastructureModel.getResource("Node5");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 1);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "Node5");
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getLink().getId(), "node5interface1");

				/** Source et dest non joignable **/
				source = infrastructureModel.getResource("Node9");
				destination = infrastructureModel.getResource("Node1");
				boolean exceptionPathNotExist5 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist5 = true;
				}
				Assert.assertEquals(exceptionPathNotExist5, true);

			} catch (Exception e) {
				log.error("Test Fail", e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@DirtiesContext
	@Test
	public void searchPath2() {
		{
			String platformTestCase = "infrastructure/testbed.xml";
			InputStream platforInputStream = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			try {
				godiet.getXmlHelpService().registerInfrastructureElements(
						platforInputStream);

				Resource source;
				Resource destination;
				Path path = null;

				/** Source = Destination **/
				//TODO: Need to throw exception like domainTopology
				source = infrastructureModel.getResource("testbedVM");
				destination = infrastructureModel.getResource("testbedVM");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				// We expect to find a path'size of 1 pointed at the source node
				Assert.assertEquals(path.getPath().size(), 1);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "testbedVM");

				/** Source et Dest n'existent pas **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Fake2");
				boolean exceptionPathNotExist = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist = true;
				}
				Assert.assertEquals(exceptionPathNotExist, true);

				/** Source = destination et source et destination n'existent pas **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Fake1");
				boolean exceptionPathNotExist2 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist2 = true;
				}
				Assert.assertEquals(exceptionPathNotExist2, true);

				/** Source qui existe dest qui n'existe pas **/
				source = infrastructureModel.getResource("phi-laptop");
				destination = infrastructureModel.getResource("Fake1");
				boolean exceptionPathNotExist3 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist3 = true;
				}
				Assert.assertEquals(exceptionPathNotExist3, true);

				/** Source qui n'existe pas et dest qui existe **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Node1");
				boolean exceptionPathNotExist4 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist4 = true;
				}
				Assert.assertEquals(exceptionPathNotExist4, true);

				/**
				 * Source dest joignable domain différent. Tester tous les hops
				 **/
				source = infrastructureModel.getResource("phi-laptop");
				destination = infrastructureModel.getResource("Node4");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 5);
				Object[] o = path.getPath().toArray();
				Assert.assertEquals(((Hop) o[0]).getDestination().getId(),
						"graal");
				Assert.assertEquals(((Hop) o[0]).getLink().getId(),
						"graalinterface1");
				Assert.assertEquals(((Hop) o[1]).getDestination().getId(),
						"testbedVM");
				Assert.assertEquals(((Hop) o[1]).getLink().getId(),
						"testbedVMinterface1");
				Assert.assertEquals(((Hop) o[2]).getDestination().getId(),
						"Node1");
				Assert.assertEquals(((Hop) o[2]).getLink().getId(),
						"node1interface1");
				Assert.assertEquals(((Hop) o[3]).getDestination().getId(),
						"Node5");
				Assert.assertEquals(((Hop) o[3]).getLink().getId(),
						"node5interface2");
				Assert.assertEquals(((Hop) o[4]).getDestination().getId(),
						"Node4");
				Assert.assertEquals(((Hop) o[4]).getLink().getId(),
						"node4interface1");

				/** Source dest joignable même domaine. Tester tous les hops **/
				source = infrastructureModel.getResource("Node2");
				destination = infrastructureModel.getResource("Node3");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 1);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "Node3");
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getLink().getId(), "node3interface1");

				source = infrastructureModel.getResource("Node3");
				destination = infrastructureModel.getResource("Node2");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 1);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "Node2");
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getLink().getId(), "node2interface1");

				
				
				source = infrastructureModel.getResource("graal");
				destination = infrastructureModel.getResource("Node1");
				try {
					path = infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 2);
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getDestination().getId(), "testbedVM");
				Assert.assertEquals(((Hop) path.getPath().toArray()[0])
						.getLink().getId(), "testbedVMinterface1");

				/** Source et dest non joignable **/
				source = infrastructureModel.getResource("Node2");
				destination = infrastructureModel.getResource("miaou");
				boolean exceptionPathNotExist5 = false;
				try {
					infrastructureModel.findPath(source, destination);
				} catch (PathException e) {
					// Comportement recherche
					log.debug(e.getMessage());
					exceptionPathNotExist5 = true;
				}
				Assert.assertEquals(exceptionPathNotExist5, true);

			} catch (Exception e) {
				log.error("Test Fail", e);
				Assert.fail(e.getMessage());
			}
		}
	}
	// @Test
	// public void topologyTest() {
	// tm =
	// (TopologyManagerGSImpl)rm.getInfrastructureModel().getTopologyManager();
	// Draw d = new Draw(tm);
	// d.display();
	// d.drawShortestPath("Node1", "Domain6");
	// }

	// public static void main(String[] args) {
	// TopologyManagerTest test = new TopologyManagerTest();
	// System.setProperty("gs.ui.renderer",
	// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	// test.setupTest();
	// test.topologyTest();
	// }
}
