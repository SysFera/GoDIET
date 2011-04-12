package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.utils.ResourceUtil;

/**
 * Some utils to set default configuration and running command for agent
 * 
 * @author phi
 * 
 */
public class AgentFactoryUtil {

	/**
	 * Init default value
	 * 
	 * @param agent
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	protected static void settingConfigurationOptions(
			DietResourceManaged agent, String agentType)
			throws DietResourceCreationException {
		if (agent.getPluggedOn() == null) {
			throw new DietResourceCreationException(agent
					.getSoftwareDescription().getId()
					+ " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();

		Option type = new Option();
		type.setKey("agentType");
		type.setValue(agentType);
		Option name = new Option();
		name.setKey("name");
		name.setValue(agent.getSoftwareDescription().getId());

		if (agentType.equals("DIET_LOCAL_AGENT")) {
			Option parent = new Option();
			parent.setKey("parentName");
			parent.setValue(agent.getSoftwareDescription().getParent().getId());
			opts.getOption().add(parent);

		}
		opts.getOption().add(type);
		opts.getOption().add(name);

		agent.getSoftwareDescription().setCfgOptions(opts);

	}

	/**
	 * PATH={phyNode.getEnv(Path)}:$PATH
	 * OMNIORB_CONFIG={phyNode.scratchdir}/{omninames.id}.cfg nohup
	 * {AgentBinaryName} --config-file {phyNode.scratchDir}/{MAName}.cfg >
	 * {phyNode.scratchdir}/{MAName}.out 2> {phyNode.scratchdir}/{MAName}.err &
	 * 
	 * @param softManaged
	 * 
	 */
	protected static void settingRunningCommand(final Diet dietPlatform,
			final SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
		Software agenteDescription = softManaged.getSoftwareDescription();
		// Env PATH
		String envPath = ResourceUtil.getEnvValue(softManaged.getPluggedOn(),
				"PATH");
		command += "PATH=" + envPath + ":$PATH ";
		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		OmniNames omniName = dietPlatform.getOmniName(softManaged);
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniName.getId() + ".cfg";
		command += omniOrbconfig + " ";

		// nohup {binaryName}
		command += "nohup " + agenteDescription.getConfig().getRemoteBinary()
				+ " ";

		// --config-file
		command += "-c " + scratchDir + "/" + agenteDescription.getId()
				+ ".cfg ";
		// > {phyNode.scratchdir}/{MAName}.out
		command += "> " + scratchDir + "/" + agenteDescription.getId()
				+ ".out ";
		// 2> {phyNode.scratchdir}/{MAName}.err
		command += "2> " + scratchDir + "/" + agenteDescription.getId()
				+ ".err &";
		softManaged.setRunningCommand(command);

	}
}
