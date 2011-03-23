package com.sysfera.godiet.model.states;

import com.sysfera.godiet.model.DietResourceManaged;
/**
 * Main class of State Design Pattern
 * Currently five State
 * 
 * @author phi
 */
public class StateController {

	final DietResourceManaged agent;
	final ReadyStateImpl ready;
	final DownStateImpl down;
	final UpStateImpl up;
	final ErrorStateImpl error;
	// Down by default
	ResourceState state;

	public StateController(DietResourceManaged agent) {
		this.agent = agent;
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
