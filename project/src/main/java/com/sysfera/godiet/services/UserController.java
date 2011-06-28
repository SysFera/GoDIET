package com.sysfera.godiet.services;

import java.util.List;

import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

public interface UserController {

	SSHKeyManager addSSHKey(Key key) throws AddAuthentificationException;

	void registerSSHKey(SSHKeyManager managedKey)throws AddAuthentificationException;
	List<SSHKeyManager> getManagedKeys();

}
