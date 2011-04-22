package com.sysfera.godiet.shell.command

import java.net.URL
import java.util.List

import jline.FileNameCompletor

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.Diet
import com.sysfera.godiet.shell.GoDietSh


class SSHCommand extends CommandSupport {
	SSHCommand(final Shell shell) {
		super(shell, 'ssh', 'ss')
	}

	protected List createCompletors() {
		return [new FileNameCompletor()]
	}

	Object execute(final List args) {
		//TODO: Creer un jsch.userInfo qui prend en passphrase l'entre utilisateur.L'injecter au remoteAccess.
	}
}
