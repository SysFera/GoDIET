package com.sysfera.godiet.common.services;

import java.util.List;

import com.sysfera.godiet.common.exceptions.DietResourceCreationException;
import com.sysfera.godiet.common.exceptions.generics.DietResourceValidationException;
import com.sysfera.godiet.common.exceptions.generics.GoDietServiceException;
import com.sysfera.godiet.common.exceptions.generics.StartException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.exceptions.remote.IncubateException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.LocalAgent;
import com.sysfera.godiet.common.model.generated.MasterAgent;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Sed;
import com.sysfera.godiet.common.model.generated.Software;

public interface PlatformService {

	public abstract void start() throws StartException;

	public abstract void registerMasterAgent(MasterAgent masterAgent)
			throws DietResourceCreationException,
			DietResourceValidationException, IncubateException;

	/**
	 * Create forwarders if need. Listen add diet components event and create
	 * forwarders if need.
	 * TODO: Not yet implemented
	 * @param value
	 */
	public abstract void autoLoadForwarders(boolean value);

	public abstract void registerForwarders(Forwarder client, Forwarder server)
			throws DietResourceCreationException, IncubateException, GraphDataException;

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

//	public abstract ResourceState getSoftwareController(String id);

	public abstract List<SoftwareInterface<? extends Software>> getAllSoftwares();

	public abstract List<SoftwareInterface<Forwarder>> getForwarders();
	public abstract List<SoftwareInterface<Forwarder>> getForwardersServer();
	public abstract List<SoftwareInterface<Forwarder>> getForwardersClient();

	public abstract List<SoftwareInterface<LocalAgent>> getLocalAgents();

	public abstract List<SoftwareInterface<MasterAgent>> getMasterAgents();

	public abstract List<SoftwareInterface<Sed>> getSeds();

	public abstract List<SoftwareInterface<OmniNames>> getOmninames();
	
	public abstract SoftwareInterface<? extends Software> getManagedSoftware(
			String id);
	
	public abstract void startSoftware(String id) throws LaunchException;

	public abstract void prepareSoftware(String id) throws PrepareException;

	public abstract void stopSoftware(String id) throws StopException;



}