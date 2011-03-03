package com.sysfera.godiet.model.states;

import com.sysfera.godiet.model.DietResourceManager;
/**
 * Master class of State Design Pattern
 * Currently five State
 * 
 * @author phi
 */
public class StateController {

	final DietResourceManager agent;
	final ReadyStateImpl ready;
	final DownStateImpl down;
	final UpStateImpl up;
	final ErrorStateImpl error;
	// Down by default
	ResourceState state;

	public StateController(DietResourceManager agent) {
		this.agent = agent;
		this.down = new DownStateImpl(this);
		this.up = new UpStateImpl(this);
		this.error = new ErrorStateImpl(this);
		this.ready = new ReadyStateImpl(this);
		
		//Down by default
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
