package com.sysfera.godiet.command.init;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.model.generated.GoDietConfiguration;

/**
 * Command to initialize GoDiet
 * @author phi
 *
 */
public class InitGoDietCommand implements Command{

	
	private GoDietConfiguration configuration;

	@Override
	public String getDescription() {
		return "Initialize configuration";
	}

	@Override
	public void execute() throws CommandExecutionException {
		
		
	}
}
