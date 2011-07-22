package com.sysfera.godiet.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sysfera.godiet.common.controllers.SSHKeyController;
import com.sysfera.godiet.common.exceptions.CommandExecutionException;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.generated.User.Ssh.Key;
import com.sysfera.godiet.common.services.GoDietService;
import com.sysfera.godiet.common.utils.StringUtils;

public class Diet {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;

	public List<SSHKeyController> getManagedKeys() {
		return this.godiet.getUserService().getManagedKeys();

	}

	public void registerKey(Key key) {
		try {
			this.godiet.getUserService().addSSHKey(key);
		} catch (AddAuthentificationException e) {
			log.error("Unable to load ssh key", e.getMessage());
		}
	}

	// TODO
	// public void modifySshKey(SSHKeyManager sshkey, String privateKeyPath,
	// String publicKeyPath, String password) {
	//
	// this.godiet.getUserService().
	// .modifySSHKey(sshkey, privateKeyPath, publicKeyPath, password);
	// }

	public void initInfrastructure(URL url) {

		InputStream inputStream = null;
		try {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			log.error("Unable to open file " + url, e);
		}

		if (inputStream == null) {
			log.error("Unable to open file " + url);
			return;
		}
		try {
			String outputString = StringUtils.streamToString(inputStream);

			this.godiet.getXmlHelpService().registerInfrastructureElements(
					outputString);
		} catch (Exception e) {
			log.error("Error when load XML infrastructure " + url, e);
		}

	}

	public void initDiet(URL url) throws CommandExecutionException {

		InputStream inputStream = null;
		try {
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				f = new File(url.getPath());
			}

			inputStream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			log.error("Unable to open file " + url, e);
		}

		if (inputStream == null) {
			log.error("Unable to open file " + url);
			return;
		}
		try {
			String outputString = StringUtils.streamToString(inputStream);

			this.godiet.getXmlHelpService().registerInfrastructureElements(
					outputString);
		} catch (Exception e) {
			log.error("Error when load XML DIET platform " + url, e);
		}
	}

	// LAUNCH
	public void launchOmniNames() throws PrepareException, LaunchException {
		List<SoftwareInterface<OmniNames>> omniNames = godiet
				.getPlatformService().getOmninames();
		for (SoftwareInterface<OmniNames> dietServiceManaged : omniNames) {
			godiet.getPlatformService().prepareSoftware(
					dietServiceManaged.getId());
			godiet.getPlatformService().startSoftware(
					dietServiceManaged.getId());
		}

	}

	public void launchForwarders() throws PrepareException, LaunchException {
		List<SoftwareInterface<Forwarder>> forwarders = godiet
				.getPlatformService().getForwarders();
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("SERVER")) {
				godiet.getPlatformService().prepareSoftware(
						dietResourceManaged.getId());
				godiet.getPlatformService().startSoftware(
						dietResourceManaged.getId());
			}
		}
		for (SoftwareInterface<Forwarder> dietResourceManaged : forwarders) {
			if (dietResourceManaged.getSoftwareDescription().getType()
					.equals("CLIENT")) {
				godiet.getPlatformService().prepareSoftware(
						dietResourceManaged.getId());
				godiet.getPlatformService().startSoftware(
						dietResourceManaged.getId());
			}
		}
	}

	public void launchMasterAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<MasterAgent>> masterAgents = godiet
				.getPlatformService().getMasterAgents();
		for (SoftwareInterface<MasterAgent> ma : masterAgents) {
			godiet.getPlatformService().prepareSoftware(ma.getId());
			godiet.getPlatformService().startSoftware(ma.getId());
		}
	}

	public void launchLocalAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<LocalAgent>> localAgents = godiet
				.getPlatformService().getLocalAgents();
		for (SoftwareInterface<LocalAgent> la : localAgents) {
			godiet.getPlatformService().prepareSoftware(la.getId());
			godiet.getPlatformService().startSoftware(la.getId());
		}
	}

	public void launchSedsAgents() throws PrepareException, LaunchException {
		List<SoftwareInterface<Sed>> seds = godiet.getPlatformService()
				.getSeds();
		for (SoftwareInterface<Sed> sed : seds) {
			godiet.getPlatformService().prepareSoftware(sed.getId());
			godiet.getPlatformService().startSoftware(sed.getId());
		}
	}

	public void stopServices() throws StopException {
		List<SoftwareInterface<OmniNames>> omniNames = godiet
				.getPlatformService().getOmninames();
		for (SoftwareInterface<OmniNames> dietServiceManaged : omniNames) {
			godiet.getPlatformService()
					.stopSoftware(dietServiceManaged.getId());

		}

	}

	public void stopSoftware(String softwareId) throws PrepareException,
			StopException, CommandExecutionException {

		SoftwareInterface<? extends Software> software = this.godiet
				.getPlatformService().getManagedSoftware(softwareId);
		if (software == null)
			throw new CommandExecutionException("Unable to find " + softwareId);

		godiet.getPlatformService().stopSoftware(software.getId());
	}

}