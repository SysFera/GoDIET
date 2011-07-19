package com.sysfera.godiet.shell.command

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.exceptions.remote.StopException;

import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState.State;
import com.sysfera.godiet.services.PlatformService;
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
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List<SoftwareInterface<? extends Software>> services = diet.omninames
		stopSoftwares(services)
	}

	private stopSoftware(SoftwareInterface<? extends Software> soft) {
		io.print("Stop ${soft.softwareDescription.id}")
		soft.stop();
		io.println(" Done")
	}

	private stopSoftwares(List<SoftwareInterface<? extends Software>> softwares) {
		softwares.each { stopSoftware(it) }
	}
	private stopMa() {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List<SoftwareInterface<? extends Software>> mas = diet.masterAgents
		stopSoftwares(mas)
	}
	private stopLa() {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List<SoftwareInterface<? extends Software>> las = diet.localAgents
		stopSoftwares(las)
	}
	private stopForwarders() {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List<SoftwareInterface<? extends Software>> forwarders = diet.forwarders
		stopSoftwares(forwarders)
	}

	private stopSeds() {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List<SoftwareInterface<? extends Software>> seds = diet.seds
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

		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		SoftwareInterface<? extends Software> software = diet.getManagedSoftware(arg)
		if (software == null){
			io.err.println("Unable to find " + arg);
			return;
		}
		stopSoftware(software)
	}
}

