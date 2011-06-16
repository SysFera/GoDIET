package com.sysfera.godiet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.managers.user.UserManager;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

@Component
public class UserControllerImpl implements UserController {

	@Autowired
	private UserManager userManager;


	@Override
	public void registerSSHKey(Key key) {
		SSHKeyManager managedKey = new SSHKeyManager(key);
		userManager.addManagedSSHKey(managedKey);
	}

}
