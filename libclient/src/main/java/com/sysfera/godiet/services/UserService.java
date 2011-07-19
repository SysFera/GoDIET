package com.sysfera.godiet.services;

import java.util.List;

import com.sysfera.godiet.controllers.SSHKeyController;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

public interface UserService {

	public SSHKeyController addSSHKey(Key key) throws AddAuthentificationException;

	public void registerSSHKey(SSHKeyController managedKey)throws AddAuthentificationException;
	public List<SSHKeyController> getManagedKeys();

}
