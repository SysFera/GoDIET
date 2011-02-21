package com.sysfera.godiet.Utils;

/* -*-mode:java; c-basic-offset:2; -*- */
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class KeyScanTest {
	
	@Test
	public  void mainTest() {
		List<String> hosts = new ArrayList<String>();
		hosts.add("127.0.0.1");
		try {
			JSch jsch = new JSch();
			jsch.setKnownHosts("keyscan");

			String[] keytype = { "ssh-rsa", "ssh-dss" };
			String user = "phi";
			java.util.Hashtable config = new java.util.Hashtable();
			for (String host : hosts) {
				
			
				System.out.println(host);
				for (int j = 0; j < keytype.length; j++) {
					try {
						config.put("server_host_key", keytype[j]);
						Session session = jsch.getSession(user, host, 22);
						session.setConfig(config);
						UserInfo ui = new MyUserInfo();
						session.setUserInfo(ui);
						session.setTimeout(1000);
						session.connect(1000);
					} catch (JSchException ee) {
						ee.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static class MyUserInfo implements UserInfo {
		public String getPassword() {
			return "rii0k47n";
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return "mondedemerde0";
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
		}
	}
}
