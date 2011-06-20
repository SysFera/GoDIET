package com.sysfera.godiet.shell.command

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
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
			'all',
		];
	}

	def do_services = {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		List<SoftwareManager> services = rm.dietModel.omninames
		launchSoftwares(services)
	}

	private launchSoftware(SoftwareManager soft) {
		if(soft.state.status.equals(State.INCUBATE)) {
			io.println("Prepare ${soft.softwareDescription.id}");
			soft.prepare();
		}

		try{
			io.println("Start ${soft.softwareDescription.id}")

			soft.start();
			io.println(" Done")
		}catch(LaunchException e) {
			io.err.println(e.getMessage())
		}
	}

	private launchSoftwares(List<SoftwareManager> softwares) {
		softwares.each { launchSoftware(it) }
	}
	private launchMa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		List<SoftwareManager> mas = rm.dietModel.masterAgents
		launchSoftwares(mas)
	}
	private launchLa() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		List<SoftwareManager> la = rm.dietModel.localAgents
		launchSoftwares(la)
	}


	private launchForwarders() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		List<SoftwareManager> forwarders = rm.dietModel.forwarders

		boolean error = false;
		for (DietResourceManaged forwarder : forwarders) {
			try {
				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("SERVER")) {
					launchSoftware(forwarder)
				}
			} catch (LaunchException e) {
				io.err.println("Unable to run Forwarder "
						+ forwarder.getSoftwareDescription().getId());
				error = true;
			}
		}

		if(error == true) {
			io.err.println("Abort forwarders deployement");
			return;
		}
		try {
			for (DietResourceManaged forwarder : forwarders) {

				Forwarder forwarderDescription = (Forwarder) forwarder
						.getSoftwareDescription();
				if (forwarderDescription.getType().equals("CLIENT")) {
					launchSoftware(forwarder)
				}
			}
		} catch (LaunchException e) {
			io.err.println("Unable to run Forwarder "
					+ forwarder.getSoftwareDescription().getId());
			io.err.println("Abort forwarders deployement");
		}
	}


	private launchSeds() {
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		List<SoftwareManager> seds = rm.dietModel.seds
		launchSoftwares(seds)
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

	def do_all = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.launchServices();
		rm.launchAgents();
	}

	def do_software = { arg ->

		assert arg.size() == 1 , 'Command start software requires at least one argument: the software id'
		String argument = arg.head()
		GoDietSh goDietShell = shell;
		ResourcesManager rm = goDietShell.getDiet().getRm();
		SoftwareManager software = rm.getDietModel().getManagedSoftware(
				arg);
		if (software == null){
			io.err.println("Unable to find " + arg);
			return;
		}
		launchSoftware(software)
	}
}

