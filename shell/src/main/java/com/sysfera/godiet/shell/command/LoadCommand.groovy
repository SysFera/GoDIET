
package com.sysfera.godiet.shell.command

import jline.FileNameCompletor


import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell
import java.lang.Integer

import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.shell.GoDietSh;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;


/**
 * The 'load' command.
 * Inspired by LoadCommand.groovy shell (Jason Dillon)
 *
 */
class LoadCommand
extends CommandSupport {
	LoadCommand(final Shell shell) {
		super(shell, 'load', 'l')
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
		def file = new File("$url")
		URLConnection uc = url.openConnection();


		ResourcesManager rm = ((GoDietSh)shell).rm
		assert rm != null;
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(uc.getInputStream());
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.execute();
	}
	public static void main(String[] args) {
		def godsh = new GoDietSh()
		def tf = new File("/home/phi/Dev/GoDIET/project/src/test/resources/testbed.xm")
		def locc = new LoadCommand(godsh)
		locc.load(tf.toURI().toURL())
		
		def lpfc = new LaunchPlatformCommand(godsh)
		lpfc.do_services()
	}
	
}
