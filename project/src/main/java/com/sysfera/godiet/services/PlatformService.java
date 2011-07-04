package com.sysfera.godiet.services;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.exceptions.generics.GoDietServiceException;
import com.sysfera.godiet.exceptions.generics.StartException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.states.ResourceState;

public interface PlatformService {

	public abstract void start() throws StartException;

	public abstract void registerMasterAgent(MasterAgent masterAgent)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException;

	/**
	 * Create forwarders if need. Listen add diet components event and create
	 * forwarders if need.
	 * 
	 * @param value
	 */
	public abstract void autoLoadForwarders(boolean value);

	public abstract void registerForwarders(Forwarder client, Forwarder server)
			throws DietResourceCreationException, IncubateException;

	public abstract void registerLocalAgent(LocalAgent localAgent)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException;

	public abstract void registerSed(Sed sed)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException;

	public abstract void registerOmniNames(OmniNames omniNames)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException;
	
	

	/**
	 * TODO: Currently only stop. Not useful. Destroy ? ORM ...
	 * 
	 * @param software
	 * @throws GoDietServiceException. If
	 *             software isn't managed by this resources manager.
	 */
	public abstract void unregisterSoftware(Software software)
			throws GoDietServiceException, StopException;

	public abstract ResourceState getSoftwareController(String id);

}