package com.sysfera.godiet.Utils;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import com.sysfera.godiet.Model.AccessMethod;
import com.sysfera.godiet.Model.Domain;
import com.sysfera.godiet.Model.RunConfig;
import com.sysfera.godiet.Model.physicalresources.StorageResource;
import com.sysfera.godiet.exceptions.LaunchException;

public class JSchUtilsImplTest {

	@Test
	public void testStageWithScpTest() {
		JSchUtilsImpl jschUtil = new JSchUtilsImpl();
		StorageResource storeRes = new StorageResource("testStorage",
				new Domain());
		AccessMethod am = new AccessMethod("ssh", "localhost");
		AccessMethod am2 = new AccessMethod("scp", "localhost");
		storeRes.addAccessMethod(am);
		storeRes.addAccessMethod(am2);

		RunConfig runConfig = new RunConfig();

		String scratchTestDirectory = this.getClass().getResource("/")
				.getPath();
		runConfig.setLocalScratch(scratchTestDirectory);

		// init the destination dir
		File f = new File(scratchTestDirectory + "/test");
		if (f.exists()) {
			File[] files = f.listFiles();
			for (File file : files) {
				if (!file.delete())
					Assert.fail("Unable to delete for tests: "
							+ file.getAbsolutePath());
			}
		} else {
			if (!f.mkdir())
				Assert.fail("Unable to create test directory");
		}
		storeRes.setScratchBase(f.getAbsolutePath());

		try {
			jschUtil.stageWithScp("GoDietNG.xsd", storeRes, runConfig);
		} catch (LaunchException e) {
			Assert.fail("Unable to copy with Scp (SSH running on Server ?)"
					+ e.getMessage());
		}
	}
}
