package com.sysfera.godiet.common.model.states;

import java.io.Serializable;

import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;

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
public interface ResourceState extends Serializable {
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
