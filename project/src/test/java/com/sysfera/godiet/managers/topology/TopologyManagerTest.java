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
import com.sysfera.godiet.common.exceptions.ExportException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.utils.StringUtils;
import com.sysfera.godiet.core.managers.ResourcesManager;
import com.sysfera.godiet.core.managers.topology.infrastructure.TopologyManagerGSImpl;
import com.sysfera.godiet.core.utils.graph.Draw;
import com.sysfera.godiet.core.utils.graph.DrawTopology;

/**
 * Infrastructure's tests
 * 
 * @author Nicolas Quattropani
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class TopologyManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;
	@Autowired
	private ResourcesManager rm;
	private TopologyManagerGSImpl tm; // used to draw

	@Before
	public void setupTest() {

		try {
			// Loading configuration
			// Loading configuration

			String configurationFile = "configuration/configuration.xml";

			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configurationFile);
			String outputString = StringUtils.streamToString(inputStream);

			godiet.getXmlHelpService().registerConfigurationFile(outputString);

		} catch (IOException e) {
			Assert.fail();
		} catch (XMLParseException e) {
			Assert.fail();

		} catch (GoDietConfigurationException e) {
			Assert.fail();

		}
	}

	@Test
	public void topologyTest() throws PathException, CommandExecutionException,
			ExportException {

		try {
			String platformTestCase = "infrastructure/testbed.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			String outputString = StringUtils
					.streamToString(inputStreamPlatform);

			godiet.getXmlHelpService().registerInfrastructureElements(
					outputString);
		} catch (IOException e) {
			Assert.fail();

		} catch (XMLParseException e) {
			Assert.fail();

		} catch (ResourceAddException e) {
			Assert.fail();

		} catch (GraphDataException e) {
			Assert.fail();

		}

		tm = (TopologyManagerGSImpl) rm.getInfrastructureModel()
				.getTopologyManager();

		Draw d = new DrawTopology(tm);
		//d.display();
		URL imageURL = getClass().getResource("/draw/");

		d.startSnapshot(imageURL.getPath());
		d.drawShortestPath("miaou", "Node5");
		d.endSnapshot();

		d.exportJPG(imageURL.getPath());
		d.exportDOT(imageURL.getPath());
		d.exportDGS(imageURL.getPath());
		// d.exportSVG(imageURL.getPath());

	}

	// to display the graph
	// public static void main(String[] args) throws PathException,
	// CommandExecutionException, ExportException {
	// ApplicationContext ctx = new ClassPathXmlApplicationContext(
	// new String[] { "/spring/spring-config.xml",
	// "/spring/ssh-context.xml", "/spring/godiet-service.xml" });
	// TopologyManagerTest test = new TopologyManagerTest();
	// GoDietService gs = (GoDietService) ctx.getBean("godiet");
	// test.rm = gs.getModel();
	// System.setProperty("gs.ui.renderer",
	// "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
	// test.setupTest();
	// test.topologyTest();
	// }
}
