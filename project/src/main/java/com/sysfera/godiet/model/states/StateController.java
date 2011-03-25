package com.sysfera.godiet.model.states;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.SoftwareManager;
/**
 * Main class of State Design Pattern
 * Currently five State
 * 
 * @author phi
 */
public class StateController {

	final SoftwareManager softwareManaged;
	final ReadyStateImpl ready;
	final DownStateImpl down;
	final UpStateImpl up;
	final ErrorStateImpl error;
	// Down by default
	ResourceState state;

	public StateController(SoftwareManager agent) {
		this.softwareManaged = agent;
		this.down = new DownStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);
		this.ready = new ReadyStateImpl(this);
		
		//Down
		this.state = down;
	}



	public boolean isRunning()
	{
		return (state instanceof UpStateImpl);
	}
	
	public ResourceState getState() {
		return state;
	}
}
