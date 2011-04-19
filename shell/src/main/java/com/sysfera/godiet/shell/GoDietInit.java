package com.sysfera.godiet.shell;

import com.sysfera.godiet.command.InitGoDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;

public class GoDietInit {
	
	public GoDietInit() {
		InitGoDietCommand init = new InitGoDietCommand();
		try {
			init.execute();
		} catch (CommandExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
