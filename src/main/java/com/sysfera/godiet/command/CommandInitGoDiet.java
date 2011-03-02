package com.sysfera.godiet.command;

import com.sysfera.godiet.Model.xml.generated.GoDietConfiguration;
import com.sysfera.godiet.exceptions.CommandExecutionException;

/**
 * Command to initialize GoDiet
 * @author phi
 *
 */
public class CommandInitGoDiet implements Command{

	
	private GoDietConfiguration configuration;

	@Override
	public String getDescription() {
		return "Initialize configuration";
	}

	@Override
	public void execute() throws CommandExecutionException {
		
		
	}
}
