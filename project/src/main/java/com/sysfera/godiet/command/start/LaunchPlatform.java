package com.sysfera.godiet.command.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.prepare.PrepareAgentsCommand;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
/**
 * 
 * InitForwarder Check connectivity
 * @author phi
 *
 */
public class LaunchPlatform implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() throws CommandExecutionException {
		
		PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
		prepareCommand.setRm(rm);
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);
	

		// Agents commands
		InitForwardersCommand initForwardersCommand = new InitForwardersCommand();
		initForwardersCommand.setRm(rm);

		PrepareAgentsCommand prepareAgents = new PrepareAgentsCommand();
		prepareAgents.setRm(rm);

		StartForwardersCommand launchForwarders = new StartForwardersCommand();
		launchForwarders.setRm(rm);

		StartAgentsCommand startAgent = new StartAgentsCommand();
		startAgent.setRm(rm);

		
		prepareCommand.execute();
		launchServicesCommand.execute();

		initForwardersCommand.execute();

		prepareAgents.execute();
	
		launchForwarders.execute();
		startAgent.execute();
	}

}
