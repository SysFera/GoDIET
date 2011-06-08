package com.sysfera.godiet.managers.topology;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;

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

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void searchPath1() {
		{
			String infrastructureTestCase = "infrastructure/6D-10N-7G-3L.xml";
			InputStream infrastructureInputStream = getClass().getClassLoader()
					.getResourceAsStream(infrastructureTestCase);
			try {
				XMLLoadingHelper.initInfrastructure(rm,
						infrastructureInputStream);

				InfrastructureManager infrastructureModel = rm
						.getInfrastructureModel();
				TopologyManager topologyManager = infrastructureModel
						.getTopologyManager();
				Resource source;
				Resource destination;
				Path path = null ;

				/** Source = Destination **/
				source = infrastructureModel.getResource("Node1");
				destination = infrastructureModel.getResource("Node1");
				try {
					path = topologyManager.findPath(source, destination);
				} catch (PathException e) {
					Assert.fail(e.getMessage());
				}
				Assert.assertEquals(path.getPath().size(), 0);

				
				
				/** Source et Dest n'existent pas **/
				source = infrastructureModel.getResource("Fake1");
				destination = infrastructureModel.getResource("Fake2");
				boolean exceptionPathNotExist = false;
				try {
					topologyManager.findPath(source, destination);

				} catch (PathException e) {
					//Comportement recherche
					exceptionPathNotExist = true;
				}

				Assert.assertEquals(exceptionPathNotExist, true);

				/** dource = destination et source et destination n'existent pas **/

				/** Source qui existe dest qui n'existe pas **/

				/**  Dest qui n'existe pas et source qui existe **/

				/**  Source dest joignable domain différent. Tester tous les hops **/

				
				/** Source dest joignable même domaine. Tester tous les hops **/

				/** Source et dest non joignable **/

				
			} catch (CommandExecutionException e) {
				log.error("Test Fail", e);
				Assert.fail(e.getMessage());
			}
		}
	}

	@Test
	public void searchPath2() {
		{
			String platformTestCase = "infrastructure/testbed.xml";
			InputStream inputStreamPlatform = getClass().getClassLoader()
					.getResourceAsStream(platformTestCase);
			try {
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
			} catch (CommandExecutionException e) {
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
