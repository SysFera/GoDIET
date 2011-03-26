package com.sysfera.godiet.remote;

import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Node;

/**
 * 
 * 
 * @author phi
 * 
 */
public class RemoteCommandBuilder {

	/**
	 * Build the omniNames launching command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/{omniNamesId}/log/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{DomainName}/{omniNamesId}.cfg +
	 * {OmniNamesBinary} + -start
	 * 
	 * @param softManaged
	 * @param remoteNode
	 * @return
	 */
	public static String buildOmniNamesCommand(SoftwareManager softManaged,
			Node remoteNode) {
		String command = "";
		command += "OMNINAMES_LOGDIR="
				+ remoteNode.getDisk().getScratch().getDir() +"/"
				+ remoteNode.getDomain().getLabel() +"/"
				+ softManaged.getSoftwareDescription().getId() + "/log/";
		command += " ";
		command += "OMNIORB_CONFIG="
				+ remoteNode.getDisk().getScratch().getDir() +"/"
				+ remoteNode.getDomain().getLabel() +"/"
				+ softManaged.getSoftwareDescription().getId() + ".cfg";
		command += " ";
		command += softManaged.getSoftwareDescription().getConfig()
				.getRemoteBinary();
		
		command += " ";
		command += "-start";
		return command;
	}
}
