package com.sysfera.godiet.core.model.factories;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.model.generated.Env;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.SoftwareFile;
import com.sysfera.godiet.common.model.generated.Var;
import com.sysfera.godiet.core.managers.DietManager;
import com.sysfera.godiet.core.model.configurator.CommandLineBuilderService;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;
import com.sysfera.godiet.core.model.validators.SedRuntimeValidatorImpl;

/**
 * Managed sed factory
 * 
 * @author phi
 * 
 */
@Component
public class SedFactory {

	@Autowired
	private DietManager dietManager;
	@Autowired
	private SoftwareController softwareController;
	
	private RuntimeValidator<DietResourceManaged<Sed>> validator;
	@Autowired
	private ConfigurationFileBuilderService configurationFileBuilderService;
	
	@Autowired
	private CommandLineBuilderService commandLineBuilderService;
	
	@PostConstruct
	public void postConstruct() {
		this.validator = new SedRuntimeValidatorImpl(dietManager);
	}

	/**
	 * Create a managed sed given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * @param sedDescription
	 * @return The managed Sed
	 * @throws IncubateException
	 */

	public DietResourceManaged<Sed> create(Sed sedDescription,
			Resource pluggedOn, OmniNamesManaged omniNames)
			throws IncubateException {
		DietResourceManaged<Sed> sedManaged = new DietResourceManaged<Sed>(
				sedDescription, pluggedOn, softwareController, validator,
				omniNames);
		try {
			
			
			configurationFileBuilderService.build(sedManaged);
			
			//Add a ref to the omniNames's config file
			sedManaged.getConfigurationFiles().putAll(omniNames.getConfigurationFiles());

			commandLineBuilderService.build(sedManaged);
			//TODO: DO something better
			//Decorate the commandline with the deployement context
			String scratchDir = sedManaged.getPluggedOn().getScratch()
			.getDir();
			String prefix = "";
			{
				
				//Add all environment node
				Env env = sedManaged.getPluggedOn().getEnv();
				if(env != null) {
					List<Var> vars = env.getVar();
					if(vars != null)
					{
						for (Var var : vars) {
							prefix+= " " + var.getName() +"=" +var.getValue()+" "; 
						}
					}
				}
				// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
				String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
						+ omniNames.getSoftwareDescription().getId() + ".cfg";
				prefix += omniOrbconfig + " ";

				// nohup {binaryName}
				prefix += "nohup ";
			}
			String suffix="";
			{
			
				// > {phyNode.scratchdir}/{MAName}.out
				suffix += "> " + scratchDir + "/" + sedDescription.getId()
						+ ".out ";
				// 2> {phyNode.scratchdir}/{MAName}.err
				suffix += "2> " + scratchDir + "/" + sedDescription.getId()
						+ ".err &";
				
			}
			String commandLine = prefix +sedManaged.getRunningCommand() + suffix;
			sedManaged.setRunningCommand(commandLine);
			
		} catch (ConfigurationBuildingException e) {
			throw new IncubateException("Unable to create configurations file ", e);
		}
		return sedManaged;
	}

}
