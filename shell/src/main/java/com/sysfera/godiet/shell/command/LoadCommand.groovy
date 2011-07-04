
package com.sysfera.godiet.shell.command

import java.net.URL
import java.util.List

import jline.FileNameCompletor

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.services.GoDietService;
import com.sysfera.godiet.shell.GoDietSh
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl


/**
 * The 'load' commands.
 * Inspired by LoadCommand.groovy shell (Jason Dillon)
 *
 */
class LoadInfrastructureCommand
extends CommandSupport {
	LoadInfrastructureCommand(final Shell shell) {
		super(shell, 'loadInfrastructure', 'li')
	}

	protected List createCompletors() {
		return [new FileNameCompletor()]
	}

	Object execute(final List args) {
		assert args != null

		if (args.size() != 1) {
			fail("Command 'load' requires one argument")
		}

		URL url

		log.debug("Attempting to load: $url")

		def source = args.get(0)
		try {
			url = new URL("$source")
		}
		catch (MalformedURLException e) {
			def file = new File("$source")

			if (!file.exists()) {
				fail("File not found: $file")
			}

			url = file.toURI().toURL()
		}

		load(url)
	}

	void load(final URL url) {
		assert url != null

		if (io.verbose) {
			io.out.println("Loading: $url")
		}
		assert url != null

		if (io.verbose) {
			io.out.println("Loading: $url")
		}

		GoDietSh goDietShell = shell;
		GoDietService godiet = goDietShell.godiet

		assert godiet != null;
		def inputStream = url.openConnection().inputStream;

		godiet.xmlHelpService.registerInfrastructureElements (inputStream)

	}
}
class LoadDietCommand
extends CommandSupport {
	LoadDietCommand(final Shell shell) {
		super(shell, 'loadDiet', 'ld')
	}

	protected List createCompletors() {
		return [new FileNameCompletor()]
	}

	Object execute(final List args) {
		assert args != null

		if (args.size() != 1) {
			fail("Command 'load' requires one argument")
		}

		URL url

		log.debug("Attempting to load: $url")

		def source = args.get(0)
		try {
			url = new URL("$source")
		}
		catch (MalformedURLException e) {
			def file = new File("$source")

			if (!file.exists()) {
				fail("File not found: $file")
			}

			url = file.toURI().toURL()
		}

		load(url)
	}

	void load(final URL url) {
		assert url != null

		if (io.verbose) {
			io.out.println("Loading: $url")
		}
		GoDietSh goDietShell = shell;
		GoDietService godiet = goDietShell.godiet

		assert godiet != null;
		def inputStream = url.openConnection().inputStream;
		godiet.xmlHelpService.registerDietElements(inputStream)
	}
}
