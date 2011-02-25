package com.sysfera.godiet.Utils;

import junit.framework.Assert;

import org.junit.Test;

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
				return "mondedemerde0";
			}
		};
		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.setUserInfo(userInfo);
		try {
			remoteJsch.execute("ls", "phi", "127.0.0.1", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
