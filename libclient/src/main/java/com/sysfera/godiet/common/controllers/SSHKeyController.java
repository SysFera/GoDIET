package com.sysfera.godiet.common.controllers;

import java.io.Serializable;

public interface SSHKeyController extends Serializable{
	public static enum Status {
		PASSWORDINITIALIZE, PASSWORDNOTSET, LOADED, ERRORPRIVKEYNOTFOUND, ERRORPUBKEYNOTFOUND, ERROR
	}


	public abstract String getPubKeyPath();

	public abstract String getPrivKeyPath();


	public abstract Status getStatus();
	
	public abstract Integer getId();
}
