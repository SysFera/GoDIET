package com.sysfera.godiet.model.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.model.SoftwareController;
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

	final SoftwareController softwareController;

	// default check periodicity
	private final static int periodicity = 5000;
	private final Thread checker;
	public StateController(SoftwareManager agent,
			SoftwareController softwareController) {
		this.softwareManaged = agent;
		this.softwareController = softwareController;
		this.down = new IncubateStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);
		this.ready = new ReadyStateImpl(this);
		
		// Down
		this.state = down;
		
		//Start state checker
		Checker ch= new  Checker(periodicity);
		this.checker =new Thread(ch);
		this.checker.start();

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

	public void interruptChecker()
	{
		this.checker.interrupt();
	}
	class Checker implements Runnable {
		private final int periodicity;

		public Checker(int periodicity) {
			this.periodicity = periodicity;
		}

		public void run() {

			while (!Thread.interrupted()) {
				try {
					Thread.sleep(periodicity);
					synchronized (state) {
						state.check();
					}
				} catch (InterruptedException e) {
					log.warn("Checking thread "
							+ softwareManaged.getSoftwareDescription().getId()
							+ " was interrupted");
				}
			}
		}
	}
}
