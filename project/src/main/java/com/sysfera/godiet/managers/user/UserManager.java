package com.sysfera.godiet.managers.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager.Status;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.remote.RemoteAccess;

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

	private final List<SSHKeyManager> managedKeys;
	@Autowired
	private RemoteAccess remoteAccessor;

	public UserManager() {
		managedKeys = new ArrayList<SSHKeyManager>();

	}

	public SSHKeyManager registerNewKey(Key key)
			throws AddAuthentificationException {
		SSHKeyManager managedKey = new SSHKeyManager(key);
		managedKeys.add(managedKey);
		registerKey(managedKey);
		return managedKey;
	}

	/**
	 * Register a key in the remote access manager if his password is
	 * initialized
	 * 
	 * @param key
	 * @throws AddAuthentificationException
	 */
	
	public void registerKey(SSHKeyManager key)
			throws AddAuthentificationException {
		if (key.state == Status.LOADED || key.state == Status.ERROR) {
			log.error("Unable to register " + key.getPubKeyPath()
					+ ". This key is in " + key.state + " state.");
			return;
		}
		if (key.state == Status.PASSWORDINITIALIZE
				|| !key.getKeyDesc().isEncrypted()) {
			try {
				remoteAccessor.addItentity(key);
				key.state = Status.LOADED;
			} catch (AddAuthentificationException e) {
				log.error(e.getMessage(), e);
				key.state = Status.ERROR;
				key.errorCause = e;
				throw e;
			}
		}

	}

	public List<SSHKeyManager> getManagedKeys() {
		return managedKeys;
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
	public void modifySSHKey(SSHKeyManager key, String privKeyPath,
			String pubkeyPath, String password)
			throws RemoveAuthentificationException,
			AddAuthentificationException {
		if (!managedKeys.contains(key))
			return;

		if (key.state == Status.LOADED) {
			remoteAccessor.removeItentity(key);
		}
		key.setPrivKeyPath(privKeyPath);
		key.setPubKeyPath(pubkeyPath);
		key.setPassword(password);
		registerKey(key);

	}

}
