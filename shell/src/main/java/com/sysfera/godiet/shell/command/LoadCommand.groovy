
package com.sysfera.godiet.shell.command

import java.net.URL
import java.util.List

import jline.FileNameCompletor

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell

import com.sysfera.godiet.Diet;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand
import com.sysfera.godiet.managers.ResourcesManager
import com.sysfera.godiet.shell.GoDietSh
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl


/**
 * The 'load' commands.
 * Inspired by LoadCommand.groovy shell (Jason Dillon)
 *
 */
class LoadPlatformCommand
extends CommandSupport {
	LoadPlatformCommand(final Shell shell) {
		super(shell, 'loadPlatform', 'lp')
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

		Diet diet = ((GoDietSh)shell).getDiet()
		assert diet != null;
		diet.initPlatform(url)
	}
	public static void main(String[] args) {
		def godsh = new GoDietSh()
		def tf = new File("/home/phi/Dev/GoDIET/project/src/test/resources/testbed.xm")
		def locc = new LoadPlatformCommand(godsh)
		locc.load(tf.toURI().toURL())

		def lpfc = new LaunchCommand(godsh)
		lpfc.do_services()
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

		Diet diet = ((GoDietSh)shell).getDiet()
		assert diet != null;
		diet.initDiet(url)
	}
}
