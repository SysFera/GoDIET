package com.sysfera.godiet.model.factories;

import java.lang.reflect.Array;
import java.util.List;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.model.generated.Binary;
import com.sysfera.godiet.model.generated.CommandLine;
import com.sysfera.godiet.model.generated.Env;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Forwarders;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.SoftwareFile;
import com.sysfera.godiet.model.generated.SoftwareFile.Template;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.generated.Var;
import com.sysfera.godiet.model.softwares.DietResourceManaged;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareController;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed Forwarder factory
 * 
 * @author phi
 * 
 */
public class ForwardersFactory {
	private static final String DEFAULT_BINARYNAME = "dietForwarder";

	private final SoftwareController softwareController;
	private final ConfigurationFileBuilderService configurationFileBuilderService;
	private final RuntimeValidator<DietResourceManaged<Forwarder>> forwarderValidator;

	public static enum ForwarderType {
		CLIENT("CLIENT"), SERVER("SERVER");
		public final String label;

		private ForwarderType(String label) {
			this.label = label;
		}

	}

	public ForwardersFactory(SoftwareController softwareController,

	RuntimeValidator<DietResourceManaged<Forwarder>> forwarderValidator,
			ConfigurationFileBuilderService configurationFileBuilderService) {
		this.softwareController = softwareController;
		this.forwarderValidator = forwarderValidator;
		this.configurationFileBuilderService = configurationFileBuilderService;
	}

	/**
	 * Create a managed diet resource. Check description validity and add
	 * default parameters if needed.
	 * 
	 * @param forwarders
	 *            Forwarders description
	 * @param connection
	 * @return The Managed forwarders. DietResourceManaged[0] the client and
	 *         DietResourceManaged[1] the server
	 * @throws IncubateException
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */

	public DietResourceManaged<Forwarder>[] create(Forwarders forwarders,
			Resource clientPluggedOn, OmniNamesManaged omniNamesClient,
			Resource serverPluggedOn, OmniNamesManaged omniNamesServer,
			Ssh connection) throws IncubateException,
			DietResourceCreationException {

		@SuppressWarnings("unchecked")
		DietResourceManaged<Forwarder>[] forwardersManager = (DietResourceManaged<Forwarder>[]) Array
				.newInstance(DietResourceManaged.class, 2);

		DietResourceManaged<Forwarder> clientForwarderManager = new DietResourceManaged<Forwarder>(
				forwarders.getClient(), clientPluggedOn, softwareController,
				forwarderValidator, omniNamesClient);
		DietResourceManaged<Forwarder> serverForwarderManager = new DietResourceManaged<Forwarder>(
				forwarders.getServer(), serverPluggedOn, softwareController,
				forwarderValidator, omniNamesServer);
		// TODO:something better
		try {
			{ // Client
				SoftwareFile sf = new ObjectFactory().createSoftwareFile();
				Template template = new ObjectFactory()
						.createSoftwareFileTemplate();
				template.setName("forwarder_template.config");
				sf.setId(clientForwarderManager.getSoftwareDescription()
						.getId());
				sf.setTemplate(template);
				clientForwarderManager.getSoftwareDescription().getFile()
						.add(sf);
				
				
				configurationFileBuilderService.build(clientForwarderManager);
				clientForwarderManager.getConfigurationFiles().putAll(omniNamesClient.getConfigurationFiles());

			}
			{// Server
				SoftwareFile sf = new ObjectFactory().createSoftwareFile();
				Template template = new ObjectFactory()
						.createSoftwareFileTemplate();
				template.setName("forwarder_template.config");
				sf.setId(serverForwarderManager.getSoftwareDescription()
						.getId());
				sf.setTemplate(template);
				serverForwarderManager.getSoftwareDescription().getFile()
						.add(sf);
				
				
				configurationFileBuilderService.build(serverForwarderManager);
				serverForwarderManager.getConfigurationFiles().putAll(omniNamesServer.getConfigurationFiles());
			}

		} catch (ConfigurationBuildingException e) {
			new IncubateException("Unable to create configurations file ", e);
		}
	
		buildForwarderCommand(clientForwarderManager, serverForwarderManager,
				omniNamesClient.getSoftwareDescription(),
				omniNamesServer.getSoftwareDescription(), connection);

		forwardersManager[0] = clientForwarderManager;
		forwardersManager[1] = serverForwarderManager;

		return forwardersManager;

	}

	/**
	 * Build the diet forwarder running command
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg TODO: What's the hell
	 * 
	 * @param connection
	 * 
	 * @param softManaged
	 * @return
	 */
	private void buildForwarderCommand(
			SoftwareManager<Forwarder> managedClient,
			SoftwareManager<Forwarder> managedServer,
			OmniNames omniNamesClient, OmniNames omniNamesServer, Ssh connection) {
		buildForwarderServerCommand(managedClient, managedServer,
				omniNamesClient, omniNamesServer);
		buildForwarderClientCommand(managedClient, managedServer,
				omniNamesClient, omniNamesServer, connection);
	}

	/**
	 * PATH={phyNode.getEnv(Path)}:$PATH
	 * OMNIORB_CONFIG={phyNode.scratchdir}/{omninames.id}.cfg nohup
	 * {forwarderBinaryname} --name {forwarderName} --net-config
	 * {phyNode.scratchDir}/{forwarderName}.cfg >
	 * {phyNode.scratchdir}/{forwarderName}.out 2>
	 * {phyNode.scratchdir}/{forwarderName}.err &
	 * 
	 * @param managedClient
	 * @param managedServer
	 */
	private void buildForwarderServerCommand(
			SoftwareManager<Forwarder> managedClient,
			SoftwareManager<Forwarder> managedServer,
			OmniNames omniNamesClient, OmniNames omniNamesServer) {
		String command = "";
		String scratchDir = managedServer.getPluggedOn().getScratch().getDir();
		Forwarder forwarderDescription = (Forwarder) managedServer
				.getSoftwareDescription();

		// TODO: do something better for default
		Binary b = new ObjectFactory().createBinary();
		b.setName(DEFAULT_BINARYNAME);
		forwarderDescription.setBinary(b);

		CommandLine cl = new ObjectFactory().createCommandLine();
		b.setCommandLine(cl);
		//

		// Add all environment node
		Env env = managedServer.getPluggedOn().getEnv();
		if (env != null) {
			List<Var> vars = env.getVar();
			if (vars != null) {
				for (Var var : vars) {
					command += " " + var.getName() + "=" + var.getValue() + " ";
				}
			}
		}

		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniNamesServer.getId() + ".cfg";
		command += omniOrbconfig + " ";

		// nohup {binaryName}
		command += "nohup " + forwarderDescription.getBinary().getName() + " ";

		// --name
		command += "--name " + forwarderDescription.getId() + " ";

		// --net-config
		command += "--net-config " + scratchDir + "/"
				+ forwarderDescription.getId() + ".cfg ";

		// > {phyNode.scratchdir}/{forwarderName}.out
		command += "> " + scratchDir + "/" + forwarderDescription.getId()
				+ ".out ";
		// 2> {phyNode.scratchdir}/{forwarderName}.err
		command += "2> " + scratchDir + "/" + forwarderDescription.getId()
				+ ".err &";
		managedServer.setRunningCommand(command);
	}

	/**
	 * 
	 * PATH={phyNode.getEnv(Path)}:$PATH
	 * OMNIORB_CONFIG={phyNode.scratchdir}/{omninames.id}.cfg nohup
	 * {forwarderBinaryname} --name {forwarderName} --net-config
	 * {phyNode.scratchDir}/{forwarderName}.cfg --peer-name
	 * {remoteForwarderName} --ssh-host {serverHost} --ssh-login {serverLogin}
	 * --remote-host 127.0.0.1 -C &
	 * 
	 * @param managedClient
	 * @param managedServer
	 */

	private void buildForwarderClientCommand(
			SoftwareManager<Forwarder> managedClient,
			SoftwareManager<Forwarder> managedServer,
			OmniNames omniNamesClient,

			OmniNames omniNamesServer, Ssh connection) {
		String command = "";
		String scratchDir = managedClient.getPluggedOn().getScratch().getDir();
		Forwarder forwarderDescription = (Forwarder) managedClient
				.getSoftwareDescription();
		// TODO: do something better for default
		Binary b = new ObjectFactory().createBinary();
		b.setName(DEFAULT_BINARYNAME);
		forwarderDescription.setBinary(b);

		CommandLine cl = new ObjectFactory().createCommandLine();
		b.setCommandLine(cl);
		//
		// Add all environment node
		Env env = managedClient.getPluggedOn().getEnv();
		if (env != null) {
			List<Var> vars = env.getVar();
			if (vars != null) {
				for (Var var : vars) {
					command += " " + var.getName() + "=" + var.getValue() + " ";
				}
			}
		}

		// find the OmniOrbConfig file on the remote host to set OmniOrb.cfg
		String omniOrbconfig = "OMNIORB_CONFIG=" + scratchDir + "/"
				+ omniNamesClient.getId() + ".cfg";
		command += omniOrbconfig + " ";

		// nohup {binaryName}
		command += "nohup " + forwarderDescription.getBinary().getName() + " ";
		// {name}
		command += "--name " + forwarderDescription.getId() + " ";
		// --net-config {phyNode.scratchDir}/{forwarderName}.cfg
		command += "--net-config " + scratchDir + "/"
				+ forwarderDescription.getId() + ".cfg ";
		// --peer-name {remoteForwarderName}
		command += "--peer-name "
				+ managedServer.getSoftwareDescription().getId() + " ";
		// --ssh-host {remoteHost}
		command += " --ssh-host " + connection.getServer() + " ";
		// --ssh-login {remoteLogin}
		command += "--ssh-login " + connection.getLogin() + " ";
		// --remote-port {remotePort} -C &

		// --remote-host 127.0.0.1 -C
		command += "--remote-host 127.0.0.1 -C ";

		// > {phyNode.scratchdir}/{forwarderName}.out
		command += "> " + scratchDir + "/" + forwarderDescription.getId()
				+ ".out ";
		// 2> {phyNode.scratchdir}/{forwarderName}.err
		command += "2> " + scratchDir + "/" + forwarderDescription.getId()
				+ ".err ";
		// &
		command += "&";

		managedClient.setRunningCommand(command);
	}

}
