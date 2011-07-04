
package com.sysfera.godiet.shell.command

import java.util.List;
import com.sysfera.godiet.model.generated.Software;

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;
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



	private launchSoftware(SoftwareManager soft) {
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

	private launchSoftwares(List<SoftwareManager> softwares) {
		softwares.each { launchSoftware(it) }
	}
	private launchMa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> mas = rm.dietModel.masterAgents
		launchSoftwares(mas)
	}
	private launchLa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> la = rm.dietModel.localAgents
		launchSoftwares(la)
	}


	private launchForwarders() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<DietResourceManaged<Forwarder>> forwarders = dietManager.getForwarders();
		forwarders.each {
			if (it.getType()
			.equals("SERVER")) {
				launchSoftware(it)
			}
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			io.err.println("Launching thread have been")
			log.error("FATAL: Launching thread have been interrupt",e);
			return;
		}
		forwarders.each {
			if (it.getType()
			.equals("CLIENT")) {
				launchSoftware(it)
			}
		}
	}


	private launchSeds() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> seds = rm.dietModel.seds
		launchSoftwares(seds)
	}


	def do_services = {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.godiet.model
		List<SoftwareManager> services = rm.dietModel.omninames
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
		DietManager diet = ((GoDietSh)shell).godiet.model.dietModel
		SoftwareManager<? extends Software>  software = diet.getManagedSoftware(arg)
		if (software == null){
			io.err.println("Unable to find " + arg);
			return;
		}
		launchSoftware(software)
	}
}

