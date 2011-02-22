package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.Utils.LaunchHelper;
import com.sysfera.godiet.exceptions.InconsistentStateException;
import com.sysfera.godiet.exceptions.LaunchException;

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
	
	private final LaunchHelper launcher;
	private final StateController stateController;
	
	public ReadyStateImpl(StateController stateController) {
		this.stateController = stateController;
		launcher = LaunchHelper.getInstance();		
	}

	@Override
	public void prepare() throws InconsistentStateException {
		throw new InconsistentStateException();
		
	}

	/**
	 * Start agent
	 */
	@Override
	public void start() {
		try {
			launcher.launch(this.stateController.agent);
			this.stateController.state = this.stateController.up;
		} catch (LaunchException e) {
			//TODO Logger
			this.stateController.state = this.stateController.error;
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
	public void check() throws InconsistentStateException {
		throw new InconsistentStateException();		
	}

}
