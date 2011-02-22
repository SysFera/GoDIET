package com.sysfera.godiet.Model.xml;

import com.sysfera.godiet.Model.Forwarder.ForwarderType;
import com.sysfera.godiet.Model.physicalresources.ComputeResource;
import com.sysfera.godiet.Model.xml.generated.Agent;

public class Forwarder extends Agent {
	public final static String DIET_FORWARDERBINARY = "dietForwarder";

	public enum ForwarderType {
		SERVER, CLIENT
	}

	public Forwarder() {

	}

	private ForwarderType type;

	public ForwarderType getType() {
		return type;
	}

	public void setType(ForwarderType type) {
		this.type = type;
	}
}
