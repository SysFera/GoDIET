package com.sysfera.godiet.shell.command

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.model.states.ResourceState.State;
import com.sysfera.godiet.shell.GoDietSh


/**
 * The launchPlatform command.
 *
 * 
 * @author phi
 */
class StopCommand
extends ComplexCommandSupport {
	StopCommand(final Shell shell) {
		super(shell, "stop", "st");
		this.functions = [
			'software',
			'services',
			'agents',
			'seds',
			'all',
		];
	}

	def do_services = {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> services = rm.dietModel.omninames
		stopSoftwares(services)
	}

	private stopSoftware(SoftwareManager soft) {
		io.print("Stop ${soft.softwareDescription.id}")
		soft.stop();
		io.println(" Done")
	}

	private stopSoftwares(List<SoftwareManager> softwares) {
		softwares.each { stopSoftware(it) }
	}
	private stopMa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> mas = rm.dietModel.masterAgents
		stopSoftwares(mas)
	}
	private stopLa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> la = rm.dietModel.localAgents
		stopSoftwares(la)
	}
	private stopForwarders() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> forwarders = rm.dietModel.forwarders
		stopSoftwares(forwarders)
	}

	private stopSeds() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> seds = rm.dietModel.seds
		stopSoftwares(seds)
	}
	def do_agents = {
		try{
			stopForwarders();
			stopMa();
			stopLa();
		}catch(StopException e) {
			io.err.println("Stop error")
		}
	}
	def do_seds = {
		try{
			stopSeds();
		}catch(StopException e) {
			io.err.println("Stop error")
		}
	}

	def do_all = {
		do_seds
		do_agents
		do_services
	}

	def do_software = { arg ->
		assert arg.size() == 1 , 'Command stop software requires at least one argument : the software id'
		String argument = arg.head()

		DietManager diet = ((GoDietSh)shell).godiet.model.dietModel
		SoftwareManager<? extends Software>  software = diet.getManagedSoftware(arg)
		if (software == null){
			io.err.println("Unable to find " + arg);
			return;
		}
		stopSoftware(software)
	}
}

