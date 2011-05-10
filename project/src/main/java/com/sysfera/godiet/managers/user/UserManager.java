package com.sysfera.godiet.managers.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager.Status;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.User;
import com.sysfera.godiet.remote.RemoteAccess;

/**
 * Manage User. Store keys and remote accessor Synchronize keys and the remote
 * access entities TODO: Pleins de trucs
 * 
 * @author phi
 * 
 */
public class UserManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final List<SSHKeyManager> managedKeys;
	private RemoteAccess remoteAccessor;

	public UserManager() {
		managedKeys = new ArrayList<SSHKeyManager>();

	}

	public void setRemoteAccessor(RemoteAccess remoteAccessor) {
		this.remoteAccessor = remoteAccessor;
	}

	public void addManagedSSHKey(SSHKeyManager key) {
		managedKeys.add(key);

	}

	/**
	 * Register a key in the remote access manager if his password is initialized
	 * @param key
	 */
	public void registerKey(SSHKeyManager key) {
		if (key.state == Status.PASSWORDINITIALIZE)
			try {
				remoteAccessor.addItentity(key);
				key.state = Status.LOADED;
			} catch (AddAuthentificationException e) {
				log.error(e.getMessage(),e);
				key.state = Status.ERROR;
				key.errorCause = e;
			}
	}

	/**
	 * 
	 * Try to register all keys on the remote access manager 
	 *	@see UserManager#registerKey(SSHKeyManager)
	 *
	 */
	public void registerAllKeys() {

		for (SSHKeyManager key : managedKeys) {
			registerKey(key);
		}

	}

	public List<SSHKeyManager> getManagedKeys() {
		return managedKeys;
	}

	/**
	 * Create a new key and add it in the Remote access manager
	 * @param pubkeyPath
	 * @param privKayPath
	 * @param password
	 */
	public void registerNewKey(String pubkeyPath, String privKayPath,
			String password) {
		User.Ssh.Key sshKeyDesc = new ObjectFactory().createUserSshKey();
		sshKeyDesc.setPath(privKayPath);
		sshKeyDesc.setPathPub(pubkeyPath);
		SSHKeyManager managedKey = new SSHKeyManager(sshKeyDesc);
		managedKey.setPassword(password);
		addManagedSSHKey(managedKey);
		registerKey(managedKey);
	}

	/**
	 * Modify key paramater. Create a new one and register if it was already Loaded in the remote access manager.
	 * @param key
	 * @param pubkeyPath
	 * @param privKayPath
	 * @param password
	 */
	public void modifySSHKey(SSHKeyManager key, String privKeyPath ,
			String pubkeyPath, String password) {
		if (!managedKeys.contains(key))
			return;
		if (key.state == Status.LOADED) {
			try {
				remoteAccessor.removeItentity(key);
				managedKeys.remove(key);
				registerNewKey(pubkeyPath, privKeyPath, password);
			} catch (RemoveAuthentificationException e) {
				log.error("Unable to remove key from identityManager");
			}

		} else {
			key.setPubKeyPath(pubkeyPath);
			key.setPrivKeyPath(privKeyPath);
			key.setPassword(password);
		}

	}

}
