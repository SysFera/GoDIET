package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.model.SoftwareManager;

/**
 * Main class of State Design Pattern Currently five State
 * 
 * @author phi
 */
public class StateController {
	private Logger log = LoggerFactory.getLogger(getClass());

	final SoftwareManager softwareManaged;
	final ReadyStateImpl ready;
	final IncubateStateImpl down;
	final UpStateImpl up;
	final ErrorStateImpl error;
	// Down by default
	ResourceState state;

	// Error reason
	Throwable errorCause = null;

	public StateController(SoftwareManager agent) {
		this.softwareManaged = agent;
		this.down = new IncubateStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);
		this.ready = new ReadyStateImpl(this);

		// Down
		this.state = down;
	}

	/**
	 * Return the error cause if the resource are in ErrorState
	 * 
	 * @return Error
	 */
	public Throwable getErrorCause() {
		if (errorCause != null && !(state instanceof ErrorStateImpl)) {
			log.error("FATAL: the resource is in "
					+ state.getClass().getCanonicalName()
					+ " state and have the errorCause set with"
					+ errorCause.getClass().getCanonicalName());
			return null;
		}
		if (errorCause == null && (state instanceof ErrorStateImpl)) {
			log.error("FATAL: the resource is in Error state and with errorCause = null");
		}
		return errorCause;
	}

	public boolean isRunning() {
		return (state instanceof UpStateImpl);
	}

	public ResourceState getState() {
		return state;
	}
}
