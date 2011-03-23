package com.sysfera.godiet.managers.topology;

import java.io.InputStream;
import java.util.LinkedHashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.CommandLoadXMLImpl;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.graph.PathException;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class TopologyManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;

	@Before
	public void setupTest() {
		String testCaseFile = "6D-10N-7G-3L-2MA-1LA-6SED.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		CommandLoadXMLImpl xmlLoadingCommand = new CommandLoadXMLImpl();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}

	}

	@Test
	public void topologyTest() {
		// Correct complex path
		Platform physPlatform = rm.getPlatformModel();
		Node sourceNode = (Node) physPlatform.getResource("Node1");
		Node destinationNode = (Node) physPlatform.getResource("Node5");
		

		try {
			Path p = physPlatform.findPath((Node)sourceNode, (Node)destinationNode);
			LinkedHashSet<? extends Resource> res = p.getPath();
			String info = "Find path: ";
			for (Resource resource : res) {

				if (resource instanceof Node) {
					Node n = (Node) resource;
					info += (n.getSsh().getServer() + " ");
				} else if (resource instanceof Gateway) {
					Node n = (Node) ((Gateway) resource).getRef();
					info += (n.getSsh().getServer() + " ");
				} else
					Assert.fail("New resource type ? "
							+ resource.getClass().getName());
			}
			log.info(info);
		} catch (PathException e) {
			e.printStackTrace();
			Assert.fail("Topology error");
		}

		// Thrown PathException cause incorrect node name
		sourceNode = (Node) physPlatform.getResource("Node1");
		destinationNode = (Node) physPlatform.getResource("IncorrectNodeName");
		boolean thrown = false;
		try {
			physPlatform.findPath(sourceNode, destinationNode);
		} catch (PathException e) {
			thrown = true;
		}
		Assert.assertTrue(thrown);
		thrown = false;

		// Null path cause unreachable node (Node7 (Domain5) to Node2 (Domain2)
		sourceNode = (Node) physPlatform.getResource("Node7");
		destinationNode = (Node) physPlatform.getResource("Node2");

		try {
			Path p = physPlatform.findPath(sourceNode, destinationNode);
			Assert.assertNull(p);
		} catch (PathException e) {
			Assert.fail("Must be null, not throw exception");
		}


		//Complex path (from D4 to D1. Find ClientNodeDomain4<-->G4<-->(G6<-->G7)<-->G2<-->G3<-->G1<-->Node1 )
		sourceNode = (Node) physPlatform.getResource("ClientNodeDomain4");
		destinationNode = (Node) physPlatform.getResource("Node1");
		try {
			Path p = physPlatform.findPath(sourceNode, destinationNode);
			Object[] pathResources = p.getPath().toArray();
			Assert.assertEquals(8,pathResources.length);

			Assert.assertEquals("ClientNodeDomain4",((Resource)pathResources[0]).getId());
			Assert.assertEquals("G4",((Resource)pathResources[1]).getId());
			Assert.assertEquals("G6",((Resource)pathResources[2]).getId());
			Assert.assertEquals("G7",((Resource)pathResources[3]).getId());
			Assert.assertEquals("G2",((Resource)pathResources[4]).getId());
			Assert.assertEquals("G3",((Resource)pathResources[5]).getId());
			Assert.assertEquals("G1",((Resource)pathResources[6]).getId());
			Assert.assertEquals("Node1",((Resource)pathResources[7]).getId());
			

		} catch (PathException e) {
			Assert.fail();
		}
		
		//Path.length = 2 cause nodes are in the same domain
		sourceNode = (Node) physPlatform.getResource("Node4");
		destinationNode = (Node) physPlatform.getResource("Node5");
		try {
			Path p = physPlatform.findPath(sourceNode, destinationNode);
			Object[] pathResources = p.getPath().toArray();
			Assert.assertEquals(2,pathResources.length);

			Assert.assertEquals("Node4",((Resource)pathResources[0]).getId());
			Assert.assertEquals("Node5",((Resource)pathResources[1]).getId());
		} catch (PathException e) {
			Assert.fail("Must be null, not throw exception");
		}
		

		//TODO Try to add an exising link
	}
}
