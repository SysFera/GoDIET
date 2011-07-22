package com.sysfera.godiet.common.services;

import java.util.List;

import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.ModifyAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;

public interface UserService {

	public  Integer addSSHKey(Key key) throws AddAuthentificationException;

	public void modifyKey(Integer keyid, String privKeyPath,
			String pubkeyPath, String password) throws ModifyAuthentificationException;
	public List<SSHKeyController> getManagedKeys();

}
