package com.sysfera.godiet.remote;

import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;

/**
 * 
 * 
 * @author phi
 * 
 */
public class RemoteCommandBuilder {

	
	
	public static String buildRunCommand(SoftwareManager softManaged,
			Node remoteNode)
	{
		if(softManaged.getSoftwareDescription() instanceof OmniNames)
			return buildOmniNamesCommand( softManaged,
					 remoteNode);
		throw new IllegalArgumentException("Cannot build command for " +softManaged.getSoftwareDescription().getId() );
		
	}
	
	/**
	 * Build the omniNames launching command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg +
	 * nohup {OmniNamesBinary} + -start -always &
	 * 
	 * @param softManaged
	 * @param remoteNode
	 * @return
	 */
	private static String buildOmniNamesCommand(SoftwareManager softManaged,
			Node remoteNode) {
		String command = "";
		command += "OMNINAMES_LOGDIR="
				+ remoteNode.getDisk().getScratch().getDir() +"/";
		command += " ";
		command += "OMNIORB_CONFIG="
				+ remoteNode.getDisk().getScratch().getDir() +"/"
				+ softManaged.getSoftwareDescription().getId() + ".cfg";
		command += " nohup ";
		command += softManaged.getSoftwareDescription().getConfig()
				.getRemoteBinary();
		
		command += " ";
		command += "-start -always &";
		return command;
	}
}
