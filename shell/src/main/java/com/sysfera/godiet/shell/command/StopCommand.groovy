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

import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.Diet
import com.sysfera.godiet.shell.GoDietSh


/**
 * The launchPlatform command.
 *
 * 
 * @author phi
 */
class StopCommand
    extends ComplexCommandSupport
{
    StopCommand(final Shell shell) {
		super(shell, "stop", "st");
       this.functions = ['services','agents','all'] ;
    }


	def do_services = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.stopServices();
	}

	def do_agents = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.stopAgents();
	}

	def do_all = {
		GoDietSh goDietShell = shell;
		Diet rm = goDietShell.getDiet();
		rm.stopAgents();
		rm.stopServices();
	}
}

