package com.sysfera.godiet.core.model.configurator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

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

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.XMLParseException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.ConfigurationFile;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.ObjectFactory;
import com.sysfera.godiet.common.model.generated.Scratch;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.common.utils.StringUtils;
import com.sysfera.godiet.core.managers.ConfigurationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class ConfigurationFileBuilderTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;
	
	//To configure scratch
	@Autowired
	private ConfigurationManager configurationManager;

	@Before
	public void init() {
		try {
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
				String outputString = StringUtils.streamToString(inputStreamPlatform);

				godiet.getXmlHelpService().registerInfrastructureElements(
						outputString);
			}
			{
				// Init RM
				String testCaseFile = "diet/vishnu.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				String outputString = StringUtils.streamToString(inputStream);

				godiet.getXmlHelpService().registerDietElements(outputString);
			}

			// Change the scratch directory to ressource manager
			{
				URL f = getClass().getResource("/");
				Scratch s = new ObjectFactory().createScratch();

				s.setDir(f.getPath());
				configurationManager.setLocalScratch(s);
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

		String contents = getContent("MA1","MA1");
		if(contents == null) Assert.fail("Unable to find configuration file");
		Assert.assertEquals(expected, contents);
	}

	@Test
	public void testLA() {
		String expected = "name = LA1\n" + "parentName = MA1\n"
				+ "agentType = DIET_LOCAL_AGENT";

		String contents = getContent("LA1","LA1");
		if(contents == null) Assert.fail("Unable to find configuration file");
		Assert.assertEquals(expected, contents);
	}

	@Test
	public void testForwarder() {
		String expected = "accept = .*\n" + "reject = localhost";

		String contents = getContent("server1","server1");
		if(contents == null) Assert.fail("Unable to find configuration file");
		Assert.assertEquals(expected, contents);
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

	
		String contents = getContent("sed1","umssedconf");
		if(contents == null) Assert.fail("Unable to find configuration file");
		
		Assert.assertEquals(expected, contents);

	}
	private String getContent(String softwareId,String configurationFileId)
	{
		PlatformService dm = godiet.getPlatformService();

		SoftwareInterface<? extends Software> sed1 = dm
				.getManagedSoftware(softwareId);
		List<ConfigurationFile> configurationsFile = sed1.getFiles();
		
		if(configurationsFile == null) Assert.fail("No configuration file");
		
		
		for (ConfigurationFile configurationFile : configurationsFile) {
			if(configurationFile.getId().equals(configurationFileId))
			{
				return configurationFile.getContents();
			}
		}
		return null;
	}

}
