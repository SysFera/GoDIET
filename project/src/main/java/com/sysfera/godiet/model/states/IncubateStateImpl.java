package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

/**
 * Init State. The remote agent isn't yet bind. Call prepare to state ready.
 * 
 * @author phi
 * 
 */
public class IncubateStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final SoftwareController launcher;
	private final StateController stateController;

	public IncubateStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.launcher = stateController.softwareControler;
	}

	/**
	 * Prepare agent to run (typically create and copy remote files)
	 * @throws PrepareException 
	 */
	@Override
	public void prepare() throws PrepareException {
		try {
			launcher.configure(this.stateController.softwareManaged);
			this.stateController.state = this.stateController.ready;
		} catch (PrepareException e) {
			this.stateController.state = this.stateController.error;
			this.stateController.errorCause = e;

			throw e;
		}

	}

	@Override
	public void start() throws LaunchException {
		throw new LaunchException("Need prepare first");
	}

	@Override
	public void stop() {
	}

	@Override
	public void check() {

	}

}
