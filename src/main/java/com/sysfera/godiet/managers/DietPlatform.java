package com.sysfera.godiet.managers;

import java.util.List;

import com.sysfera.godiet.Model.Forwarder;
import com.sysfera.godiet.Model.LocalAgent;
import com.sysfera.godiet.Model.LogCentral;
import com.sysfera.godiet.Model.Ma_dag;
import com.sysfera.godiet.Model.MasterAgent;
import com.sysfera.godiet.Model.OmniNames;
import com.sysfera.godiet.Model.ServerDaemon;
import com.sysfera.godiet.Model.Services;

public interface DietPlatform {

	public abstract List<OmniNames> getOmniNames();

	public abstract LogCentral getLogCentral();

	public abstract Services getTestTool();

	public abstract boolean getUseDietStats();

	public abstract List<MasterAgent> getMasterAgents();

	public abstract List<Ma_dag> getMa_dags();

	public abstract List<LocalAgent> getLocalAgents();

	public abstract List<ServerDaemon> getServerDaemons();

	/**
	 * Return the Diet forwarders of the Plateform
	 * @return Forwarders
	 */
	public abstract List<Forwarder> getForwarders();

	public abstract boolean useLogCentral();

	public abstract boolean useTestTool();
	/**
	 * Add a Diet forwarder to the Diet Platform
	 * @param the forwarder to add
	 */
	public abstract void addForwarder(Forwarder forwarder);

	
}