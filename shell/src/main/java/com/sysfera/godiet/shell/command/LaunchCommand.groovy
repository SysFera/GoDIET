
package com.sysfera.godiet.shell.command

import java.util.List

import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.exceptions.remote.LaunchException
import com.sysfera.godiet.exceptions.remote.PrepareException
import com.sysfera.godiet.model.SoftwareInterface
import com.sysfera.godiet.model.generated.Software
import com.sysfera.godiet.model.states.ResourceState
import com.sysfera.godiet.model.states.ResourceState.State
import com.sysfera.godiet.services.PlatformService
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
		super(shell, "start", "run");
		this.functions = [
			'software',
			'services',
			'agents',
			'seds',
		];
	}



	private launchSoftware(SoftwareInterface soft) {
		try{
			if(soft.state.status.equals(State.INCUBATE)) {
				io.println("Prepare ${soft.softwareDescription.id}");
				soft.prepare();
			}

			try{
				io.println("Start ${soft.softwareDescription.id}")

				soft.start();
				io.println("Done")
			}catch(LaunchException e) {
				io.err.println(e.getMessage())
			}
		}catch(PrepareException e ) {
			io.err.println("Prepare error : "+e.getMessage())
		}
	}

	private launchSoftwares(List<SoftwareInterface> softwares) {
		softwares.each { launchSoftware(it) }
	}
	private launchMa() {
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		def mas = ps.masterAgents
		launchSoftwares(mas)
	}
	private launchLa() {
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		def la = ps.localAgents
		launchSoftwares(la)
	}


	private launchForwarders() {
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		def forwardersServer = ps.forwardersServer
		forwardersServer.each {
			launchSoftware(it)
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			io.err.println("Launching thread have been")
			log.error("FATAL: Launching thread have been interrupt",e);
			return;
		}
		
		def forwardersClient = ps.forwardersClient
		forwardersClient.each {
				launchSoftware(it)
			}
		
	}


	private launchSeds() {
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		def seds = ps.seds
		launchSoftwares(seds)
	}


	def do_services = {
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		def services = ps.getOmninames()
		launchSoftwares(services)
	}
	def do_agents = {
		try{
			launchForwarders();
			launchMa();
			launchLa();
		}catch(LaunchException e) {
			io.err.println("Launching error")
		}
	}
	def do_seds = {
		try{
			launchSeds();
		}catch(LaunchException e) {
			io.err.println("Launching error")
		}
	}



	def do_software = { arg ->

		assert arg.size() == 1 , 'Command start software requires at least one argument: the software id'
		String argument = arg.head()
		GoDietSh goDietShell = shell;
		PlatformService ps = goDietShell.godiet.platformService
		
		SoftwareInterface<? extends Software>  software = ps.getManagedSoftware(arg)
		if (software == null){
			io.err.println("Unable to find " + arg);
			return;
		}
		launchSoftware(software)
	}
}

