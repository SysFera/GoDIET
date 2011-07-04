package com.sysfera.godiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.services.GoDietService;

public class Diet {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoDietService godiet;

	private DietManager dietManager;
	public List<SSHKeyManager> getManagedKeys() {
		return this.godiet.getModel().getUserManager().getManagedKeys();

	}

	@PostConstruct
	public void postConstruct(){
		this.dietManager = godiet.getModel().getDietModel();
	}
	public void registerKey(Key key) {
		try {
			this.godiet.getUserService().addSSHKey(key);
		} catch (AddAuthentificationException e) {
			log.error("Unable to load ssh key", e.getMessage());
		}
	}

//TODO	
//	public void modifySshKey(SSHKeyManager sshkey, String privateKeyPath,
//			String publicKeyPath, String password) {
//
//		this.godiet.getUserService().
//				.modifySSHKey(sshkey, privateKeyPath, publicKeyPath, password);
//	}

	public void initInfrastructure(URL url)  {
		
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
		
		if(inputStream == null)
		{
			log.error("Unable to open file " + url);
			return;
		}
		try {
		
			this.godiet.getXmlHelpService().registerInfrastructureElements(inputStream);
		} catch (Exception e) {
			log.error("Error when load XML infrastructure "+ url,e);
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
		
		if(inputStream == null)
		{
			log.error("Unable to open file " + url);
			return;
		}
		try {
		
			this.godiet.getXmlHelpService().registerInfrastructureElements(inputStream);
		} catch (Exception e) {
			log.error("Error when load XML DIET platform "+ url,e);
		} 
	}

	// LAUNCH
	public void launchOmniNames() throws PrepareException, LaunchException {
		List<OmniNamesManaged> omniNames = this.dietManager.getOmninames();
		for (OmniNamesManaged dietServiceManaged : omniNames) {
			dietServiceManaged.prepare();
			dietServiceManaged.start();
		}

	}


	public void launchMasterAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<MasterAgent>> masterAgents = this.dietManager
				.getMasterAgents();
		for (DietResourceManaged<MasterAgent> ma : masterAgents) {
			ma.prepare();
			ma.start();
		}
	}

	public void launchLocalAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<LocalAgent>> localAgents = this.dietManager
				.getLocalAgents();
		for (DietResourceManaged<LocalAgent> la : localAgents) {
			la.prepare();
			la.start();
		}
	}

	public void launchSedsAgents() throws PrepareException, LaunchException {
		List<DietResourceManaged<Sed>> seds = this.dietManager.getSeds();
		for (DietResourceManaged<Sed> sed : seds) {
			sed.prepare();
			sed.start();
		}
	}



	public void stopServices() throws  StopException {
		List<OmniNamesManaged> omniNames = this.dietManager.getOmninames();
		for (OmniNamesManaged sed : omniNames) {
			sed.stop();
			
		}
	
	}

	public void stopSoftware(String softwareId) throws PrepareException,
			StopException, CommandExecutionException {

		SoftwareManager<? extends Software> software = this.dietManager
				.getManagedSoftware(softwareId);
		if (software == null)
			throw new CommandExecutionException("Unable to find " + softwareId);

		software.stop();
	}


}