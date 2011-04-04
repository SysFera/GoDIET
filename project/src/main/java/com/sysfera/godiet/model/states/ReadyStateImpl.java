package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

/**
 * 
 * The remote agent is ready to be start. 
 * Call start to Up State
 * Call stop to Down State
 * 
 * @author phi
 *
 */
public class ReadyStateImpl implements ResourceState{
	private Logger log = LoggerFactory.getLogger(getClass());

	private final RemoteConfigurationHelper launcher;
	private final StateController stateController;
	
	public ReadyStateImpl(StateController stateController) {
		this.stateController = stateController;
		launcher = RemoteConfigurationHelper.getInstance();		
	}

	@Override
	public void prepare() throws PrepareException {
		log.warn("Already prepared !");
	}

	/**
	 * Start agent
	 * @throws LaunchException 
	 */
	@Override
	public void start() throws LaunchException {
		try {
			launcher.launch(this.stateController.softwareManaged);
			//TODO: Check if started 
			this.stateController.state = this.stateController.up;
		} catch (LaunchException e) {
			this.stateController.state = this.stateController.error;
			throw e;
		}
		
	}

	/**
	 * Stop agent
	 */
	@Override
	public void stop() {
		this.stateController.state = this.stateController.down;
		
	}

	@Override
	public void check() {
	}

}
