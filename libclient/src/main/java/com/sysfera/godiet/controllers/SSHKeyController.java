package com.sysfera.godiet.controllers;

public interface SSHKeyController {
	public static enum Status {
		PASSWORDINITIALIZE, PASSWORDNOTSET, LOADED, ERRORPRIVKEYNOTFOUND, ERRORPUBKEYNOTFOUND, ERROR
	}
	
	public abstract void setPubKeyPath(String keyPath);

	public abstract void setPrivKeyPath(String keyPath);

	public abstract String getPubKeyPath();

	public abstract String getPrivKeyPath();

	public abstract String getPassword();

	//TODO if already loaded unload , change pass and reload
	public abstract void setPassword(String password);

	public abstract Status getStatus();
}
