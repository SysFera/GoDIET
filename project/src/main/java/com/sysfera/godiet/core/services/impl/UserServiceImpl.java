package com.sysfera.godiet.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.ModifyAuthentificationException;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;
import com.sysfera.godiet.common.rmi.model.SSHKeyControllerSerializable;
import com.sysfera.godiet.common.services.UserService;
import com.sysfera.godiet.core.managers.user.UserManager;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserManager userManager;

	@Override
	public Integer addSSHKey(Key key) throws AddAuthentificationException {
		Integer keyid = userManager.registerNewKey(key).getId();
		return keyid;
	}

	@Override
	public List<SSHKeyController> getManagedKeys() {
		List<SSHKeyController> keysToSerialize = new ArrayList<SSHKeyController>();
		List<SSHKeyController> keys = userManager.getManagedKeys();
		for (SSHKeyController sshKeyController : keys) {
			keysToSerialize.add(new SSHKeyControllerSerializable(sshKeyController));
		}
		return keysToSerialize;
	}



	@Override
	public void modifyKey(Integer keyid, String privKeyPath, String pubkeyPath,
			String password) throws ModifyAuthentificationException {
		this.userManager.modifySSHKey(keyid, privKeyPath, pubkeyPath, password);
		
	}


	
	 
}
