package com.sysfera.godiet.model.validators.building;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.validators.BuildingValidator;

public class SoftwareValidatorTest {
	private ResourcesManager rm = new ResourcesManager();

	@Before
	public void init() {

		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				XMLLoadingHelper.initConfig(rm, inputStream);
			}
			{
				String platformTestCase = "infrastructure/testbed-platform.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
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
			BuildingValidator.validate(ma, rm.getDietModel());
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
