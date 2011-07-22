package com.sysfera.godiet.core.remote.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.UserInfo;

/**
 * Helper class to trust a new host
 */
public class TrustedUserInfo implements UserInfo  {
	private Log log = LogFactory.getLog(getClass());

	public String getPassphrase() {
		return null;
	}

	public String getPassword() {
		return null;
	}

	public boolean promptPassword(String s) {
		log.error(s);
		return false;
	}

	public boolean promptPassphrase(String s) {
		log.info(s);
		return false;
	}

	public boolean promptYesNo(String s) {
		log.debug(s);
		// trust
		return true;
	}

	public void showMessage(String s) {
		log.debug(s);
	}
}