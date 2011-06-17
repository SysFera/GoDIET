package com.sysfera.vishnu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.exceptions.remote.LaunchException;
import com.sysfera.godiet.exceptions.remote.PrepareException;
import com.sysfera.godiet.exceptions.remote.StopException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.vishnu.api.ims.stub.ProcessIF;

public class GoVishnuPlugin {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final DietManager dietModel;
	private final GoVishnuSedFactory govishnuFactory;
	private final VishnuListener vishnuListener;
	private final Thread vishnuListenerThread;

	public GoVishnuPlugin(DietManager dietModel) {

		this.dietModel = dietModel;
		this.govishnuFactory = new GoVishnuSedFactory(dietModel);
		vishnuListener = new VishnuListener(this);
		this.vishnuListenerThread = new Thread(vishnuListener);
	}

	/**
	 * 
	 * @param periodicity
	 *            The polling periocity time in milliseconds
	 */
	public void init(int periodicity) {
		// TODO: get session ID

		startListener(periodicity);
	}

	public void startListener(int periodicity) {
		try {
			vishnuListener.setPeriodicity(periodicity);
			vishnuListenerThread.start();
		} catch (IllegalThreadStateException e) {
			log.warn("Vishnu listener thread already started");
		}
	}

	public void stopListener() {
		vishnuListener.setStopPolling(true);
	}

	/**
	 * Add a new sed in diet manager and start it. Create a sed description and
	 * set the id and the pluggedon.
	 * 
	 * @param process
	 */
	public void newSedNotification(ProcessIF process) {
		log.debug("New sed discover: " + process.getDietId());
		Sed sedDescription = new ObjectFactory().createSed();
		sedDescription.setId(process.getDietId());

		// Plugged on process.getMachineId()
		Config config = new ObjectFactory().createConfig();
		config.setServer(process.getMachineId());
		sedDescription.setConfig(config);

		try {
			DietResourceManaged drmanaged = govishnuFactory
					.create(sedDescription);
			dietModel.addSed(drmanaged);
			drmanaged.prepare();
			drmanaged.start();
		} catch (IncubateException e) {
			log.error(
					"Unable to add software in manager " + process.getDietId(),
					e);
		} catch (DietResourceCreationException e) {
			// TODO: if already created, change manager
			log.error(
					"Unable to add software in manager " + process.getDietId(),
					e);
		} catch (PrepareException e) {
			log.error(
					"Must never happened. Unable to prepare"
							+ process.getDietId(), e);

		} catch (LaunchException e) {
			log.error(
					"Must never happened. Unable to start"
							+ process.getDietId(), e);
		}
	}

	/**
	 * Stop sed. Need to be
	 * 
	 * @param process
	 */
	public void deletedSedNotification(ProcessIF process) {
		log.debug("Sed state change: " + process.getDietId());
		List<DietResourceManaged> seds = dietModel.getSeds();
		for (DietResourceManaged dietResourceManaged : seds) {
			if (dietResourceManaged.getSoftwareDescription().getId()
					.equals(process.getDietId())) {
				try {
					dietResourceManaged.stop();
				} catch (StopException e) {
					log.error("Must never happened", e);
				}
				return;
			}
		}
		log.error(process.getDietId() + " expected to be register.");

	}

	public void waitProperExit() {

		try {
			log.debug("Waiting polling thread proper exit");
			vishnuListenerThread.join();
		} catch (InterruptedException e) {
			log.error("FATAL. Waiting vishnu's polling thread exit failed ", e);
		}
	}

}
