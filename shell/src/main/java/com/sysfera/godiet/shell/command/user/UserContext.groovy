package com.sysfera.godiet.shell.command.user

import org.codehaus.groovy.tools.shell.CommandRegistry
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.SimpleCompletor

class UserContext  extends CommandSupport {
	UserContext(final Shell shell) {
		super(shell, 'user', 'u')
	}


	protected List createCompletors() {
		return [
			new UserContextCompletor(registry),
			null
		]
	}

	Object execute(final List args) {
		assert args != null

		if (args.size() > 1) {
			fail(messages.format('error.unexpected_args', args.join(' ')))
		}

		if (args.size() == 1) {
			help(args[0])
		}
		else {
			list()
		}
	}
}
class UserContextCompletor
extends SimpleCompletor {
	private final CommandRegistry registry

	UserContextCompletor(final CommandRegistry registry) {
		assert registry

		this.registry = registry
	}

	SortedSet getCandidates() {
		def set = new TreeSet()

		for (command in registry) {
			if (command.hidden) {
				continue
			}

			set << command.name
			set << command.shortcut
		}

		return set
	}
}