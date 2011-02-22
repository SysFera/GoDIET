package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.exceptions.InconsistentStateException;

public interface ResourceState {

	
	public abstract void prepare() throws InconsistentStateException;
	public abstract void start() throws InconsistentStateException;
	public abstract void stop()throws InconsistentStateException;
	public abstract void check()throws InconsistentStateException;
	
	
}
