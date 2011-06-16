package com.sysfera.godiet.model.states;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Main class of State Design Pattern Currently five State Incubate, Ready, Up,
 * Down,Error
 * 
 * @author phi
 */
public class StateController {
	private Logger log = LoggerFactory.getLogger(getClass());

	final SoftwareManager softwareManaged;
	private final ReadyStateImpl ready;
	private final DownStateImpl down;
	private final IncubateStateImpl incubate;
	private final UpStateImpl up;
	private final ErrorStateImpl error;
	// Down by default
	private ResourceState state;

	// Error reason
	Throwable errorCause = null;

	// Do the concrete action
	final SoftwareController softwareController;

	// store the date of the last state transition
	private Date lastTransition;

	// Runtime validator
	final RuntimeValidator validator;
	// default check periodicity
	private final static int PERIODICITY = 5000;
	private final Thread checker;

	public StateController(SoftwareManager agent,
			SoftwareController softwareController, RuntimeValidator validator) throws IncubateException {
		this.validator = validator;
		this.softwareManaged = agent;
		this.softwareController = softwareController;
		this.incubate = new IncubateStateImpl(this);

		this.down = new DownStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);

		this.ready = new ReadyStateImpl(this);

		this.lastTransition = Calendar.getInstance().getTime();

		// Start state checker
		Checker ch = new Checker(PERIODICITY);
		this.checker = new Thread(ch);
		// Incubate
		toIncubate();

	}

	private void startChecking() {
		try {
			this.checker.start();
			log.info("Checking start for " + this.checker.getName());
		} catch (IllegalThreadStateException e) {
			log.error("Try to start a running thread");
		}
	}

	private void stopChecking() {
		log.info("Checking stop for " + this.checker.getName());
		this.checker.interrupt();
	}

	void toIncubate() throws IncubateException {
		stopChecking();
		errorCause = null;
		this.softwareManaged.setPid(null);
		this.lastTransition = Calendar.getInstance().getTime();
		this.state = incubate;
		this.state.incubate();
	}

	void toDown() {
		stopChecking();
		errorCause = null;
		this.softwareManaged.setPid(null);
		this.lastTransition = Calendar.getInstance().getTime();
		this.state = down;
	}

	void toUp() {
		errorCause = null;
		this.lastTransition = Calendar.getInstance().getTime();
		this.state = up;
		startChecking();

	}

	void toReady() {
		errorCause = null;
		this.lastTransition = Calendar.getInstance().getTime();
		this.state = ready;
	}

	void toError() {
		this.lastTransition = Calendar.getInstance().getTime();
		this.state = error;
	}

	/**
	 * Return the error cause if the resource are in ErrorState
	 * 
	 * @return Error
	 */
	public Throwable getErrorCause() {
		return errorCause;
	}

	/**
	 * 
	 * Return the current state Use with carefully. State is periodically
	 * checked and could be change in independent thread
	 * 
	 * @return ResourceState. The state
	 */
	public ResourceState getState() {
		return state;
	}

	public void interruptChecker() {
		this.checker.interrupt();
	}

	/**
	 * Return the Date of the last transition
	 * 
	 * @author phi
	 * 
	 */
	public Date getLastTransition() {
		return lastTransition;
	}

	/*
	 * Thread which call periodically state.check
	 */
	class Checker implements Runnable {
		private final int periodicity;

		public Checker(int periodicity) {
			this.periodicity = periodicity;

		}

		public void run() {
			if (softwareManaged.getSoftwareDescription() != null
					&& softwareManaged.getSoftwareDescription().getId() != null)
				Thread.currentThread().setName(
						softwareManaged.getSoftwareDescription().getId());
			log.debug("Checking thread start: " + softwareManaged.getSoftwareDescription().getId() ); 
			while (!Thread.interrupted()) {
				try {
					

					Thread.sleep(periodicity);
					synchronized (state) {
						state.check();
						log.debug("State: " + state.getStatus() ); 
					}
				} catch (InterruptedException e) {
					log.debug("Checking thread "
							+ softwareManaged.getSoftwareDescription().getId()
							+ " was interrupted");
					break;
				}
			}
		}
	}
}
