package com.sysfera.godiet.common.rmi.model;

import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.controllers.SSHKeyController.Status;

public class SSHKeyControllerSerializable implements SSHKeyController {
	private final Integer id;
	private final String pubKeyPath;
	private final String privKeyPath;
	private final Status status;

	public SSHKeyControllerSerializable(SSHKeyController copy) {
		this.id = copy.getId();
		this.privKeyPath = copy.getPrivKeyPath();
		this.pubKeyPath = copy.getPubKeyPath();
		this.status = copy.getStatus();
	}

	@Override
	public String getPubKeyPath() {

		return pubKeyPath;
	}

	@Override
	public String getPrivKeyPath() {
		return privKeyPath;
	}

	@Override
	public Status getStatus() {

		return status;
	}

	@Override
	public Integer getId() {

		return id;
	}

}
