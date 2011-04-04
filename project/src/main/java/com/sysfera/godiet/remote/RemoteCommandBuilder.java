package com.sysfera.godiet.remote;

import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Software;

/**
 * 
 * 
 * @author phi
 * 
 */
public class RemoteCommandBuilder {

	public static String buildRunCommand(SoftwareManager softManaged,
			Node remoteNode) {
		if (softManaged.getSoftwareDescription() instanceof OmniNames)
			return buildOmniNamesCommand(softManaged, remoteNode);
		throw new IllegalArgumentException("Cannot build command for "
				+ softManaged.getSoftwareDescription().getId());

	}

	/**
	 * Build the omniNames launching command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg + nohup
	 * {OmniNamesBinary} + -start -always > {scratch_runtime}/OmniNames.out 2>
	 * {scratch_runtime}/OmniNames.err
	 * 
	 * @param softManaged
	 * @param remoteNode
	 * @return
	 */
	private static String buildOmniNamesCommand(SoftwareManager softManaged,
			Node remoteNode) {
		String command = "";
		String scratchDir = remoteNode.getDisk().getScratch().getDir();
		Software softwareDescription = softManaged.getSoftwareDescription();
		command += "OMNINAMES_LOGDIR=" + scratchDir + "/";
		command += " ";

		command += "OMNIORB_CONFIG=" + scratchDir + "/"
				+ softwareDescription.getId() + ".cfg";
		command += " nohup ";
		command += softwareDescription.getConfig().getRemoteBinary();

		command += " ";
		command += "-start -always >";
		command += scratchDir + "/OmniNames.out 2> ";
		command += scratchDir + "/OmniNames.err ";
		return command;
	}

}
