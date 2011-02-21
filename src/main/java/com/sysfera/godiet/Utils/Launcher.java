/*@GODIET_LICENSE*/
/*
 * Launcher.java
 *
 * Created on April 19, 2004, 1:59 PM
 */

package com.sysfera.godiet.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Model.AccessMethod;
import com.sysfera.godiet.Model.Agents;
import com.sysfera.godiet.Model.ComputeCollection;
import com.sysfera.godiet.Model.Domain;
import com.sysfera.godiet.Model.Elements;
import com.sysfera.godiet.Model.RunConfig;
import com.sysfera.godiet.Model.ServerDaemon;
import com.sysfera.godiet.Model.physicalresources.ComputeResource;
import com.sysfera.godiet.Model.physicalresources.StorageResource;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.managers.DietPlatformImpl;

/**
 * 
 * @author hdail
 */
public class Launcher {
	private ConsoleController consoleCtrl;
	private File killPlatformFile;

	private Set<Domain> domains;

	/** Creates a new instance of Launcher */
	public Launcher(ConsoleController consoleController, Set<Domain> domains) {
		this.consoleCtrl = consoleController;
		this.domains = domains;
	}

	public void createLocalScratch() {
		RunConfig runCfg = consoleCtrl.getRunConfig();
		File dirHdl;
		String runLabel = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMMdd_HHmm");
		java.util.Date today = new Date();
		String dateString = formatter.format(today);

		for (Domain domain : domains) {
			dirHdl = new File(runCfg.getLocalScratch() + "/" + domain.getName());
			dirHdl.mkdirs();

			runCfg.setRunLabel(null);

		}

		runCfg.setLocalScratchReady(true);
		consoleCtrl.printOutput(
				"\nLocal scratch directory ready:\n\t"
						+ runCfg.getLocalScratch(), 1);

		// Initiate output file for pids to use as backup for failures
		killPlatformFile = new File(runCfg.getLocalScratch(),
				"killPlatform.csh");
		try {
			if (killPlatformFile.exists()) {
				killPlatformFile.delete();
			}
			killPlatformFile.createNewFile();
		} catch (IOException x) {
			consoleCtrl.printOutput("createLocalScratch: Could not create "
					+ killPlatformFile);
		}
	}

	/*
	 * launchElement is the primary method for launching components of the DIET
	 * hierarchy. This method performs the following actions: - check that
	 * element, compRes, & scratch base are non-null - create the config file
	 * locally - stage the config file to remote host - run the element on the
	 * remote host
	 */
	public void launchElement(Elements element, boolean useLogService)
			throws LaunchException {
		RunConfig runCfg = consoleCtrl.getRunConfig();
		if (element == null) {
			consoleCtrl.printError("launchElement called with null element. "
					+ "Launch request ignored.", 1);
			return;
		}
		if (element.getComputeResource() == null) {
			consoleCtrl.printError("LaunchElement called with null resource. "
					+ "Launch request ignored.");
			return;
		}
		if (runCfg.isLocalScratchReady() == false) {
			consoleCtrl
					.printError("launchElement: Scratch space is not ready. "
							+ "Need to run createLocalScratch.");
			return;
		}

		consoleCtrl.printOutput("\n** Launching element " + element.getName()
				+ " on " + element.getComputeResource().getName(), 1);
		try {
			// LAUNCH STAGE 1: Write config file
			createCfgFile(element, useLogService);
		} catch (IOException x) {
			consoleCtrl.printError(
					"Exception writing cfg file for " + element.getName(), 0);
			consoleCtrl.printError("Exception: " + x + "\nExiting.", 1);
			element.getLaunchInfo().setLaunchState(
					com.sysfera.godiet.Defaults.LAUNCH_STATE_CONFUSED);
			System.exit(1); // / TODO: Add error handling and don't exit
		}
		ComputeCollection coll = element.getComputeResource().getCollection();
		StorageResource storeRes = coll.getStorageResource();
		// LAUNCH STAGE 2: Stage config file
		stageFile(element.getCfgFileName(), storeRes);
		// LAUNCH STAGE 3: Launch element
		runElement(element);
	}

	/*
	 * launchElement2 is the second method for launching components of the DIET
	 * hierarchy. This method performs the following actions: - check that
	 * element, compRes, & scratch base are non-null - run the element on the
	 * remote host - don't need to create and stage cfg file, it suppose to be
	 * already done.
	 */
	public void launchElement2(Elements element, boolean useLogService) {
		if (element == null) {
			consoleCtrl.printError("launchElement called with null element. "
					+ "Launch request ignored.", 1);
			return;
		}
		if (element.getComputeResource() == null) {
			consoleCtrl.printError("LaunchElement called with null resource. "
					+ "Launch request ignored.");
			return;
		}
		consoleCtrl.printOutput("\n** Launching element " + element.getName()
				+ " on " + element.getComputeResource().getName(), 1);
		// LAUNCH STAGE 1: Launch element
		runElement(element);
	}

	public void stageFile(String filename, StorageResource storeRes)
			throws LaunchException {
		consoleCtrl.printOutput(
				"Staging file " + filename + " to " + storeRes.getName(), 1);

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.stageWithScp(filename, storeRes, consoleCtrl.getRunConfig());
	}

	// TODO: incorporate Elagi usage
	public void stageAllFile(StorageResource storeRes) throws LaunchException {
		consoleCtrl.printOutput("Staging file to " + storeRes.getName(), 1);

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.stageFilesWithScp(storeRes, consoleCtrl.getRunConfig());
	}

	// TODO: incorporate Elagi usage
	private void runElement(Elements element) {
		ComputeResource compRes = element.getComputeResource();
		consoleCtrl.printOutput("Executing element " + element.getName()
				+ " on resource " + compRes.getName(), 1);
		AccessMethod access = compRes.getAccessMethod("ssh");
		if (access == null) {
			consoleCtrl.printError("runElement: compRes does not have "
					+ "ssh access method. Ignoring launch request.");
			return;
		}

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.runWithSsh(element, consoleCtrl.getRunConfig(),
				killPlatformFile);
	}

	public void stopElement(Elements element) {
		consoleCtrl.printOutput("Trying to stop element " + element.getName(),
				1);
		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		if (element instanceof Agents || element instanceof ServerDaemon)
			sshUtil.stopWithSsh(element, consoleCtrl.getRunConfig(), true);
		else
			sshUtil.stopWithSsh(element, consoleCtrl.getRunConfig(), false);
	}

	public void createCfgFile(Elements element, boolean useLogService)
			throws IOException {
		RunConfig runCfg = consoleCtrl.getRunConfig();
		if (element != null) {
			if (element.getName().compareTo("TestTool") == 0) {
				return;
			}
			File cfgFile = new File(runCfg.getLocalScratch() + "/" +

			element.getCfgFileName());
			try {
				cfgFile.createNewFile();
				consoleCtrl.printOutput(
						"Writing config file " + element.getCfgFileName(), 1);
				FileWriter out = new FileWriter(cfgFile);
				element.writeCfgFile(out);
				out.close();
			} catch (IOException x) {
				consoleCtrl.printError("Failed to write " + cfgFile.getPath());
				throw x;
			}
		}
	}

}
