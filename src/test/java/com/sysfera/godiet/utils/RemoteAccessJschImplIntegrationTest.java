package com.sysfera.godiet.utils;

import org.junit.Assert;
import org.junit.Test;

import com.sysfera.godiet.exceptions.RemoteAccessException;

public class RemoteAccessJschImplIntegrationTest {

	@Test
	public void testLocalhostScp() {

		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.jschDebug(true);
		String testCaseFile = "fakeuser/id_rsa";
		String urlFile = getClass().getClassLoader().getResource(testCaseFile)
				.getFile();

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + testCaseFile);

		remoteJsch.addKey(urlFile, null, "godiet");
		try {
			remoteJsch.run("ls", "godiet", "localhost", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine. Reason"
					+ e.getMessage());
		}
	}

	@Test
	public void testProxy() {
		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.jschDebug(true);
		String testCaseFile = "fakeuser/id_rsa";
		String urlFile = getClass().getClassLoader().getResource(testCaseFile)
				.getFile();

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + testCaseFile);

		remoteJsch.addKey(urlFile, null, "godiet");
		try {
			remoteJsch.run("ls", "godiet", "localhost", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine. Reason"
					+ e.getMessage());
		}
	}
}
