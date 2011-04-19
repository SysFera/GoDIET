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


/**
 * The launchPlatform command.
 *
 * 
 * @author phi
 */
class LaunchPlatformCommand
    extends ComplexCommandSupport
{
    LaunchPlatformCommand(final Shell shell) {
		super(shell, "launch", "t");
       // this.functions = ['services','agents','seds','all'] ;
    }
    
//    def do_services = {
//		GoDietSh goDietShell = shell
//		ResourcesManager rm = goDietShell.rm;
//        if (rm.getDietModel() == null || rm.getDietModel().getOmninames().size() == 0) {
//            io.out.println('Load diet resources first') 
//        }
//        else {
//            io.out.println('Variables:') // TODO: i18n
//            
//            variables.each { key, value ->
//                // Special handling for defined methods, just show the sig
//                if (value instanceof MethodClosure) {
//                    //
//                    // TODO: Would be nice to show the argument types it will accept...
//                    //
//                    value = "method ${value.method}()"
//                }
//
//                // Need to use String.valueOf() here to avoid icky exceptions causes by GString coercion
//                io.out.println("  $key = ${String.valueOf(value)}")
//            }
//        }
//    }
//    
//    def do_seds = {
//        def classes = classLoader.loadedClasses
//        
//        if (classes.size() == 0) {
//            io.out.println("No classes have been loaded") // TODO: i18n
//        }
//        else {
//            io.out.println('Classes:') // TODO: i18n
//            
//            classes.each {
//                io.out.println("  $it")
//            }
//        }
//    }
//    
//    def do_all = {
//        if (imports.isEmpty()) {
//            io.out.println("No custom imports have been defined") // TODO: i18n
//        }
//        else {
//            io.out.println("Custom imports:") // TODO: i18n
//            
//            imports.each {
//                io.out.println("  $it")
//            }
//        }
//    }
//
//    def do_agents = {
//        def keys = Preferences.keys()
//
//        if (keys.size() == 0) {
//            io.out.println('No preferences are set')
//            return
//        }
//        else {
//            io.out.println('Preferences:')
//            keys.each {
//                def value = Preferences.get(it, null)
//                println("    $it=$value")
//            }
//        }
//        return
//    }
}

