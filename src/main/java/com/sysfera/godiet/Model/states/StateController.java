package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.Model.xml.DietResource;
import com.sysfera.godiet.exceptions.InconsistentStateException;
/**
 * Master class of State Design Pattern
 * Currently five State
 * 
 * @author phi
 *
 */
public class StateController {

	final DietResource agent;
	final ReadyStateImpl ready;
	final DownStateImpl down;
	final UpStateImpl up;
	final ErrorStateImpl error;
	// Down by default
	ResourceState state;

	public StateController(DietResource agent) {
		this.agent = agent;
		this.down = new DownStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);
		this.ready = new ReadyStateImpl(this);
		
		//Down by default
		this.state= down;
	}

	public void start() {
		try {
			this.state.start();
		} catch (InconsistentStateException e) {
			// TODO Logger
			e.printStackTrace();
		}

	}

	public void stop() {
		try {
			this.state.stop();
		} catch (InconsistentStateException e) {
			// TODO Logger
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Launch a thread and check the up resource
	 */
	public void check()
	{
		try {
			this.state.check();
		} catch (InconsistentStateException e) {
			// TODO Logger
			e.printStackTrace();
		}
	}

	public void prepare() {
		try {
			this.state.prepare();
		} catch (InconsistentStateException e) {
			// TODO Logger
			e.printStackTrace();
		}
		
	}

	public boolean isRunning()
	{
		return (state instanceof UpStateImpl);
	}
}
