package com.sysfera.godiet.managers;

import com.sysfera.godiet.model.observer.PlatformObserver;

public interface PlatformObservable {

	public abstract void register(PlatformObserver observer);
	public abstract void unregister(PlatformObserver observer);
}
