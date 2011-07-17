package com.sysfera.godiet.model.configurator;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

public class TemplateFileProviderTest {

	@Test
	public void testFindTemplate() {
		TemplateFilesProvider tfm = new TemplateFilesProvider();
		try {
			Assert.assertNull(tfm.getTemplateLoader().findTemplateSource(
					"fakeTemplateFile"));

			Assert.assertNotNull(tfm.getTemplateLoader().findTemplateSource(
					"sed_template.config"));
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}

	}

}
