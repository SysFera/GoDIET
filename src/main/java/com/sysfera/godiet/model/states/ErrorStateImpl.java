package com.sysfera.godiet.model.states;

import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;


/**
 * Error State. Currently no way to leave.
 * 
 * @author phi
 * 
 */
public class ErrorStateImpl implements ResourceState {

	public ErrorStateImpl(StateController stateController) {

		// Nothing to do
	}

	@Override
	public void start() throws LaunchException {
		throw new LaunchException("Erro State");
	}

	@Override
	public void stop() {

	}

	@Override
	public void check() {

	}

	@Override
	public void prepare() throws PrepareException {
		throw new PrepareException("In error states");
	}

}
