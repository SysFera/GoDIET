package com.sysfera.godiet.core.model.softwares;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.ConfigurationFile;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.common.model.states.ResourceState.State;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileImpl;
import com.sysfera.godiet.core.model.states.StateController;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * 
 * Use to manage software lifecycle.
 * 
 * @author phi
 * 
 */
public abstract class SoftwareManager<T extends Software> implements
		SoftwareInterface<T> {
	private String runningCommand;

	// Software description
	private T softwareDescription;
	private final Resource pluggedOn;

	private Integer pid;
	protected transient final StateController stateController;

	@Override
	public String getId() {
		return softwareDescription.getId();
	}

	private Map<String, ConfigurationFileImpl> configurationFiles;

	public SoftwareManager(T description, Resource pluggedOn,
			SoftwareController softwareController, RuntimeValidator<?> validator)
			throws IncubateException {

		this.softwareDescription = description;
		this.pluggedOn = pluggedOn;
		stateController = new StateController(this, softwareController,
				validator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#getPluggedOn()
	 */
	@Override
	public Resource getPluggedOn() {
		return pluggedOn;
	}

	/**
	 * 
	 * @return stateController
	 */
	public StateController getStateController() {
		return stateController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.model.softwares.SoftwareInterface#getSoftwareDescription
	 * ()
	 */
	@Override
	public T getSoftwareDescription() {
		return softwareDescription;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getPid() {
		return pid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.model.softwares.SoftwareInterface#getRunningCommand()
	 */
	@Override
	public String getRunningCommand() {
		return runningCommand;
	}

	/**
	 * TODO: Perhaps code refactor: setRunning may not be null.
	 * 
	 * @param runningCommand
	 *            the command
	 */
	public void setRunningCommand(String runningCommand) {
		this.runningCommand = runningCommand;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#getState()
	 */
	@Override
	public State getState() {
		return stateController.getState().getStatus();
	}

	public synchronized void start() throws LaunchException {

		ResourceState currentState = this.stateController.getState();
		currentState.start();

	}

	public synchronized void prepare() throws PrepareException {
		ResourceState currentState = this.stateController.getState();
		currentState.prepare();

	}

	public synchronized void stop() throws StopException {
		ResourceState currentState = this.stateController.getState();
		currentState.stop();
	}

	public Map<String, ConfigurationFileImpl> getConfigurationFiles() {
		return this.configurationFiles;
	}

	@Override
	public List<ConfigurationFile> getFiles() {
		List<ConfigurationFile> configurationsFiles = new ArrayList<ConfigurationFile>();
		configurationsFiles.addAll(this.configurationFiles.values());
		return configurationsFiles;
	}

	public void setConfigurationFiles(
			Map<String, ConfigurationFileImpl> configurationFiles) {
		this.configurationFiles = configurationFiles;
	}

	/**
	 * Return the Date of the last transition
	 * 
	 * 
	 * 
	 */
	@Override
	public Date getLastTransition() {
		return this.stateController.getLastTransition();
	}

	@Override
	public String getErrorMessage() {
		if (this.stateController.getErrorCause() != null) {
			return this.stateController.getErrorCause().getMessage();
		}
		return null;
	}

	@Override
	public String getErrorMessageDetails() {
		// TODO
		return null;
	}

}