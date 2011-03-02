/*@GODIET_LICENSE*/
/*
 * GoDIET.java
 *
 * Created on April 25, 2004, 12:40 PM
 */

package com.sysfera.godiet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Events.DeployStateChange;
import com.sysfera.godiet.exceptions.XMLParseException;

/**
 * 
 * @author hdail
 */
public class GoDIET implements java.util.Observer {
	
	
	protected static final String USAGE = "USAGE: GoDiet [--launch|--launch_check] <file.xml>\n"
			+ "              (launch the command line interface, using <file.xml> file)\n"
			+ "       GoDiet --interface (launch GUI)\n"
			+ "       GoDiet --help (this help)";
	private int deployState = Defaults.DEPLOY_NONE;

	public GoDIET() {
	}

	public void update(Observable observable, Object obj) {
		java.awt.AWTEvent e = (java.awt.AWTEvent) obj;
		int newState;
		if (e instanceof DeployStateChange) {
			newState = ((DeployStateChange) e).getNewState();
			// System.out.println("godiet Changing state to : " +
			// goDiet.Defaults.getDeployStateString(newState));
			this.deployState = newState;
			if (newState != com.sysfera.godiet.Defaults.DEPLOY_LAUNCHING
					&& newState != com.sysfera.godiet.Defaults.DEPLOY_STOPPING) {
				synchronized (this) {
					notifyAll();
				}
			}
		}
	}

	public static void main(String[] args) {
		GoDIET goDiet = new GoDIET();
		boolean launchMode = false;
		boolean shellMode = true; // default
		
		String xmlFile = "";
		boolean giveUserCtrl = true;
		boolean check_mode = false;
		int i;
		// System.out.println("args.length :"+args.length);
		if (args.length < 1 || args.length > 2
				|| (args[0].equals("--help") || args[0].equals("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}
		xmlFile = args[args.length - 1];
		/** Parse Arguments */

		if (args.length == 2) {
			if (args[0].compareTo("--launch") == 0) {
				shellMode = false;
				
				launchMode = true;
				xmlFile = args[args.length - 1];
			}
			if (args[0].compareTo("--launch_check") == 0) {
				shellMode = false;
			
				launchMode = true;
				check_mode = true;
				xmlFile = args[args.length - 1];
			}

		}

		try {
			/** Prepare GoDIET for usage */
			if (launchMode) {
				// If not interactive, just launch platform and exit
				ConsoleController consoleController = new ConsoleController(
						goDiet);

				consoleController.loadXmlFile(xmlFile);

				consoleController.printOutput("* Launching DIET platform.");
				if (check_mode)
					consoleController.doCommand("launch_check");
				else
					consoleController.doCommand("launch");
				consoleController.printOutput("GoDIET finished.");
			
			} else if (shellMode) {
				ConsoleController consoleController = new ConsoleController(
						goDiet);
				consoleController.loadXmlFile(xmlFile);
				BufferedReader stdin = new BufferedReader(
						new InputStreamReader(System.in));

				try {
					ConsoleReader reader = new ConsoleReader();
					reader.setBellEnabled(false);

					List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();
					completors.add(new SimpleCompletor(new String[] { "launch",
							"launch_check", "relaunch", "relaunch_failed",
							"stop", "status", "history", "help", "check",
							"stop_check", "exit", "start_watcher",
							"stop_watcher" }));
					reader.addCompletor(new ArgumentCompletor(completors));

					String command;
					while (true) {
						System.out.print("GoDIET> ");
						command = "";
						try {
							// command = stdin.readLine();
							command = reader.readLine();
							giveUserCtrl = consoleController.doCommand(command);
						} catch (Exception x) {
							consoleController.printOutput("Exception: "
									+ x.toString());
							// break;
						}
						if (giveUserCtrl == false) {
							try {
								synchronized (goDiet) {
									goDiet.wait();
								}
							} catch (InterruptedException x) {
								System.out
										.println("Unexpected sleep interruption.");
							}
						}
					} // End while(true)
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			} // End Shell mode context
		} catch (XMLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // End main{}
} // End GoDIET
