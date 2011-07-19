package com.sysfera.godiet.model.configurator;

import java.io.IOException;
import java.io.InputStream;

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
import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.services.GoDietService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class CommandLineBuilderServiceTest {

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
			Assert.fail("" + e.getMessage());

		} catch (ResourceAddException e) {
			log.error("", e);
			Assert.fail("" + e.getMessage());

		}
	}

	@Test
	@DirtiesContext
	public void testMA1() {
		SoftwareInterface<? extends Software> MA1 = godiet.getPlatformService().getManagedSoftware("MA1");

		Assert.assertEquals(
				" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNIORB_CONFIG=/tmp/scratch_runtime/Domain1/omniNamesVishnu.cfg nohup dietAgent /tmp/scratch_runtime/Domain1/MA1.cfg > /tmp/scratch_runtime/Domain1/MA1.out 2> /tmp/scratch_runtime/Domain1/MA1.err &",
				MA1.getRunningCommand().toString());
	}

	@Test
	@DirtiesContext
	public void testLA1() {
		SoftwareInterface<? extends Software> LA1 = godiet.getPlatformService().getManagedSoftware("LA1");

		Assert.assertEquals(
				" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNIORB_CONFIG=/tmp/scratch_runtime/Domain1/omniNamesVishnu.cfg nohup dietAgent /tmp/scratch_runtime/Domain1/LA1.cfg > /tmp/scratch_runtime/Domain1/LA1.out 2> /tmp/scratch_runtime/Domain1/LA1.err &",
				LA1.getRunningCommand().toString());
	}

	@Test
	@DirtiesContext
	public void testForwarders() {
		{
			SoftwareInterface<? extends Software> client1 = godiet.getPlatformService().getManagedSoftware("client1");

			Assert.assertEquals(
					" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNIORB_CONFIG=/tmp/scratch_runtime/Domain1/omniNamesVishnu.cfg nohup dietForwarder --name client1 --net-config /tmp/scratch_runtime/Domain1/client1.cfg --peer-name server1  --ssh-host 192.169.1.2 --ssh-login godiet --remote-host 127.0.0.1 -C > /tmp/scratch_runtime/Domain1/client1.out 2> /tmp/scratch_runtime/Domain1/client1.err &",
					client1.getRunningCommand().toString());
		}
		{
			SoftwareInterface<? extends Software> server1 = godiet.getPlatformService().getManagedSoftware("server1");

			Assert.assertEquals(
					" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNIORB_CONFIG=/tmp/scratch_runtime/Domain2/omniNamesVishnu.cfg nohup dietForwarder --name server1 --net-config /tmp/scratch_runtime/Domain2/server1.cfg > /tmp/scratch_runtime/Domain2/server1.out 2> /tmp/scratch_runtime/Domain2/server1.err &",
					server1.getRunningCommand().toString());
		}
	}

	@Test
	@DirtiesContext
	public void testCommandLineSed1() {
		SoftwareInterface<? extends Software> sed1 = godiet.getPlatformService().getManagedSoftware("sed1");
		String server1command = sed1.getRunningCommand();

		Assert.assertEquals(
				" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNIORB_CONFIG=/tmp/scratch_runtime/Domain1/omniNamesVishnu.cfg nohup umssed /tmp/scratch_runtime/Domain1/umssedconf.cfg > /tmp/scratch_runtime/Domain1/sed1.out 2> /tmp/scratch_runtime/Domain1/sed1.err &",
				server1command.toString());

	}

	// test the default command line
	@Test
	@DirtiesContext
	public void testCommandLineOmniNames() {
		SoftwareInterface<? extends Software> omninames = godiet.getPlatformService().getManagedSoftware("omniNamesVishnu");
		String omninamescommand = omninames.getRunningCommand();

		// Take care of the last space
		Assert.assertEquals(
				" PATH=/home/godiet/GRAAL/build/bin:/home/godiet/GRAAL/build/bin/examples/dmat_manips/:$PATH  LD_LIBRARY_PATH=/home/godiet/GRAAL/build/lib OMNINAMES_LOGDIR=/tmp/scratch_runtime/Domain1/ OMNIORB_CONFIG=/tmp/scratch_runtime/Domain1/omniNamesVishnu.cfg nohup omniNames -start -always >/tmp/scratch_runtime/Domain1/OmniNames.out 2> /tmp/scratch_runtime/Domain1/OmniNames.err &",
				omninamescommand.toString());
	}

}
