package com.sysfera.godiet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

@Component
public class UserControllerImpl implements UserController {

	@Autowired
	private UserManager userManager;

	@Override
	public SSHKeyManager addSSHKey(Key key) throws AddAuthentificationException {
		return userManager.registerNewKey(key);
	}

	@Override
	public List<SSHKeyManager> getManagedKeys() {
		return userManager.getManagedKeys();
	}

	@Override
	public void registerSSHKey(SSHKeyManager managedKey) throws AddAuthentificationException {
		this.userManager.registerKey(managedKey);
		
	}
}
