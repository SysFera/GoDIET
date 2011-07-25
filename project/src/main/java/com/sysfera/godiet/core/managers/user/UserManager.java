package com.sysfera.godiet.core.managers.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.controllers.SSHKeyController.Status;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.ModifyAuthentificationException;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;
import com.sysfera.godiet.core.remote.RemoteAccess;

/**
 * Manage User. Store keys and remote accessor Synchronize keys and the remote
 * access entities TODO: Pleins de trucs
 * 
 * @author phi
 * 
 */
@Component
public class UserManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final Map<Integer, SSHKeyManager> managedKeys;
	@Autowired
	private RemoteAccess remoteAccessor;

	public UserManager() {
		managedKeys = new HashMap<Integer, SSHKeyManager>();

	}

	public SSHKeyManager registerNewKey(Key key)
			throws AddAuthentificationException {
		SSHKeyManager managedKey = new SSHKeyManager(key);
		managedKeys.put(managedKey.getId(), managedKey);
		registerKey(managedKey.getId());
		return managedKey;
	}

	/**
	 * Register a key in the remote access manager if his password is
	 * initialized or if it is not encypted
	 * 
	 * @param managedKey
	 * @throws AddAuthentificationException
	 */

	public void registerKey(Integer keyId) throws AddAuthentificationException {

		SSHKeyManager managedKey = managedKeys.get(keyId);
		if (managedKey == null) {
			log.error("Unable to find key with id : " + keyId);
			return;
		}
		if (managedKey.getStatus() == Status.LOADED
				|| managedKey.getStatus() == Status.ERROR) {
			log.error("Unable to register " + managedKey.getPubKeyPath()
					+ ". This key is in " + managedKey.getStatus() + " state.");
			return;
		}
		if (managedKey.getStatus() == Status.PASSWORDINITIALIZE
				|| !managedKey.getKeyDesc().isEncrypted()) {
			try {
				remoteAccessor.addItentity(managedKey);
				managedKey.state = Status.LOADED;
			} catch (AddAuthentificationException e) {
				log.error(e.getMessage(), e);
				managedKey.state = Status.ERROR;
				managedKey.errorCause = e;
				throw e;
			}
		}

	}

	public List<SSHKeyController> getManagedKeys() {
		List<SSHKeyController> sshKeys = new ArrayList<SSHKeyController>();
		sshKeys.addAll(managedKeys.values());
		return sshKeys;
	}

	/**
	 * Modify key paramater. Create a new one and register if it was already
	 * Loaded in the remote access manager.
	 * 
	 * @param key
	 * @param pubkeyPath
	 * @param privKayPath
	 * @param password
	 */
	public void modifySSHKey(Integer keyid, String privKeyPath,
			String pubkeyPath, String password)
			throws ModifyAuthentificationException {
		try{
		SSHKeyManager sshKeyManager = managedKeys.get(keyid);
		if (sshKeyManager ==  null)
			return;

		if (sshKeyManager.state == Status.LOADED) {
			remoteAccessor.removeItentity(sshKeyManager);
		}
		sshKeyManager.setPrivKeyPath(privKeyPath);
		sshKeyManager.setPubKeyPath(pubkeyPath);
		sshKeyManager.setPassword(password);
		registerKey(sshKeyManager.getId());
		}catch(Exception e)
		{
			throw new ModifyAuthentificationException("Unable to modify key with id " + keyid+ " Cause : " + e.getMessage(),e);
		}
	}
}
