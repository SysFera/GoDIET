package com.sysfera.godiet.common.rmi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sysfera.godiet.common.model.ConfigurationFile;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState.State;

public class SoftwareInterfaceSerializable<T extends Software> implements
		SoftwareInterface<T> {
	String id;
	Resource pluggedOn;
	T softwareDescription;
	String runningCommand;
	State state;
	List<ConfigurationFile> files = new ArrayList<ConfigurationFile>();
	Date lastTransition;
	String errorMessage;
	String errorMessageDetails;

	public SoftwareInterfaceSerializable(SoftwareInterface<T> copy) {
		this.id = copy.getId();
		this.pluggedOn = copy.getPluggedOn();
		this.softwareDescription = copy.getSoftwareDescription();
		this.runningCommand = copy.getRunningCommand();
		this.state = copy.getState();
		if(copy.getFiles() != null)
		{
			for (ConfigurationFile configFile : copy.getFiles()) {
				this.files.add(new ConfigurationFileSerializable(configFile));
			}
			
		}
		
		this.lastTransition = copy.getLastTransition();
		this.errorMessage = copy.getErrorMessage();
		this.errorMessageDetails = copy.getErrorMessageDetails();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Resource getPluggedOn() {
		return pluggedOn;
	}

	public void setPluggedOn(Resource pluggedOn) {
		this.pluggedOn = pluggedOn;
	}

	public T getSoftwareDescription() {
		return softwareDescription;
	}

	public void setSoftwareDescription(T softwareDescription) {
		this.softwareDescription = softwareDescription;
	}

	public String getRunningCommand() {
		return runningCommand;
	}

	public void setRunningCommand(String runningCommand) {
		this.runningCommand = runningCommand;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<ConfigurationFile> getFiles() {
		return files;
	}

	public void setFiles(List<ConfigurationFile> files) {
		this.files = files;
	}

	public Date getLastTransition() {
		return lastTransition;
	}

	public void setLastTransition(Date lastTransition) {
		this.lastTransition = lastTransition;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessageDetails() {
		return errorMessageDetails;
	}

	public void setErrorMessageDetails(String errorMessageDetails) {
		this.errorMessageDetails = errorMessageDetails;
	}

	class ConfigurationFileSerializable implements ConfigurationFile {
		private String id;
		private String contents;
		private String absolutePath;

		public ConfigurationFileSerializable(ConfigurationFile copy) {
			this.id = copy.getId();
			this.contents = copy.getContents();
			this.absolutePath = copy.getAbsolutePath();
		}

		@Override
		public String getId() {

			return id;
		}

		@Override
		public String getContents() {
			return contents;
		}

		@Override
		public String getAbsolutePath() {

			return absolutePath;
		}

	}
}