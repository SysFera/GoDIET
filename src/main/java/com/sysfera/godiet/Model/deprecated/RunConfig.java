/*@GODIET_LICENSE*/
/*
 * RunConfig.java
 *
 * Created on May 13, 2004, 11:00 AM
 */

package com.sysfera.godiet.Model.deprecated;


/**
 * 
 * @author hdail
 */
public class RunConfig {
	private int debugLevel = 1;
	private boolean saveStdOut = true;
	private boolean saveStdErr = true;
	private boolean useUniqueDirs = true;
	private String localScratch = null;
	private boolean localScratchReady = false;
	private String runLabel = "";
	private int watcherPeriod = 30000;

	/** Creates a new instance of RunConfig */
	public RunConfig() {

	}

	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}

	public int getDebugLevel() {
		return this.debugLevel;
	}



	public void setRunLabel(String label) {
		this.runLabel = label;
	}

	public String getRunLabel() {
		return this.runLabel;
	}

	public void setLocalScratch(String scratchDir) {
		this.localScratch = scratchDir;
	}

	public String getLocalScratch() {
		return this.localScratch;
	}

	public void setLocalScratchReady(boolean flag) {
		this.localScratchReady = flag;
	}

	public boolean isLocalScratchReady() {
		return this.localScratchReady;
	}

	public void setWatcherPeriod(int period) {
		/* Period is given in seconds, but should be milliseconds in the code */
		this.watcherPeriod = period * 1000;
	}

	public int getWatcherPeriod() {
		return this.watcherPeriod;
	}



	public void setSaveStdErr(boolean saveStdErr) {
		this.saveStdErr = saveStdErr;
	}

	public boolean isSaveStdErr() {
		return saveStdErr;
	}

	public void setSaveStdOut(boolean saveStdOut) {
		this.saveStdOut = saveStdOut;
	}

	public boolean isSaveStdOut() {
		return saveStdOut;
	}

//	public void addLocalScratchBase(Domain domain, String scratchDir) {
//		this.scratchDirs.put(domain, scratchDir);
//	}

//	public String getLocalScratchBase(Domain domain) {
//		return scratchDirs.get(domain);
//	}
//	public Collection<String> getLocalScratchBase() {
//		return scratchDirs.values();
//	}
}
