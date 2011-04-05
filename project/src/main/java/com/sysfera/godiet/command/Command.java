package com.sysfera.godiet.command;

import com.sysfera.godiet.exceptions.CommandExecutionException;

/**
 * Interface of all GoDiet commnand
 * @author phi
 *
 */
public interface Command {

	/**
	 * Command description
	 * @return The command description
	 */
	public abstract String getDescription();
	
	/**
	 * Execute command
	 * @throws CommandExecutionException if can't execute
	 */
	public void execute() throws CommandExecutionException;
}
