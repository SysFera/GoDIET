package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.managers.Diet;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;
import com.sysfera.godiet.model.utils.ResourceUtil;


/**
 * Managed MA factory
 * 
 * @author phi
 *
 */
public class MasterAgentFactory {
	// Needed to find the omniNames of the Domain
	private final Diet dietPlatform;

	
	public MasterAgentFactory(Diet dietPlatform) {
		this.dietPlatform = dietPlatform;
	}
	/**
	 * Create a managed MasterAgent given his description. Check validity. Set the default option if needed (like 
	 * command launch).
	 * @param masterAgentDescription
	 * @return The managed MasterAgent
	 * @throws DietResourceCreationException 
	 */
	public DietResourceManaged create(MasterAgent masterAgentDescription) throws DietResourceCreationException
	{
		DietResourceManaged MAManaged = new DietResourceManaged();
		MAManaged.setManagedSoftware(masterAgentDescription);
		settingConfigurationOptions(MAManaged);
		settingMaRunningCommand(MAManaged);
		return MAManaged;
	}
	
	/**
	 * Init default value
	 * @param masterAgent
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManaged masterAgent)
			throws DietResourceCreationException {
		if (masterAgent.getPluggedOn() == null) {
			throw new DietResourceCreationException(masterAgent.getSoftwareDescription()
					.getId() + " not plugged on physical resource");
		}

		Options opts = new ObjectFactory().createOptions();

		Option type = new Option();
		type.setKey("agentType");
		type.setValue("DIET_MASTER_AGENT");
		Option name = new Option();
		name.setKey("name");
		name.setValue(masterAgent.getSoftwareDescription().getId());
		opts.getOption().add(type);
		opts.getOption().add(name);
		
		masterAgent.getSoftwareDescription().setCfgOptions(opts);
		

	}
	
	/**
	 * PATH={phyNode.getEnv(Path)}:$PATH
	 * OMNIORB_CONFIG={phyNode.scratchdir}/{omninames.id}.cfg nohup
	 * {MABinaryname} --config-file  
	 * {phyNode.scratchDir}/{MAName}.cfg >
	 * {phyNode.scratchdir}/{MAName}.out 2>
	 * {phyNode.scratchdir}/{MAName}.err &
	 * 
	 * @param softManaged
	 * 
	 */
	private void settingMaRunningCommand(SoftwareManager softManaged) {
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getDisk().getScratch()
				.getDir();
		MasterAgent masterAgentDescription = (MasterAgent) softManaged
				.getSoftwareDescription();
		//Env PATH
		String envPath = ResourceUtil.getEnvValue(softManaged.getPluggedOn(),"PATH");
		command+= "PATH="+envPath+":$PATH ";
		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		OmniNames omniName = dietPlatform.getOmniName(softManaged);
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniName.getId() + ".cfg";
		command += omniOrbconfig + " ";

		// nohup {binaryName}
		command += "nohup "
				+ masterAgentDescription.getConfig().getRemoteBinary() + " ";
		
		
		
		//--config-file
		command += "-c " + scratchDir + "/"
				+ masterAgentDescription.getId() + ".cfg ";
		// > {phyNode.scratchdir}/{MAName}.out
		command += "> " + scratchDir + "/" + masterAgentDescription.getId()
				+ ".out ";
		// 2> {phyNode.scratchdir}/{MAName}.err
		command += "2> " + scratchDir + "/" + masterAgentDescription.getId()
				+ ".err &";
		softManaged.setRunningCommand(command);
		
	}
}
