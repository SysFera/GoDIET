package com.sysfera.godiet.Model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.Utils.RemoteConfigurationHelper;
import com.sysfera.godiet.exceptions.LaunchException;
import com.sysfera.godiet.exceptions.PrepareException;

/**
 * Init State. The remote agent isn't yet bind. Call prepare to state ready
 * 
 * @author phi
 * 
 */
public class DownStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final RemoteConfigurationHelper launcher;
	private final StateController stateController;

	public DownStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.launcher = RemoteConfigurationHelper.getInstance();
	}

	/**
	 * Prepare agent to run (typically create and copy remote files)
	 * @throws PrepareException 
	 */
	@Override
	public void prepare() throws PrepareException {
		try {
			launcher.configure(this.stateController.agent);
			this.stateController.state = this.stateController.ready;
		} catch (PrepareException e) {
			this.stateController.state = this.stateController.error;
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
