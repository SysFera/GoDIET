package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;

/**
 * 
 * The remote agent is ready to be start. Call start to Up State Call stop to
 * Down State
 * 
 * @author phi
 * 
 */
public class ReadyStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final SoftwareController launcher;
	private final StateController stateController;

	public ReadyStateImpl(StateController stateController) {
		this.stateController = stateController;
		launcher = stateController.softwareControler;
	}

	@Override
	public void prepare() throws PrepareException {
		log.warn("Already prepared !");
	}

	/**
	 * Start agent
	 * 
	 * @throws LaunchException
	 */
	@Override
	public void start() throws LaunchException {
		try {
			launcher.launch(this.stateController.softwareManaged);
			Thread.sleep(400);
			launcher.check(this.stateController.softwareManaged);
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
