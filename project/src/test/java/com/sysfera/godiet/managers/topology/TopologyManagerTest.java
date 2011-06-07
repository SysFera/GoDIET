package com.sysfera.godiet.managers.topology;

import java.io.InputStream;

import junit.framework.Assert;

import org.graphstream.graph.Path;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.graph.Draw;

public class TopologyManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	RemoteAccess remoteAccess = new RemoteAccessMock();
	private TopologyManagerGSImpl tm;

	@Before
	public void setupTest() {
		rm = new ResourcesManager();
		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				XMLLoadingHelper.initConfig(rm, inputStream);
			}
			{
				String platformTestCase = "infrastructure/6D-10N-7G-3L.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
			}			
		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void topologyTest() {
		tm = (TopologyManagerGSImpl)rm.getInfrastructureModel().getTopologyManager();		
		Draw d = new Draw(tm);
		d.display();
		d.drawShortestPath("Node1", "Domain6");		
	}
		
	public static void main(String[] args) {
		TopologyManagerTest test = new TopologyManagerTest();
		System.setProperty("gs.ui.renderer",
		"org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		test.setupTest();
		test.topologyTest();		
	}
}
