package com.sysfera.godiet.core.model.states;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.remote.CheckException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

/**
 * The remote agent is running. Call Stop to stop remote agent and down state
 * Call check to check if the remote agent is already active
 * 
 * 
 * @author phi
 * 
 */
public class UpStateImpl implements ResourceState {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final SoftwareController launcher;
	private final StateController stateController;
	private final RuntimeValidator validator;

	public UpStateImpl(StateController stateController) {
		this.stateController = stateController;
		this.validator = stateController.validator;

		this.launcher = stateController.softwareController;
	}

	@Override
	public void prepare() {
		log.warn("Already run !");
	}

	@Override
	public void start() {
		log.warn("Try to run an up resource !");
	}

	/**
	 * Stop agent Could set the state on Down if ok or error if remote execution
	 * error.
	 */
	@Override
	public void stop() throws StopException {
		synchronized (this.stateController.getState())  {
			try {
				validator.wantStop(stateController.softwareManaged);

				launcher.stop(this.stateController.softwareManaged);
				this.stateController.toDown();
			} catch (StopException e) {
				this.stateController.toError();
				throw e;
			}
		}
	}

	/**
	 * Check if the agent is currently up if not goto error state
	 * 
	 * @throws InconsistentStateException
	 */
	@Override
	public void check() {
		synchronized (this.stateController.getState()) {
			try {
				launcher.check(this.stateController.softwareManaged);
			} catch (CheckException e) {
				this.stateController.toError();
				e.addInfo("UPSTATE", "CHECKERROR", "Check failed at "
						+ (new Date()).getTime());
			}

		}
	}



	@Override
	public State getStatus() {
		return State.UP;
	}

	@Override
	public void incubate() throws IncubateException {
		// TODO Auto-generated method stub
		
	}
}
