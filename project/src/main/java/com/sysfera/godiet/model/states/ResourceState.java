package com.sysfera.godiet.model.states;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;

public interface ResourceState {

	
	public abstract void prepare() throws  PrepareException;
	public abstract void start() throws  LaunchException;
	public abstract void stop();
	public abstract void check();
	
	
}
