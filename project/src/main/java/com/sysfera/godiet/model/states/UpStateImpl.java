package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

/**
 * The remote agent is running. Call Stop to stop remote agent and down state
 * Call check to check if the remote agent is already active
 * 
 * 
 * @author phi
 * 
 */
public class UpStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final RemoteConfigurationHelper launcher;
	private final StateController stateController;

	public UpStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.launcher = RemoteConfigurationHelper.getInstance();
	}

	@Override
	public void prepare() {
		log.warn("Already run !");
	}

	@Override
	public void start() {
		log.warn("Try to run an up resource !");
	}

	/**
	 * Stop agent Could set the state on Down if ok or error if remote execution
	 * error.
	 */
	@Override
	public void stop() throws StopException{
		try {
			launcher.stop(this.stateController.softwareManaged);
			this.stateController.state = this.stateController.down;
		} catch (StopException e) {
			this.stateController.state = this.stateController.error;
			throw e;
		}
	}

	/**
	 * Check if the agent is currently up if not goto error state
	 * 
	 * @throws InconsistentStateException
	 */
	@Override
	public void check() {
		// TODO : implement check
		log.error("Check Not yet implemented");
	}

}
