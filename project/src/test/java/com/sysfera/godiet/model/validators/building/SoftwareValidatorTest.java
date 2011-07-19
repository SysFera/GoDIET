package com.sysfera.godiet.model.validators.building;

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

import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.validators.BuildingValidator;
import com.sysfera.godiet.services.GoDietService;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = { "/spring/spring-config.xml",
		"/spring/ssh-context.xml", "/spring/godiet-service.xml" })
public class SoftwareValidatorTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private GoDietService godiet;

	@Autowired
	private DietManager dietManager;

	@Before
	public void init() throws IncubateException {
		try {
			{
				// Loading configuration
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);

				godiet.getXmlHelpService().registerConfigurationFile(
						inputStream);
			}
			{
				// Load infrastructure
				String platformTestCase = "infrastructure/testbed.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				godiet.getXmlHelpService().registerInfrastructureElements(
						inputStreamPlatform);
			}
		} catch (Exception e) {

		}
	}

	@Test
	public void test1() {

		ObjectFactory factory = new ObjectFactory();

		MasterAgent ma = factory.createMasterAgent();
		ma.setId("MA1");

		try {
			BuildingValidator.validate(ma,dietManager);
		} catch (DietResourceValidationException e) {
			Assert.fail("");
		}

		// TODO: tester le reste. Partir d'un scénario de platform pour que le
		// ma (parent) soit valide (ie enregistrer dans dietmanager
		// LocalAgent la = factory.createLocalAgent();
		// la.setId("LA1");
		// la.setParent(ma);
		// try {
		// validator.validate(la);
		// } catch (DietResourceValidationException e) {
		// Assert.fail("");
		// }
		//

	}
}
