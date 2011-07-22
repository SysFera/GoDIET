package com.sysfera.godiet.core.model.observer;

import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;

public interface PlatformObserver {

	public abstract void newMasterAgent(DietResourceManaged<MasterAgent> ma);
	public abstract void newLocalAgent(DietResourceManaged<LocalAgent> la);
	public abstract void newSed(DietResourceManaged<Sed> sed);
	public abstract void newOmniNames(OmniNamesManaged omniNames);
}
