package com.sysfera.godiet.managers.user;

import java.io.File;
import java.io.FileNotFoundException;

import com.sysfera.godiet.model.generated.User;
import com.sysfera.godiet.model.generated.User.Ssh.Key;

public class SSHKeyManager {
	static enum Status {
		PASSWORDINITIALIZE, PASSWORDNOTSET, LOADED, ERRORPRIVKEYNOTFOUND, ERRORPUBKEYNOTFOUND, ERROR
	}

	Throwable errorCause = null;
	private final User.Ssh.Key sshDesc;
	Status state;
	private String password;

	public SSHKeyManager(User.Ssh.Key sshDesc) {
		this.sshDesc = sshDesc;
		state = Status.PASSWORDNOTSET;
		
		setPrivKeyPath(this.sshDesc.getPath());
		setPubKeyPath(this.sshDesc.getPathPub());

	}

	private boolean fileExist(String filePath)
	{
		File file = new File(filePath);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}
	
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
	public void setPrivKeyPath(String keyPath)
	{
		if (!fileExist(keyPath)) {
			state = Status.ERRORPRIVKEYNOTFOUND;
			errorCause = new FileNotFoundException(
					"Unable to find private key " + keyPath);
			
		}
	}
	public String getPubKeyPath() {
		return sshDesc.getPathPub();
	}

	public String getPrivKeyPath() {
		return sshDesc.getPath();
	}

	public String getPassword() {
		return password;
	}

	//TODO if already loaded unload , change pass and reload
	public void setPassword(String password) {
		if (state == Status.ERRORPRIVKEYNOTFOUND
				|| state == Status.ERRORPUBKEYNOTFOUND || state == Status.LOADED)
			return;
		this.password = password;
		state = SSHKeyManager.Status.PASSWORDINITIALIZE;
	}

	Key getKeyDesc() {

		return sshDesc;
	}

	public Status getState() {
		return state;
	}
}
