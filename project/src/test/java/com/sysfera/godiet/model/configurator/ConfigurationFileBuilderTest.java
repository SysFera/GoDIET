package com.sysfera.godiet.model.configurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Scratch;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.services.GoDietService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class ConfigurationFileBuilderTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;

	@Before
	public void init() {
		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);

				godiet.getXmlHelpService().registerConfigurationFile(
						inputStream);

			}
			{
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				godiet.getXmlHelpService().registerInfrastructureElements(
						inputStreamPlatform);
			}
			{
				// Init RM
				String testCaseFile = "diet/vishnu.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				godiet.getXmlHelpService().registerDietElements(inputStream);
			}
			// Change the scratch directory to ressource manager
			{
				URL f = getClass().getResource("/");
				Scratch s = new ObjectFactory().createScratch();

				s.setDir(f.getPath());
				godiet.getModel().getGodietConfiguration().setLocalScratch(s);
			}
		} catch (IncubateException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

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
			// bar Assert.fail("" + e.getMessage());

		} catch (ResourceAddException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		}
	}

	@Test
	public void testMA() {
		String expected = "name = MA1\n" + "agentType = DIET_MASTER_AGENT";
		DietManager dm = godiet.getModel().getDietModel();

		SoftwareManager<? extends Software> MA1 = dm.getManagedSoftware("MA1");
		ConfigurationFile cf = MA1.getConfigurationFiles().get("MA1");
		if (cf == null)
			Assert.fail("MA1 config file not created");

		Assert.assertEquals(expected, cf.getContents());
	}

	@Test
	public void testLA() {
		String expected = "name = LA1\n" + "parentName = MA1\n"
				+ "agentType = DIET_LOCAL_AGENT";
		DietManager dm = godiet.getModel().getDietModel();

		SoftwareManager<? extends Software> LA1 = dm.getManagedSoftware("LA1");
		ConfigurationFile cf = LA1.getConfigurationFiles().get("LA1");
		if (cf == null)
			Assert.fail("LA1 config file not created");

		Assert.assertEquals(expected, cf.getContents());
	}

	@Test
	public void testForwarder() {
		String expected = "accept = .*\n" + "reject = localhost";
		DietManager dm = godiet.getModel().getDietModel();

		SoftwareManager<? extends Software> LA1 = dm.getManagedSoftware("server1");
		ConfigurationFile cf = LA1.getConfigurationFiles().get("server1");
		if (cf == null)
			Assert.fail("server1 config file not created");

		Assert.assertEquals(expected, cf.getContents());
	}

	@Test
	public void testUMSSedConfigurationFile() {
		String expected = "# Configuration of the VISHNU UMS SeD\n"
				+ "dietConfigFile=/tmp/scratch_runtime/Domain1/umsseddiet.cfg\n"
				+ "vishnuId=1\n"
				+ "databaseType=postgresql\n"
				+ "databaseHost=localhost\n"
				+ "databaseName=vishnu01\n"
				+ "databaseUserName=vishnu_user\n"
				+ "databaseUserPassword=vishnu_user\n"
				+ "#sendmailScriptPath=/home/hudson/workspace/IMS1/core/src/utils/sendmail.py\n"
//TODO: passer les tests			//	+ "sendmailScriptPath=/tmp/scratch_runtime/Domain1/sendmail.py\n"
				+ "sendmailScriptPath={this.configurationFiles.sendmailscript.absolutePath}\n"
				+ "vishnuMachineId=machine_1";

		DietManager dm = godiet.getModel().getDietModel();

		SoftwareManager<? extends Software> sed1 = dm
				.getManagedSoftware("sed1");
		String contents = sed1.getConfigurationFiles().get("umssedconf")
				.getContents();
 
		Assert.assertEquals(expected, contents);

	}

}
