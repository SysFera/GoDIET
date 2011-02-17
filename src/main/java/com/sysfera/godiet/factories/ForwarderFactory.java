package com.sysfera.godiet.factories;

import com.sysfera.godiet.Model.Forwarder;
import com.sysfera.godiet.Model.Option;
import com.sysfera.godiet.Model.physicalresources.GatewayResource;
/**
 *  A concrete forwarder factory
 * @author phi
 *
 */
public class ForwarderFactory {

	/**
	 *  Create a new instance of Gateway. 
	 * @param gateway the gateway on which the forwarder will be run
	 * @param type The Type of forwarder
	 * @return A new Forwarder
	 */
	public Forwarder create(GatewayResource gateway,
			Forwarder.ForwarderType type) {
		Forwarder forwarder = new Forwarder(gateway.getName()
				+ "-DietForwarder", gateway, Forwarder.DIET_FORWARDERBINARY,
				gateway.getDomain());

		// Add options
		forwarder.getElementCfg().addOption(new Option("accept", ".*"));
		String rejectIP = gateway.getAccessMethod("ssh").getServer();
		forwarder.getElementCfg().addOption(
				new Option("reject", rejectIP.replaceAll("[.]", "\\\\.")));
		forwarder.setType(type);
		return forwarder;

	}
	

}
