package com.sysfera.godiet.factories;

import com.sysfera.godiet.Model.xml.generated.Config;
import com.sysfera.godiet.Model.xml.generated.Forwarder;
import com.sysfera.godiet.Model.xml.generated.Gateway;

public class ForwarderFactory {
	private static String FORWARDERBINARY = "dietForwarder";

	public static enum ForwarderType {
		CLIENT, SERVER;
	}

	public Forwarder create(Gateway gateway, ForwarderFactory.ForwarderType type) {
		Forwarder forwarder = new Forwarder();
		Config config = new Config();
		config.setServer(gateway.getRef());
		config.setRemoteBinary(FORWARDERBINARY);
		forwarder.setConfig(config);
		forwarder.setId("DietForwarder-" + gateway.getId() + "-" + type);
		switch (type) {
		case CLIENT:
			forwarder.setType("CLIENT");
			break;

		case SERVER:
			forwarder.setType("SERVER");
			break;

		default:
			break;
		}
		return forwarder;
	}
}
