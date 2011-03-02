package com.sysfera.godiet.Utils;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.sysfera.godiet.exceptions.RemoteAccessException;

public class RemoteAccessJschImplTest {

	@Test
	public void testLocalhostScp() {

		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
	
		String testCaseFile = "/home/phi/.ssh/id_rsa";
		String urlFile = getClass().getClassLoader()
				.getResource(testCaseFile).getFile();
		if(urlFile == null)
			remoteJsch.addKey(urlFile,null,"mondedemerde0");
		try {
			remoteJsch.run("ls", "godiet", "testbed1", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
		//	Assert.fail("Unable access to TestBed1 machine");
		}
	}
}
