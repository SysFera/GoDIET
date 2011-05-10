
/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sysfera.godiet.shell.command

import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.ResourceState.State;
import com.sysfera.godiet.shell.GoDietSh
import java.text.SimpleDateFormat;


/**
 * The status commands.
 *
 * 
 * @author phi
 */
class StatusCommand
extends ComplexCommandSupport {
	private static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	StatusCommand(final Shell shell) {
		super(shell, "status", "s")
		this.functions = [
			'services',
			'ma',
			'la',
			'sed',
			'all'
		]
	}

	def printStatus = { String elementName, resources ->
		if(resources.size() == 0) {
			io.out.println("\n@|bold No ${elementName}|@")
			return
		}
		io.out.println("")
		io.out.println("@|bold ${elementName}|@ Status (${resources.size()}) : ")
		io.out.println("@|bold Label\t\tStatus\t\tSince\t\tPlugged\t\tCause|@")
		io.out.println("---------------------------------------------------------")
		try{
			for (Software resource : resources) {
				try{
					def coloredStatus
					String cause ='-'
					String since = sdf.format(resource.stateController.lastTransition)
					switch (resource.state.status) {
						case State.UP:
							coloredStatus =  "@|green ${resource.state.status}|@"
							break;
						case State.DOWN:
							coloredStatus =  "@|yellow ${resource.state.status}|@"
							break;
						case State.ERROR:
							coloredStatus =  "@|BG_RED,BLACK ${resource.state.status}|@"
							cause = resource.stateController.errorCause.message
							break;
						default:
							coloredStatus = "@|BLUE ${resource.state.status}|@"
							break;
					}
					io.out.println "${resource.softwareDescription.id}\t${coloredStatus}\t${since}\t${resource.pluggedOn.id}\t${cause}"
				}catch (Exception e) {
					io.err.println("Status ${resource.softwareDescription.id} printing error",e)
				}
			}
		}catch (Exception e) {
			io.err.println("Status ${resource.softwareDescription.id} printing error",e)
		}
	}

	def do_services = {
		Diet diet = ((GoDietSh)shell).getDiet()
		List omniNames = diet.getRm().getDietModel().omninames
		printStatus("OmniNames",omniNames)
	}

	def do_sed = {
		Diet diet = ((GoDietSh)shell).getDiet()
		List seds = diet.getRm().getDietModel().seds
		printStatus("Seds",seds)
	}
	def do_forwarders = {
		Diet diet = ((GoDietSh)shell).getDiet()
		List forwarders = diet.getRm().getDietModel().forwaders
		printStatus("Forwarders",forwarders)
	}
	def do_ma = {
		Diet diet = ((GoDietSh)shell).getDiet()
		List mas = diet.getRm().getDietModel().masterAgents
		printStatus("Master agents",mas)
	}
	def do_la = {
		Diet diet = ((GoDietSh)shell).getDiet()
		List las = diet.getRm().getDietModel().localAgents
		printStatus("Local agents",las)
	}
	def do_all = {
		do_services()
		do_forwarders()
		do_ma()
		do_la()
		do_sed()
	}
}
