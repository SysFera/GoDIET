package com.sysfera.godiet.core.managers;

import com.sysfera.godiet.core.model.observer.PlatformObserver;

public interface PlatformObservable {

	public abstract void register(PlatformObserver observer);
	public abstract void unregister(PlatformObserver observer);
}
