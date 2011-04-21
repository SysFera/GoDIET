
package com.sysfera.godiet.shell.command

import org.codehaus.groovy.tools.shell.CommandException
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.ComplexCommandSupport

abstract class GoDietCommandSupport extends CommandSupport{


	abstract void preCondition() throws CommandException;
}

abstract class GoDietComplexCommandSupport extends ComplexCommandSupport{


	abstract void preCondition() throws CommandException;
}
