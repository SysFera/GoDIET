package com.sysfera.godiet.model.observer;

import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.Sed;

public interface PlatformObserver {

	public abstract void newMasterAgent(DietResourceManaged<MasterAgent> ma);
	public abstract void newLocalAgent(DietResourceManaged<LocalAgent> la);
	public abstract void newSed(DietResourceManaged<Sed> sed);
	public abstract void newOmniNames(OmniNamesManaged omniNames);
}