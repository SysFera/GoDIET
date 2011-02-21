/*@GODIET_LICENSE*/
/*
 * DietPlatform.java
 *
 * Created on 13 avril 2004, 14:51
 */

package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Events.AddElementsEvent;
import com.sysfera.godiet.Model.DietElements;
import com.sysfera.godiet.Model.Elements;
import com.sysfera.godiet.Model.Forwarder;
import com.sysfera.godiet.Model.LaunchInfo;
import com.sysfera.godiet.Model.LocalAgent;
import com.sysfera.godiet.Model.LogCentral;
import com.sysfera.godiet.Model.Ma_dag;
import com.sysfera.godiet.Model.MasterAgent;
import com.sysfera.godiet.Model.OmniNames;
import com.sysfera.godiet.Model.Option;
import com.sysfera.godiet.Model.ServerDaemon;
import com.sysfera.godiet.Model.Services;
import com.sysfera.godiet.Model.physicalresources.GatewayResource;

/**
 * 
 * @author hdail
 */
public class DietPlatformImpl extends java.util.Observable implements
		DietPlatform {
	private ConsoleController consoleCtrl;

	private List<MasterAgent> masterAgents;
	private List<Ma_dag> ma_dags;
	private List<LocalAgent> localAgents;
	private List<ServerDaemon> serverDaemons;
	private List<OmniNames> omniNames;
	private LogCentral logCentral;
	private boolean haveLogCentral = false; // Did user request Logger
	private Services testTool;
	private boolean haveTestTool = false;

	private boolean useDietStats = false;

	// each new DIET component is assigned unique ID.
	// ID counts incremented at each addition & are not re-used.
	private int MA_ID = 0;
	private int MA_DAG_ID = 0;
	private int LA_ID = 0;
	private int SeD_ID = 0;

	private List<Forwarder> forwarders;

	/** Creates a new instance of DietPlatform */
	public DietPlatformImpl(ConsoleController consoleController) {
		this.consoleCtrl = consoleController;

		this.masterAgents = new ArrayList<MasterAgent>();
		this.ma_dags = new ArrayList<Ma_dag>();
		this.localAgents = new ArrayList<LocalAgent>();
		this.serverDaemons = new ArrayList<ServerDaemon>();
		this.omniNames = new ArrayList<OmniNames>();
		this.forwarders = new ArrayList<Forwarder>();
	}

	public void addOmniNames(OmniNames omni) {
		this.omniNames.add(omni);
		setChanged();
		notifyObservers(new AddElementsEvent(omni));
		clearChanged();
	}

	public void addLogCentral(LogCentral logCentral) {
		if (logCentral != null) {
			this.logCentral = logCentral;
			this.haveLogCentral = true;
			setChanged();
			notifyObservers(new AddElementsEvent(logCentral));
			clearChanged();
		} else {
			consoleCtrl.printError("addLogCentral called with null logCentral "
					+ " object.  Continuing without logCentral.");
		}
	}

	public void addTestTool(Services testTool) {
		this.testTool = testTool;
		this.haveTestTool = true;
		setChanged();
		notifyObservers(new AddElementsEvent(testTool));
		clearChanged();
	}

	public void addMasterAgent(MasterAgent newMA) {
		String label = newMA.getName();
		if ((label != null) && (label.length() > 0)) {
			// newMA.setName(label + "_" + MA_ID);
			newMA.setName(label);
		} else {
			newMA.setName("MA_" + MA_ID);
		}
		MA_ID++;
		if (haveLogCentral) {
			addLogServiceOption(newMA);
		}
		this.masterAgents.add(newMA);
		setChanged();
		notifyObservers(new AddElementsEvent(newMA));
		clearChanged();
	}

	public void addMa_dag(Ma_dag newMa_dag) {
		String label = newMa_dag.getName();
		if ((label != null) && (label.length() > 0)) {
			// newMa_dag.setName(label + "_" + MA_DAG_ID);
			newMa_dag.setName(label);
		} else {
			newMa_dag.setName("MA_DAG_" + MA_DAG_ID);
		}
		if (haveLogCentral) {
			addLogServiceOption(newMa_dag);
		}
		MA_DAG_ID++;
		this.ma_dags.add(newMa_dag);
		setChanged();
		notifyObservers(new AddElementsEvent(newMa_dag));
		clearChanged();
	}

	public void addLocalAgent(LocalAgent newLA) {
		String label = newLA.getName();
		if ((label != null) & (label.length() > 0)) {
			newLA.setName(label + "_" + LA_ID);
		} else {
			newLA.setName("LA_" + LA_ID);
		}
		LA_ID++;
		if (haveLogCentral) {
			addLogServiceOption(newLA);
		}
		this.localAgents.add(newLA);
		setChanged();
		notifyObservers(new AddElementsEvent(newLA));
		clearChanged();
	}

	public void addServerDaemon(ServerDaemon newSeD) {
		String label = newSeD.getName();
		if ((label != null) && (label.length() > 0)) {
			newSeD.setName(label + "_" + SeD_ID);
		} else {
			newSeD.setName("SeD_" + SeD_ID);
		}
		SeD_ID++;
		if (haveLogCentral) {
			addLogServiceOption(newSeD);
		}
		this.serverDaemons.add(newSeD);
		setChanged();
		notifyObservers(new AddElementsEvent(newSeD));
		clearChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getOmniNames()
	 */
	@Override
	public List<OmniNames> getOmniNames() {
		return this.omniNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getLogCentral()
	 */
	@Override
	public LogCentral getLogCentral() {
		return this.logCentral;
	}

	@Override
	public boolean useLogCentral() {
		return this.haveLogCentral;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getTestTool()
	 */
	@Override
	public Services getTestTool() {
		return this.testTool;
	}

	@Override
	public boolean useTestTool() {
		return this.haveTestTool;
	}

	public void setUseDietStats(boolean flag) {
		this.useDietStats = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getUseDietStats()
	 */
	@Override
	public boolean getUseDietStats() {
		return this.useDietStats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getMasterAgents()
	 */
	@Override
	public List<MasterAgent> getMasterAgents() {
		return this.masterAgents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getMa_dags()
	 */
	@Override
	public List<Ma_dag> getMa_dags() {
		return this.ma_dags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getLocalAgents()
	 */
	@Override
	public List<LocalAgent> getLocalAgents() {
		return this.localAgents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getServerDaemons()
	 */
	@Override
	public List<ServerDaemon> getServerDaemons() {
		return this.serverDaemons;
	}

	public void printStatus() {
		java.util.Iterator it;
		MasterAgent mAgent;
		Ma_dag ma_dag;
		LocalAgent lAgent;
		ServerDaemon sed;

		String platStatus = getStatusHeader();
		for (OmniNames om : omniNames) {
			platStatus += getStatusString(om);
		}

		if (haveLogCentral) {
			platStatus += getStatusString(logCentral);
			if (haveTestTool) {
				platStatus += getStatusString(testTool);
			}
		}
		for (it = masterAgents.iterator(); it.hasNext();) {
			mAgent = (MasterAgent) it.next();
			platStatus += getStatusString(mAgent);
		}
		for (it = ma_dags.iterator(); it.hasNext();) {
			ma_dag = (Ma_dag) it.next();
			platStatus += getStatusString(ma_dag);
		}
		for (it = localAgents.iterator(); it.hasNext();) {
			lAgent = (LocalAgent) it.next();
			platStatus += getStatusString(lAgent);
		}
		for (it = serverDaemons.iterator(); it.hasNext();) {
			sed = (ServerDaemon) it.next();
			platStatus += getStatusString(sed);
		}

		consoleCtrl.printOutput(platStatus);

		/*
		 * setChanged(); notifyObservers(new StatusInfosEvent(platStatus));
		 * clearChanged();
		 */
	}

	private String getStatusHeader() {
		return "Status   Element   LaunchState   LogState   "
				+ "Resource     PID\n";
	}

	private String getStatusString(Elements element) {
		String status = paddedString(" ", 8) + " "
				+ paddedString(element.getName(), 9) + " ";
		if (element == null) {
			status += "NULL\n";
			return status;
		}
		LaunchInfo launch = element.getLaunchInfo();

		status += paddedString(
				com.sysfera.godiet.Defaults.getLaunchStateString(launch
						.getLaunchState()), 13)
				+ " ";
		status += paddedString(
				com.sysfera.godiet.Defaults.getLogStateString(launch
						.getLogState()), 10)
				+ " ";
		status += paddedString(element.getComputeResource().getName(), 12)
				+ " ";
		if (launch.getPID() > 0) {
			status += launch.getPID();
		}
		status += "\n";

		return status;
	}

	private String paddedString(String input, int outputLength) {
		if (outputLength <= input.length()) {
			return input.substring(0, outputLength);
		} else {
			String output = input;
			for (int i = input.length(); i < outputLength; i++) {
				output += " ";
			}
			return output;
		}
	}

	private void addLogServiceOption(DietElements dEl) {
		dEl.getElementCfg().addOption(new Option("useLogService", "1"));
		dEl.getElementCfg().addOption(new Option("lsOutbuffersize", "0"));
		dEl.getElementCfg().addOption(new Option("lsFlushinterval", "10000"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#addForwarder(Forwarder
	 * forwarder)
	 */
	@Override
	public void addForwarder(Forwarder forwarder) {
		this.forwarders.add(forwarder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.managers.DietPlatform#getForwarders()
	 */
	@Override
	public List<Forwarder> getForwarders() {
		return this.forwarders;
	}

}
