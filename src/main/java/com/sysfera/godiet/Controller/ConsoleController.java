/*@GODIET_LICENSE*/
/*
 * ConsoleController.java
 *
 * Created on 11 juin 2004, 02:39
 */

package com.sysfera.godiet.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import com.sysfera.godiet.Events.LaunchCheckRequest;
import com.sysfera.godiet.Events.LaunchRequest;
import com.sysfera.godiet.Model.RunConfig;
import com.sysfera.godiet.Utils.XmlScanner;
import com.sysfera.godiet.Utils.XmlScannerImpl;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.XMLReadException;

/**
 * 
 * @author rbolze hdail
 */
public class ConsoleController extends java.util.Observable implements
		java.util.Observer {
	private RunConfig runCfg; // configure behavior of GoDIET
	private DietPlatformController modelController;
	private DeploymentController deployCtrl;

	private XmlScanner xmlScanner;

	private List<String> history;
	private boolean fileLoaded = false;

	private OutputStream outStream = null;

	private String logFileName = null;
	private FileWriter logError = null;
	private FileWriter logOutput = null;

	protected static final String HELP = "The following commands are available:\n"
			+ "   launch:          launch entire DIET platform\n"
			+ "   launch_check:    launch entire DIET platform then check its status\n"
			+ "   relaunch:        kill the current platform and launch entire DIET platform once again\n"
			+ "   relaunch_failed: relaunch only failed elements\n"
			+ "   stop:            kill entire DIET platform using kill pid\n"
			+
			// "   kill:       kill entire DIET platform using kill -9 pid\n" +
			"   status:          print run status of each DIET component\n"
			+ "   history:         print history of commands executed\n"
			+ "   help:            print this message\n"
			+ "   check:           check the platform status\n"
			+ "   stop_check:      stop the platform status then check its status before exit\n"
			+ "   start_watcher:   start a \"watcher process\" which periodically checks the platform status\n"
			+ "   stop_watcher:    start the \"watcher process\"\n"
			+ "   exit:            exit GoDIET, do not change running platform.\n";

	public ConsoleController(java.util.Observer deployObserver) {

		initNonGraphic();
		this.deployCtrl.addObserver(deployObserver);
	}

	public void setOutputStream(OutputStream anOutputstream) {
		outStream = anOutputstream;
	}

	public void setLogFile(String logFileName) {
		this.logFileName = logFileName;
		try {
			File errorFile = new File(logFileName + ".err");
			File outputFile = new File(logFileName + ".out");
			this.logError = new FileWriter(errorFile, true);
			this.logOutput = new FileWriter(outputFile, true);
			logError.write("##"
					+ (new Date(System.currentTimeMillis())).toString() + "\n");
			logOutput.write("##"
					+ (new Date(System.currentTimeMillis())).toString() + "\n");
			logError.flush();
			logOutput.flush();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public ConsoleController() {

		initNonGraphic();
	}

	/** To be called by all contructors */
	private void initNonGraphic() {
		this.runCfg = new RunConfig(); // Set defaults
		history = new ArrayList();
		this.modelController = new DietPlatformController(this);
		this.deployCtrl = new DeploymentController(this, modelController);
		this.deployCtrl.addObserver(this);
		this.xmlScanner = new XmlScannerImpl(modelController, this);
	}

	public void setRunConfig(RunConfig runCfg) {
		this.runCfg = runCfg;
	}

	public RunConfig getRunConfig() {
		return this.runCfg;
	}


	public boolean doCommand(String cmd) {
		boolean giveUserCtrl = true;
		// For CTRL + D: cmd == null, we consider that CTRL + D == stop
		if (cmd == null)
			cmd = "stop";
		if (!cmd.equalsIgnoreCase("") && !cmd.equalsIgnoreCase(" ")) {
			history.add(cmd);
			java.util.StringTokenizer strTok = new java.util.StringTokenizer(
					cmd, " ");
			String command = strTok.nextToken();
			if (command.compareTo("load") == 0) {
				if (!fileLoaded) {
					try {
						loadXmlFile(strTok.nextToken());
					} catch (Exception x) {
						printOutput("Usage load <file.xml>", 0);
					}
				} else {
					printOutput("You have already loaded a file", 0);
				}
			} else if (command.compareTo("launch") == 0) {
				launch();
				giveUserCtrl = false;

			} else if (command.compareTo("launch_check") == 0) {
				launchCheck();
				giveUserCtrl = false;
			} else if (command.compareTo("relaunch") == 0) {
				relaunch();
				giveUserCtrl = false;
			} else if (command.compareTo("relaunch_failed") == 0) {
				relaunchFailed();
				// giveUserCtrl = false;
			} else if (command.compareTo("stop") == 0) {
				stop();
				giveUserCtrl = false;
			} else if (command.compareTo("status") == 0) {
				status();
			} else if (command.compareTo("history") == 0) {
				history();
			} else if (command.compareTo("check") == 0) {
				check();
			} else if (command.compareTo("stop_check") == 0) {
				stop_check();
			} else if (command.compareTo("start_watcher") == 0) {
				startWatcher();
			} else if (command.compareTo("stop_watcher") == 0) {
				stopWatcher();
			} else if (command.compareTo("exit") == 0) {
				exit();
			} else {
				help();
			}
		} else {
			help();
		}
		return giveUserCtrl;
	}

	public void loadXmlFile(String xmlFileName) throws XMLReadException {

		this.printOutput("Parsing xml file: " + xmlFileName, 0);

		try {
			this.loadXmlFile(new FileInputStream(new File(xmlFileName)));
		} catch (FileNotFoundException e) {
			this.printError("Error: " + e.getMessage());
			this.printError("Can not continue without valid XML.  Exiting.");
			System.exit(1);
		}

		setFileloaded();
	}

	public void loadXmlFile(InputStream xmlInputStream) throws XMLReadException {

		try {
			xmlScanner.buildDietModel(xmlInputStream);
		} catch (IOException ioe) {

		}
		setFileloaded();
	}

	private void launch() {
		if (fileLoaded) {
			setChanged();
			this.printOutput("sending launch request all", 3);
			notifyObservers(new LaunchRequest(this, "all"));
			clearChanged();

			/*
			 * if(interfaceMode){
			 * goDietConsolePanel.getStopButton().setEnabled(true);
			 * goDietConsolePanel.getLaunchButton().setEnabled(false); }
			 */
		} else {
			printError("You must load the XML file before launch !");
		}
	}

	private void launchCheck() {
		if (fileLoaded) {
			setChanged();
			this.printOutput("sending launch_check request all", 3);
			notifyObservers(new LaunchCheckRequest(this, "all"));
			clearChanged();

			/*
			 * if(interfaceMode){
			 * goDietConsolePanel.getStopButton().setEnabled(true);
			 * goDietConsolePanel.getLaunchButton().setEnabled(false); }
			 */
		} else {
			printError("You must load the XML file before launch !");
		}
	}

	private void relaunch() {
		java.util.Date startTime, endTime;
		double timeDiff;

		if (fileLoaded) {
			startTime = new java.util.Date();
			printOutput("\n* Stopping DIET platform at " + startTime.toString());
			deployCtrl.stopPlatform();
			endTime = new java.util.Date();
			timeDiff = (endTime.getTime() - startTime.getTime()) / 1000;
			printOutput("\n* DIET platform stopped at " + endTime.toString()
					+ "[time= " + timeDiff + " sec]", 0);

			setChanged();
			this.printOutput("sending launch request all", 3);
			notifyObservers(new LaunchRequest(this, "all"));
			clearChanged();

			/*
			 * if(interfaceMode){
			 * goDietConsolePanel.getStopButton().setEnabled(true);
			 * goDietConsolePanel.getLaunchButton().setEnabled(false); }
			 */
		} else {
			printError("You must load the XML file before launch !");
		}
	}

	private void relaunchFailed() {
		if (fileLoaded) {
			setChanged();
			try {
				deployCtrl.checkRelaunchPlatform(true);
			} catch (LaunchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clearChanged();
		} else {
			printOutput("[Relaunch-Failed]: You must load the XML file before checking the platform status !");
		}
	}

	private void startWatcher() {
		if (fileLoaded) {
			deployCtrl.startWatcher();
		} else {
			printOutput("[Start Watcher]: You must load the XML file before checking the platform status !");
		}
	}

	private void stopWatcher() {
		if (fileLoaded) {
			deployCtrl.stopWatcher();
		} else {
			printOutput("[Stop Watcher]: You must load the XML file before checking the platform status !");
		}
	}

	private void stop() {
		java.util.Date startTime, endTime;
		double timeDiff;

		if (fileLoaded) {
			startTime = new java.util.Date();
			printOutput("\n* Stopping DIET platform at " + startTime.toString());
			deployCtrl.stopPlatform();
			endTime = new java.util.Date();
			timeDiff = (endTime.getTime() - startTime.getTime()) / 1000;
			printOutput("\n* DIET platform stopped at " + endTime.toString()
					+ "[time= " + timeDiff + " sec]", 0);

			printOutput("\n* Exiting GoDIET. Bye.");
			exit();

		} else {
			printError("You must load the XML file before stop !");
		}
	}

	private void status() {
		if (fileLoaded) {
			modelController.printPlatformStatus();
		} else {
			printError("You must load the XML file before get status !");
		}
	}

	private void help() {
		printOutput(HELP);
	}

	private void history() {
		for (java.util.Iterator it = history.iterator(); it.hasNext();) {
			printOutput((String) it.next());
		}
	}

	private String exit() {
		if (outStream == null)
			System.exit(0);
		return "exit";
	}

	public void printOutput(String msg) {
		this.printOutput(msg, 0);
	}

	public void printOutput(String msg, int printLevel) {
		// if (this.goDIETconsole != null){
		//if (printLevel <= this.runCfg.getDebugLevel()) {
			if (outStream != null) {
				try {
					outStream.write(msg.getBytes());
					outStream.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(msg);
			//}
		}
		if (this.logFileName != null) {
			logOutput(msg);
		}
	}

	private void logOutput(String msg) {
		try {
			this.logOutput.write(msg + "\n");
			this.logOutput.flush();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	public void printError(String msg) {
		this.printError(msg, 0);
	}

	public void printError(String msg, int printLevel) {
		// if (this.goDIETconsole != null){
		if (printLevel <= this.runCfg.getDebugLevel()) {
			if (outStream != null) {
				try {
					outStream.write(msg.getBytes());
					outStream.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.err.println(msg);
			}

		}
		if (this.logFileName != null) {
			logError(msg);
		}
	}

	private void logError(String msg) {
		try {
			this.logError.write(msg + "\n");
			this.logError.flush();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

	}

	private void setFileloaded() {
		this.fileLoaded = true;

	}

	public void check() {
		if (fileLoaded) {
			deployCtrl.checkPlatform();
		} else {
			printOutput("[Check]: You must load the XML file before checking the platform status !");
		}
	
	}

	public void stop_check() {
		java.util.Date startTime, endTime;
		double timeDiff;

		if (fileLoaded) {
			startTime = new java.util.Date();
			printOutput("\n* Stopping DIET platform at " + startTime.toString());
			deployCtrl.stopPlatform();
			endTime = new java.util.Date();
			timeDiff = (endTime.getTime() - startTime.getTime()) / 1000;
			printOutput("\n* DIET platform stopped at " + endTime.toString()
					+ "[time= " + timeDiff + " sec]", 0);

			check();

			printOutput("\n* Exiting GoDIET. Bye.");
			exit();

		} else {
			printError("You must load the XML file before stop !");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
