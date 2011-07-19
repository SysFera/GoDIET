package com.sysfera.godiet.model.softwares;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.model.ConfigurationFile;
import com.sysfera.godiet.model.SoftwareInterface;
import com.sysfera.godiet.model.configurator.ConfigurationFileImpl;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;
import com.sysfera.godiet.model.states.StateController;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * 
 * Use to manage software lifecycle.
 * 
 * @author phi
 * 
 */
public abstract class SoftwareManager<T extends Software> implements SoftwareInterface<T> {
	private String runningCommand;

	// Software description
	private T softwareDescription;
	private final Resource pluggedOn;

	private Integer pid;
	protected final StateController stateController;


	private Map<String,ConfigurationFileImpl> configurationFiles;

	public SoftwareManager(T description, Resource pluggedOn,
			SoftwareController softwareController,
			RuntimeValidator<?> validator)
			throws IncubateException {
	
		this.softwareDescription = description;
		this.pluggedOn = pluggedOn;
		stateController = new StateController(this, softwareController,
				validator);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#getSoftwareDescription()
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

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#getRunningCommand()
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

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#getState()
	 */
	@Override
	public ResourceState getState() {
		return stateController.getState();
	}

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#start()
	 */
	@Override
	public synchronized void start() throws LaunchException {

		ResourceState currentState = this.stateController.getState();
		currentState.start();

	}

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#prepare()
	 */
	@Override
	public synchronized void prepare() throws PrepareException {
		ResourceState currentState = this.stateController.getState();
		currentState.prepare();

	}

	/* (non-Javadoc)
	 * @see com.sysfera.godiet.model.softwares.SoftwareInterface#stop()
	 */
	@Override
	public synchronized void stop() throws StopException {
		ResourceState currentState = this.stateController.getState();
		currentState.stop();
	}

	
	public Map<String,ConfigurationFileImpl> getConfigurationFiles() {
		return this.configurationFiles;
	}

	@Override
	public List<ConfigurationFile> getFiles()
	{
		List<ConfigurationFile> configurationsFiles = new ArrayList<ConfigurationFile>();
		configurationsFiles.addAll(this.configurationFiles.values());
		return configurationsFiles;
	}
	public void setConfigurationFiles(Map<String,ConfigurationFileImpl> configurationFiles) {
		this.configurationFiles = configurationFiles;
	}
	
}