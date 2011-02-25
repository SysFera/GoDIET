package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.Utils.RemoteConfigurationHelper;
import com.sysfera.godiet.exceptions.InconsistentStateException;
import com.sysfera.godiet.exceptions.LaunchException;


/**
 * The remote agent is running.
 * Call Stop to stop remote agent and down state
 * Call check to check if the remote agent is already active
 * 
 * 
 * @author phi
 *
 */
public class UpStateImpl implements ResourceState {

	private final RemoteConfigurationHelper launcher;
	private final StateController stateController;

	public UpStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.launcher = RemoteConfigurationHelper.getInstance();
	}

	@Override
	public void prepare() throws InconsistentStateException {
		throw new InconsistentStateException();
	}

	@Override
	public void start() throws InconsistentStateException {
		throw new InconsistentStateException();
		
	}

	/**
	 * Stop agent
	 * Could set the state on Down if ok or error if remote execution error.
	 */
	@Override
	public void stop()  {
		try {
			launcher.stop(this.stateController.agent);
			this.stateController.state = this.stateController.down;
		} catch (LaunchException e) {
			//TODO Logger
			this.stateController.state = this.stateController.error;
		
		}		
	}

	/**
	 * Check if the agent is currently up
	 * if not goto error state
	 * @throws InconsistentStateException 
	 */
	@Override
	public void check() throws InconsistentStateException  {
		//TODO : implement check
		throw new InconsistentStateException("Not yet implemented");		
	}


}
