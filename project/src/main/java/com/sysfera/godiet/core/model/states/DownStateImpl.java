package com.sysfera.godiet.core.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.remote.CheckException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * Error State. Currently no way to leave.
 * 
 * @author phi
 * 
 */
public class DownStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final StateController stateController;
	private final RuntimeValidator validator;

	public DownStateImpl(StateController stateController) {
		this.validator = stateController.validator;
		this.stateController = stateController;
	}

	/**
	 * Relaunch the software
	 */
	@Override
	public void start() throws LaunchException {
		try {
			validator.wantLaunch(stateController.softwareManaged);
			stateController.softwareController
					.launch(this.stateController.softwareManaged);
			Thread.sleep(400);
			stateController.softwareController
					.check(this.stateController.softwareManaged);
			this.stateController.toUp();
		} catch (LaunchException e) {
			this.stateController.toError();
			throw e;
		} catch (InterruptedException e) {
			this.stateController.toError();
			throw new LaunchException(
					"Fatal. Thread.sleep have been interrupted", e);
		} catch (CheckException e) {
			this.stateController.toError();
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
		log.warn("Check a " + this + " state resource");
	}

	@Override
	public void prepare() throws PrepareException {
		log.warn("Try to prepare a DOWN state resource "
				+ stateController.softwareManaged.getSoftwareDescription()
						.getId());
		
	}

	@Override
	public State getStatus() {
		return State.DOWN;
	}

	@Override
	public void incubate() throws IncubateException {
		
	}

}
