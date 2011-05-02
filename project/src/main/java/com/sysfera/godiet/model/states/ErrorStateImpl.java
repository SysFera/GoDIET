package com.sysfera.godiet.model.states;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.model.SoftwareController;

/**
 * Error State. Currently no way to leave.
 * 
 * @author phi
 * 
 */
public class ErrorStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());
	private final SoftwareController softwareController;
	private final StateController stateController;

	public ErrorStateImpl(StateController stateController) {
		this.softwareController = stateController.softwareController;
		this.stateController = stateController;
	}

	/**
	 * Try to relaunch software
	 * @See {@link ReadyStateImpl#start()}
	 */
	@Override
	public void start() throws LaunchException {
		try {
			softwareController.launch(this.stateController.softwareManaged);
			Thread.sleep(400);
			softwareController.check(this.stateController.softwareManaged);
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
		try {
			softwareController.check(this.stateController.softwareManaged);
			// no exception ? Ok come back up
			log.info(this.stateController.softwareManaged
					.getSoftwareDescription().getId()
					+ "come back UP at "
					+ (new Date()).getTime());

		} catch (CheckException e) {
			synchronized (this.stateController.state) {
				this.stateController.state = this.stateController.error;

			}

		}
	}

	@Override
	public void prepare() throws PrepareException {
		throw new PrepareException("In error states");
	}

	@Override
	public String toString() {
		return "Error";
	}

}
