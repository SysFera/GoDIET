package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;

/**
 * Error State. Currently no way to leave.
 * 
 * @author phi
 * 
 */
public class DownStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final StateController stateController;

	public DownStateImpl(StateController stateController) {
		this.stateController = stateController;
	}


	/**
	 * Relaunch the software
	 */
	@Override
	public void start() throws LaunchException {
		try {
			stateController.softwareController.launch(this.stateController.softwareManaged);
			Thread.sleep(400);
			stateController.softwareController.check(this.stateController.softwareManaged);
			this.stateController.state = this.stateController.up;
		} catch (LaunchException e) {
			this.stateController.state = this.stateController.error;
			throw e;
		} catch (InterruptedException e) {
			this.stateController.state = this.stateController.error;
			throw new LaunchException(
					"Fatal. Thread.sleep have been interrupted", e);
		} catch (CheckException e) {
			this.stateController.state = this.stateController.error;
			throw new LaunchException(
					"The check start failed. Couldn't be sure that the process is up",
					e);
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public void check() {
		log.warn("Check a "+this+" state resource");
	}

	@Override
	public void prepare() throws PrepareException {
		throw new PrepareException("In error states");
	}
	@Override
	public String toString() {
		return "Down";
	}

}
