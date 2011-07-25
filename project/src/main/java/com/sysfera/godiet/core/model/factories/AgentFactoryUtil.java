package com.sysfera.godiet.core.model.factories;

import java.util.List;

import com.sysfera.godiet.common.model.generated.CommandLine.Parameter;
import com.sysfera.godiet.common.model.generated.Env;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.generated.Var;
import com.sysfera.godiet.core.model.softwares.SoftwareManager;

/**
 * Some utils to set default configuration and running command for agent
 * 
 * @author phi
 * 
 */
public class AgentFactoryUtil {

	
	/**
	 * PATH={phyNode.getEnv(Path)}:$PATH
	 * OMNIORB_CONFIG={phyNode.scratchdir}/{omninames.id}.cfg nohup
	 * {AgentBinaryName} -c {phyNode.scratchDir}/{AgentName}.cfg
	 * [agentParameters]> {phyNode.scratchdir}/{AgentName}.out 2>
	 * {phyNode.scratchdir}/{AgentName}.err &
	 * 
	 * @param softManaged
	 * 
	 */
	protected static void settingRunningCommand(final OmniNames omniName,
			final SoftwareManager<? extends Software> softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getScratch()
				.getDir();
		Software agenteDescription = softManaged.getSoftwareDescription();
		
		

		
		
		//Add all environment node
		Env env = softManaged.getPluggedOn().getEnv();
		if(env != null) {
			List<Var> vars = env.getVar();
			if(vars != null)
			{
				for (Var var : vars) {
					command+= " " + var.getName() +"=" +var.getValue()+" "; 
				}
			}
		}
		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniName.getId() + ".cfg";
		command += omniOrbconfig + " ";

		// nohup {binaryName}
		command += "nohup " + agenteDescription.getBinary().getName()
				+ " ";

		
		// [agentParameters]
		List<Parameter> parameters = agenteDescription.getBinary().getCommandLine().getParameter();
		for (Parameter parameter : parameters) {
			command += parameter.getString() + " ";
		}
		// > {phyNode.scratchdir}/{MAName}.out
		command += "> " + scratchDir + "/" + agenteDescription.getId()
				+ ".out ";
		// 2> {phyNode.scratchdir}/{MAName}.err
		command += "2> " + scratchDir + "/" + agenteDescription.getId()
				+ ".err &";
		softManaged.setRunningCommand(command);

	}
}
