package com.sysfera.godiet.model.states;

import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;

public interface ResourceState {

	
	public abstract void prepare() throws  PrepareException;
	public abstract void start() throws  LaunchException;
	public abstract void stop() throws StopException;
	public abstract void check();
	
	
}
