package com.sysfera.godiet.shell;

import com.sysfera.godiet.command.init.InitGoDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;

/**
 * Helper to init the shell command
 * 
 * @author phi
 *
 */
public class GoDietShInit {
	
	/**
	 * Load GoDietConfiguration
	 * 
	 */
	public void init() {
		InitGoDietCommand init = new InitGoDietCommand();
		try {
			init.execute();
		} catch (CommandExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
