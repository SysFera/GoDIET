package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.exceptions.InconsistentStateException;

/**
 * Error State. Currently no way to leave.
 * 
 * @author phi
 *
 */
public class ErrorStateImpl implements ResourceState {

	
	public ErrorStateImpl(StateController stateController) {
		
		//Nothing to do
	}
	@Override
	public void start() throws InconsistentStateException {
		throw new InconsistentStateException();
	}

	@Override
	public void stop() throws InconsistentStateException {
		throw new InconsistentStateException();
	}

	@Override
	public void check() throws InconsistentStateException {
		throw new InconsistentStateException();
	}
	@Override
	public void prepare() throws InconsistentStateException {
		throw new InconsistentStateException();
	}

}
