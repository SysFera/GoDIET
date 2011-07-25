package com.sysfera.godiet.core.managers;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.sysfera.godiet.common.exceptions.generics.GoDietConfigurationException;
import com.sysfera.godiet.common.model.generated.Scratch;

public class ConfigurationManagerTest {

	@Test(expected = GoDietConfigurationException.class)
	public void directoryIsAFile() throws GoDietConfigurationException {
		String path = "itsafile";
		// First create a file
		File file = new File(path);
		try {
			file.createNewFile();
			
		} catch (IOException e) {
			Assert.fail("Unable to test " + e.getMessage());
		}
		
		ConfigurationManager cm = new ConfigurationManager();
		Scratch s = new Scratch();
		s.setDir(path);
		try {
			cm.setLocalScratch(s);
		} catch (GoDietConfigurationException e) {
			throw e;
		}
	}
}
