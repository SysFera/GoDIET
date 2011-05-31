package com.sysfera.vishnu;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.vishnu.api.ims.stub.ListProcessesStub;
import com.sysfera.vishnu.api.ims.stub.ProcessIF;
import com.sysfera.vishnu.api.ims.stub.VISHNU_IMSStub;

/**
 * 
 * @author phi
 * 
 */
public class VishnuListener implements Runnable {
	private Logger log = LoggerFactory.getLogger(getClass());

	private int periodicity = 4000;
	private ListProcessesStub currentProcesses;
	private final GoVishnuPlugin goVishnu;
	private boolean stopPolling = false;
	public VishnuListener(GoVishnuPlugin goVishnu) {
		this.goVishnu = goVishnu;
		currentProcesses = new ListProcessesStub();
	}

	// Vishnu Session
	private String sessionID;

	/**
	 * Change periodicity checking. Default is 4000ms
	 * 
	 * @param periodicity
	 */
	public void setPeriodicity(int periodicity) {
		this.periodicity = periodicity;
	}
	
	/**
	 * Stop the polling thread
	 * @param stopPolling
	 */
	public void setStopPolling(boolean stopPolling) {
		this.stopPolling = stopPolling;
	}
	/**
	 * Polling seds modification managed by vishnu ims sed.
	 */
	@Override
	public void run() {
		Thread.currentThread().setName("Vishnu Listener");
		log.debug("Vishnu listening start");

		boolean end = false;

		do{
			try {
				// traitement

				synchronized (this) {
					checkStatus();
					Thread.sleep(periodicity);
					end = this.stopPolling;
				}
			} catch (InterruptedException e) {
					log.error("FATAL: VishnuListener have been interrupted. Data could be in an incoherent state",e);
			}
		
		}while (!end) ;

	
	}

	/**
	 * 
S
	 * 
	 */
	private void checkStatus() {
		ListProcessesStub pulledProcesses = VISHNU_IMSStub.getProcesses(
				sessionID, null);

		@SuppressWarnings("unchecked")
		List<ProcessIF> newProcesses = ListUtils
				.subtract(pulledProcesses.getProcesses(),
						currentProcesses.getProcesses());
		@SuppressWarnings("unchecked")
		List<ProcessIF> deletedProcesses = ListUtils
				.subtract(currentProcesses.getProcesses(),
						pulledProcesses.getProcesses());
		for (ProcessIF processIF : newProcesses) {
			goVishnu.newSedNotification(processIF);
		}
		for (ProcessIF processIF : deletedProcesses) {
			goVishnu.removeSedNotification(processIF);
		}
		currentProcesses = pulledProcesses;
	}

	public void setVishnuSessionId(String sessionID) {
		this.sessionID = sessionID;
	}

}
