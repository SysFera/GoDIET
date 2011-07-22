package com.sysfera.godiet.core.model.states;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.remote.CheckException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.validators.RuntimeValidator;

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
	private final RuntimeValidator validator;

	public ErrorStateImpl(StateController stateController) {
		this.softwareController = stateController.softwareController;
		this.stateController = stateController;
		this.validator = stateController.validator;
	}

	/**
	 * Try to relaunch software
	 * 
	 * @See {@link ReadyStateImpl#start()}
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
			log.error(stateController.softwareManaged.getSoftwareDescription().getId() + " launching error",e);
			this.stateController.toError();
			throw e;
		} catch (InterruptedException e) {
			log.error(stateController.softwareManaged.getSoftwareDescription().getId() + " launching error",e);

			this.stateController.toError();
			throw new LaunchException(
					"Fatal. Thread.sleep have been interrupted", e);
		} catch (CheckException e) {
			log.error(stateController.softwareManaged.getSoftwareDescription().getId() + " checking status error",e);

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
		try {
			softwareController.check(this.stateController.softwareManaged);
			// no exception ? Ok come back up
			
			log.info(this.stateController.softwareManaged
					.getSoftwareDescription().getId()
					+ "come back UP at "
					+ (new Date()).getTime());
			synchronized (this.stateController.getState()) {
				this.stateController.toUp();

			}

		} catch (CheckException e) {
			//nothing to do
		}
	}

	@Override
	public void prepare() throws PrepareException {
		throw new PrepareException("In error states");
	}

	@Override
	public State getStatus() {
		return State.ERROR;
	}

	@Override
	public void incubate() throws IncubateException {
		// TODO Auto-generated method stub
		
	}

}
