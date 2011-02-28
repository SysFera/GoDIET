package com.sysfera.godiet.Model.states;

import com.sysfera.godiet.exceptions.InconsistentStateException;
import com.sysfera.godiet.exceptions.LaunchException;

public interface ResourceState {

	
	public abstract void prepare() throws InconsistentStateException;
	public abstract void start() throws InconsistentStateException, LaunchException;
	public abstract void stop()throws InconsistentStateException;
	public abstract void check()throws InconsistentStateException;
	
	
}
