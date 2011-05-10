package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.CheckException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * 
 * The remote agent is ready to be start. Call start to Up State. Call stop to
 * Down State
 * 
 * @author phi
 * 
 */
public class ReadyStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final SoftwareController softwareController;
	private final StateController stateController;
	private final RuntimeValidator validator;
	public ReadyStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.validator = stateController.validator;
		softwareController = stateController.softwareController;
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
			validator.wantLaunch(stateController.softwareManaged);

			softwareController.launch(this.stateController.softwareManaged);
			Thread.sleep(400);
			softwareController.check(this.stateController.softwareManaged);
			this.stateController.toUp();
		} catch (LaunchException e) {
			this.stateController.toError();
			this.stateController.errorCause = e;
			throw e;
		} catch (InterruptedException e) {
			this.stateController.toError();
			this.stateController.errorCause = e;
			throw new LaunchException(
					"Fatal. Thread.sleep have been interrupted", e);
		} catch (CheckException e) {
			this.stateController.errorCause = e;
			this.stateController.toError();
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

	}

	@Override
	public void check() {
		
	}


	@Override
	public State getStatus() {
		return State.READY;
	}
}
