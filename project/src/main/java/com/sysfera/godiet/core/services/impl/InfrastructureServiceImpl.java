package com.sysfera.godiet.core.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Cluster;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.observer.InfrastructureObervable;
import com.sysfera.godiet.common.model.observer.InfrastructureObserver;
import com.sysfera.godiet.common.services.InfrastructureService;
import com.sysfera.godiet.core.managers.InfrastructureManager;

@Component
public class InfrastructureServiceImpl implements InfrastructureService,
		InfrastructureObervable {

	List<WrappedObserver> observers = new ArrayList<WrappedObserver>();
	@Autowired
	private InfrastructureManager instrastructureManager;

	public InfrastructureServiceImpl() {
		fakeEventsGenerator.start();

	}

	@PreDestroy
	public void stopService() {
		fakeEventsGenerator.interrupt();
	}

	@Override
	public void registerDomains(List<Domain> domains)
			throws ResourceAddException, GraphDataException {
		this.instrastructureManager.addDomains(domains);

	}

	@Override
	public void registerNodes(List<Node> nodes) throws ResourceAddException,
			GraphDataException {
		this.instrastructureManager.addNodes(nodes);
	}

	@Override
	public void registerClusters(List<Cluster> clusters)
			throws ResourceAddException {
		this.instrastructureManager.addClusters(clusters);
	}

	@Override
	public void registerLinks(List<Link> links) throws ResourceAddException,
			GraphDataException {
		this.instrastructureManager.addLinks(links);
	}

	@Override
	public List<Node> getNodes() {
		return this.instrastructureManager.getNodes();
	}

	@Override
	public void addObserver(InfrastructureObserver observer) {
		WrappedObserver mo = new WrappedObserver(observer);
		observers.add(mo);
		System.out.println("Added observer:" + mo);

	}

	Thread fakeEventsGenerator = new Thread() {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					// ignore
				}
				// setChanged();
				notifyObservers(new Date());
			}
		}

	};

	public void notifyObservers(Object param) {
		for (WrappedObserver obs : observers) {

			obs.update(this, "update");

		}
	}

	private class WrappedObserver implements InfrastructureObserver,
			Serializable {

		private static final long serialVersionUID = 1L;

		private InfrastructureObserver ro = null;

		public WrappedObserver(InfrastructureObserver ro) {
			this.ro = ro;

		}

		@Override
		public void update(Object o, Object arg) {
			try {
				ro.update(o.toString(), arg);
			} catch (RemoteException e) {
				System.out
						.println("Remote exception removing observer:" + this);
			}
		}

	}

}
