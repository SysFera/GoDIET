package com.sysfera.godiet.remote;

import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Software;

/**
 * Build the launching command of each software 
 * TODO: Refactor: Perhaps embed the buildCommandBuilder into the softwareManaged. Each managed softwares
 * create his own command (and perhaps his own kill command )
 * 
 * @author phi
 * 
 */
public class RemoteCommandBuilder {

	public static String buildRunCommand(SoftwareManager softManaged) {
		Software softwareDescription = softManaged.getSoftwareDescription();
		if (softwareDescription instanceof OmniNames)
		{
			return buildOmniNamesCommand(softManaged);
		}
		else if (softwareDescription instanceof Forwarder)
		{
			return buildForwarderCommand(softManaged);
		}
		throw new IllegalArgumentException("Cannot build command for "
				+ softwareDescription.getId());

	}

	/**
	 * Build the omniNames launching command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg + nohup
	 * {OmniNamesBinary} + -start -always > {scratch_runtime}/OmniNames.out 2>
	 * {scratch_runtime}/OmniNames.err &
	 * 
	 * @param softManaged
	 * @return
	 */
	private static String buildOmniNamesCommand(SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
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
		command += scratchDir + "/OmniNames.err &";
		return command;
	}

	/**
	 * Build the diet forwarder launching command
	 * 
	 * @param softManaged
	 * @return 
	 */
	private static String buildForwarderCommand(
			SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
		command += "OMNIORB_CONFIG=" + scratchDir + "/" ;
		command += " ";

		return command;
	}

}
