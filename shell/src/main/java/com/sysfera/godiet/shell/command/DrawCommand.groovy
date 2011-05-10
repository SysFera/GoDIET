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

import java.util.List;
import java.util.SortedSet;

import org.codehaus.groovy.tools.shell.Command;
import org.codehaus.groovy.tools.shell.CommandRegistry;
import org.codehaus.groovy.tools.shell.CommandSupport;
import org.codehaus.groovy.tools.shell.util.SimpleCompletor;

import com.sysfera.godiet.shell.GoDietSh;

/**
 * The 'help' command.
 *
 * @version $Id: HelpCommand.groovy 17536 2009-09-04 01:42:05Z glaforge $
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class DrawCommand
    extends CommandSupport
{
    DrawCommand(final GoDietSh shell) {
        super(shell, 'draw', 'dr')
    }

    
    Object execute(final List args) {
        assert false, "Not yet implemented"
    }

    
}