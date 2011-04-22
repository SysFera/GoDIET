package com.sysfera.godiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.prepare.PrepareAgentsCommand;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.command.start.StartAgentsCommand;
import com.sysfera.godiet.command.start.StartForwardersCommand;
import com.sysfera.godiet.command.start.StartServicesCommand;
import com.sysfera.godiet.command.stop.StopAgentsCommand;
import com.sysfera.godiet.command.stop.StopForwardersCommand;
import com.sysfera.godiet.command.stop.StopServicesCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.ssh.ChannelManagerJsch;
import com.sysfera.godiet.remote.ssh.RemoteAccessJschImpl;

public class Diet {
	private Logger log = LoggerFactory.getLogger(getClass());

	private RemoteAccess remoteAccess;
	private ResourcesManager rm;
	private boolean configLoaded = false;
	private boolean platfromLoaded = false;
	private boolean dietLoaded = false;
	private boolean servicesLaunched = false;
	private boolean agentsLaunched = false;

	public Diet() {
		this.rm = new ResourcesManager();
		RemoteAccessJschImpl remoteJsch =new RemoteAccessJschImpl();
		remoteJsch.setChannelManager(new ChannelManagerJsch());
		this.remoteAccess = remoteJsch;

		// Real Remote SSH
		// TODO SSH Key manager
		try {
			remoteAccess
					.addItentity(
							"/home/phi/Dev/GoDIET/integration-test/src/test/resources/fakeuser/testbedKey",
							null, "godiet");
		} catch (AddAuthentificationException e) {
			log.error("unable to load tesbted key");
		}
		try {
			// Here add a key to access on testbed
			remoteAccess.addItentity("/home/phi/tmp/id_dsa", null, "");
		} catch (AddAuthentificationException e) {
			log.error("unable to load /home/phi/tmp/id_dsa key");
		}

	}

	// INIT
	public void initConfig(URL url) throws CommandExecutionException {
		if (configLoaded)
			new CommandExecutionException("Configuration already loaded");

		InputStream inputStream;
		try {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new CommandExecutionException("Unable to open file " + url, e);
		}
		XMLLoadingHelper.initConfig(rm, inputStream);
		configLoaded = true;
	}

	public void initPlatform(URL url) throws CommandExecutionException {
		if (!configLoaded)
			new CommandExecutionException("Godiet not correctly configured");
		InputStream inputStream;
		try {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new CommandExecutionException("Unable to open file " + url, e);
		}
		XMLLoadingHelper.initPlatform(rm, inputStream);
		platfromLoaded = true;
	}

	public void initDiet(URL url) throws CommandExecutionException {
		if (!configLoaded)
			new CommandExecutionException("Godiet not correctly configured");
		if (!platfromLoaded)
			new CommandExecutionException("Load platform first");
		InputStream inputStream;
		try {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new CommandExecutionException("Unable to open file " + url, e);
		}
		XMLLoadingHelper.initDiet(rm, inputStream, remoteAccess);
		dietLoaded = true;
	}

	// LAUNCH
	public void launchServices() throws CommandExecutionException {
		if (!dietLoaded)
			new CommandExecutionException("Load diet description first");
		try {
			// Services commands
			PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
			prepareCommand.setRm(rm);
			StartServicesCommand launchServicesCommand = new StartServicesCommand();
			launchServicesCommand.setRm(rm);
			prepareCommand.execute();
			launchServicesCommand.execute();
		} finally {
			servicesLaunched = true;
		}
	}

	public void launchAgents() throws CommandExecutionException {
		if (!servicesLaunched)
			new CommandExecutionException("Launch diet services first");
		try {
			InitForwardersCommand initForwardersCommand = new InitForwardersCommand();
			initForwardersCommand.setRm(rm);
			initForwardersCommand.setRemoteAccess(remoteAccess);

			PrepareAgentsCommand prepareAgents = new PrepareAgentsCommand();
			prepareAgents.setRm(rm);

			StartForwardersCommand launchForwarders = new StartForwardersCommand();
			launchForwarders.setRm(rm);

			StartAgentsCommand startAgent = new StartAgentsCommand();
			startAgent.setRm(rm);

			// execute
			initForwardersCommand.execute();

			prepareAgents.execute();

			launchForwarders.execute();
			startAgent.execute();
		} finally {
			agentsLaunched = true;
		}
	}

	// STOP
	public void stopAgents() throws CommandExecutionException {
		if (!agentsLaunched)
			new CommandExecutionException("Agents doesn't running");
		StopAgentsCommand stopAgents = new StopAgentsCommand();
		stopAgents.setRm(rm);
		StopForwardersCommand stopForwarders = new StopForwardersCommand();
		stopForwarders.setRm(rm);

		stopAgents.execute();
		stopForwarders.execute();
		agentsLaunched = false;
	}

	public void stopServices() throws CommandExecutionException {
		if (!servicesLaunched)
			new CommandExecutionException("Diet services doesn't running");
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);
		stopServicesCommand.execute();
		servicesLaunched = false;
	}

	public ResourcesManager getRm() {
		return rm;
	}

}