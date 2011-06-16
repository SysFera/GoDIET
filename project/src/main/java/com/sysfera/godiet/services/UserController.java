package com.sysfera.godiet.services;

import com.sysfera.godiet.model.generated.User.Ssh.Key;

public interface UserController {

	void registerSSHKey(Key key);

}
