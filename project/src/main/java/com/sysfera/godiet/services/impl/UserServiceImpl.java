package com.sysfera.godiet.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.controllers.SSHKeyController;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.services.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserManager userManager;

	@Override
	public SSHKeyController addSSHKey(Key key) throws AddAuthentificationException {
		return userManager.registerNewKey(key);
	}

	@Override
	public List<SSHKeyController> getManagedKeys() {
		return userManager.getManagedKeys();
	}

	@Override
	public void registerSSHKey(SSHKeyController managedKey) throws AddAuthentificationException {
		this.userManager.registerKey(managedKey);
		
	}
}
