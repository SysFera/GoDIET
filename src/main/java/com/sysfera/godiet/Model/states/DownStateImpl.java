package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.Utils.LaunchHelper;
import com.sysfera.godiet.exceptions.InconsistentStateException;
import com.sysfera.godiet.exceptions.PrepareException;


/**
 * Init State. The remote agent isn't yet bind. 
 * Call prepare to state ready
 * 
 * @author phi
 *
 */
public class DownStateImpl implements ResourceState{
	
	private final LaunchHelper launcher;
	private final StateController stateController;
	
	public DownStateImpl(StateController stateController) {
		this.stateController = stateController;
		launcher = LaunchHelper.getInstance();		
	}

	/**
	 * Prepare agent to run (typically create and copy remote files)
	 */
	@Override
	public void prepare()  {
		try {
			launcher.configure(this.stateController.agent);
			this.stateController.state = this.stateController.ready;
		} catch (PrepareException e) {
			//TODO Logger
			this.stateController.state = this.stateController.error;
		}
		
	}

	@Override
	public void start() throws InconsistentStateException {
		throw new InconsistentStateException("Need prepare before");		
	}

	
	@Override
	public void stop() throws InconsistentStateException {
		throw new InconsistentStateException();		
	}

	@Override
	public void check() throws InconsistentStateException {
		throw new InconsistentStateException();		
		
	}
	


}
