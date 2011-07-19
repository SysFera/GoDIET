package com.sysfera.godiet.model.states;

import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;

/**
 * Contract of resource state
 * 
 * @see DownStateImpl
 * @see UpStateImpl
 * @see IncubateStateImpl
 * @see ReadyStateImpl
 * @see Error
 * @author phi
 *
 */
public interface ResourceState {
	static public enum State{
		INCUBATE,DOWN,READY,UP,ERROR
	}
	
	/**
	 * Return the current state
	 * @return State the current State
	 */
	public abstract State getStatus();
	
	public abstract void incubate() throws IncubateException;
	public abstract void prepare() throws  PrepareException;
	public abstract void start() throws  LaunchException;
	public abstract void stop() throws StopException;
	public abstract void check();
	
	
}
