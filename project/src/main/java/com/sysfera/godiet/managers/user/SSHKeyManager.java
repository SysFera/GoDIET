package com.sysfera.godiet.managers.user;

import java.io.File;
import java.io.FileNotFoundException;

import com.sysfera.godiet.controllers.SSHKeyController;
import com.sysfera.godiet.model.generated.User;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

public class SSHKeyManager implements SSHKeyController{


	Throwable errorCause = null;
	private final User.Ssh.Key sshDesc;
	Status state;
	private String password;

	protected SSHKeyManager(User.Ssh.Key sshDesc) {
		this.sshDesc = sshDesc;
		
		
		
		setPrivKeyPath(this.sshDesc.getPath());
		setPubKeyPath(this.sshDesc.getPathPub());
		if(this.sshDesc.isEncrypted()){
			state = Status.PASSWORDNOTSET;
		}
		else {
			setPassword("");
		}

	}

	private boolean fileExist(String filePath)
	{
		File file = new File(filePath);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setPubKeyPath(String keyPath)
	{
		if (keyPath  == null) {
			keyPath = this.sshDesc.getPath() + ".pub";
			this.sshDesc.setPathPub(keyPath);
		}
		if(!fileExist(keyPath)){
			state = Status.ERRORPUBKEYNOTFOUND;
			errorCause = new FileNotFoundException("Unable to find public key "
					+ keyPath);
			
		}
		
	}
	@Override
	public void setPrivKeyPath(String keyPath)
	{
		if (!fileExist(keyPath)) {
			state = Status.ERRORPRIVKEYNOTFOUND;
			errorCause = new FileNotFoundException(
					"Unable to find private key " + keyPath);
			
		}
	}
	@Override
	public String getPubKeyPath() {
		return sshDesc.getPathPub();
	}

	@Override
	public String getPrivKeyPath() {
		return sshDesc.getPath();
	}

	@Override
	public String getPassword() {
		return password;
	}

	//TODO if already loaded unload , change pass and reload
	@Override
	public void setPassword(String password) {
		if (state == Status.ERRORPRIVKEYNOTFOUND
				|| state == Status.ERRORPUBKEYNOTFOUND || state == Status.LOADED)
			return;
		this.password = password;
		state = Status.PASSWORDINITIALIZE;
	}

	Key getKeyDesc() {

		return sshDesc;
	}

	@Override
	public Status getStatus() {
		return state;
	}

	
}
