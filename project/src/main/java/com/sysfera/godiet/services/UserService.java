package com.sysfera.godiet.services;

import java.util.List;

import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

public interface UserService {

	public SSHKeyManager addSSHKey(Key key) throws AddAuthentificationException;

	public void registerSSHKey(SSHKeyManager managedKey)throws AddAuthentificationException;
	public List<SSHKeyManager> getManagedKeys();

}
