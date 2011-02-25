package com.sysfera.godiet.Utils;

import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.sysfera.godiet.exceptions.RemoteAccessException;

public class RemoteAccessJschImplTest {

	@Test
	public void testLocalhostScp() {
		UserInfo userInfo = new UserInfo() {
			
			@Override
			public void showMessage(String message) {
				
			}
			
			@Override
			public boolean promptYesNo(String message) {
				return false;
			}
			
			@Override
			public boolean promptPassword(String message) {
				return false;
			}
			
			@Override
			public boolean promptPassphrase(String message) {
				return false;
			}
			
			@Override
			public String getPassword() {
				return null;
			}
			
			@Override
			public String getPassphrase() {
				return null;
			}
		};
		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.setUserInfo(userInfo);
		String testCaseFile = "fakeuser/id_rsa";
		String urlFile = getClass().getClassLoader()
				.getResource(testCaseFile).getFile();
		if(urlFile == null) 
		try {
			remoteJsch.setPrivateKey(urlFile);
		} catch (JSchException e1) {
			Assert.fail("Unable to load ssh key" +urlFile);
		}
		try {
			remoteJsch.execute("ls", "godiet", "testbed1", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine");
		}
	}
}
