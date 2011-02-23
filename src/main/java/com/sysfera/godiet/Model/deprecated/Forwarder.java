package com.sysfera.godiet.Model.deprecated;

import com.sysfera.godiet.Model.deprecated.Forwarder.ForwarderType;
import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;

public class Forwarder extends Agents {
	public final static String DIET_FORWARDERBINARY = "dietForwarder";

	public enum ForwarderType {
		SERVER, CLIENT
	}

	public Forwarder(String name, ComputeResource compRes, String binary,
			Domain domain) {
		super(name, compRes, binary, domain);

	}

	private ForwarderType type;

	public ForwarderType getType() {
		return type;
	}

	public void setType(ForwarderType type) {
		this.type = type;
	}
}
