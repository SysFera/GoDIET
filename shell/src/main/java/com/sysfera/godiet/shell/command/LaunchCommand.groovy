package com.sysfera.godiet.shell.command

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.shell.GoDietSh


/**
 * The launchPlatform command.
 *
 * 
 * @author phi
 */
class LaunchCommand
extends ComplexCommandSupport {
	LaunchCommand(final Shell shell) {
		super(shell, "launch", "t");
		this.functions = ['services', 'agents', 'all','relaunch'];
	}

	def do_services = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.launchServices();
	}

	def do_agents = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.launchAgents();
	}

	def do_all = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.launchServices();
		rm.launchAgents();
	}
	
	def do_relaunch = { it ->
		assert it.size() == 1 , 'Init key need one argument : names of software to relaunch'
		String argument = it.head()
		
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.relauch it
		
	}
}

