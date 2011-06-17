package com.sysfera.godiet.managers.topology;

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

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml" })
public class TopologyManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ResourcesManager rm;

	private TopologyManagerGSImpl tm;

	@Before
	public void setupTest() {

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
			{
				// Init RM
				String testCaseFile = "diet/2MA-1LA-6SED.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
				LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
				xmlLoadingCommand.setRm(rm);
				xmlLoadingCommand.setXmlInput(inputStream);
				xmlLoadingCommand.setXmlParser(scanner);
				SoftwareController softwareController = new RemoteConfigurationHelper(
						rm.getGodietConfiguration(), rm.getInfrastructureModel());
				DietManager dietModel = rm.getDietModel();
				GodietMetaFactory godietAbstractFactory = new GodietMetaFactory(
						softwareController, new ForwarderRuntimeValidatorImpl(
								dietModel),
						new MasterAgentRuntimeValidatorImpl(dietModel),
						new LocalAgentRuntimeValidatorImpl(dietModel),
						new SedRuntimeValidatorImpl(dietModel),
						new OmniNamesRuntimeValidatorImpl(dietModel));

				xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);

				xmlLoadingCommand.execute();
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
				Path path = null;

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
					// Comportement recherche
					exceptionPathNotExist = true;
				}

				Assert.assertEquals(exceptionPathNotExist, true);

				/** dource = destination et source et destination n'existent pas **/

				/** Source qui existe dest qui n'existe pas **/

				/** Dest qui n'existe pas et source qui existe **/

				/** Source dest joignable domain différent. Tester tous les hops **/

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
