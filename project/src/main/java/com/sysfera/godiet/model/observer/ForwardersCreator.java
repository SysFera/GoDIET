package com.sysfera.godiet.model.observer;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.DomainsManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.managers.topology.TopologyManager;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.factories.ForwardersFactory;
import com.sysfera.godiet.model.factories.ForwardersFactory.ForwarderType;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.services.PlatformService;

/**
 * 
 * Initialize and add forwarders in data model. Listen platform event and
 * 
 * 
 */
@Component
public class ForwardersCreator implements PlatformObserver {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DomainsManager domainsManager;
	@Autowired
	private ResourcesManager rm;
	private TopologyManager tm;

	@Autowired
	private PlatformService platformController;

	/**
	 * First get all already resources register and create forwarder and
	 * register to the platform events.
	 */
	public void start() {
		tm = this.rm.getInfrastructureModel().getTopologyManager();

		rm.getDietModel().register(this);
		init();
	}

	public void stop() {
		rm.getDietModel().unregister(this);
	}

	@Override
	public void newMasterAgent(DietResourceManaged<MasterAgent> ma) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newLocalAgent(DietResourceManaged<LocalAgent> la) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newSed(DietResourceManaged<Sed> sed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newOmniNames(OmniNamesManaged omniNames) {
		// TODO Auto-generated method stub

	}

	private void init() {
		List<SoftwareManager<? extends Software>> managedSoftwaresFrom = this.rm
				.getDietModel().getAllManagedSoftware();
		List<SoftwareManager<? extends Software>> managedSoftwaresTo = this.rm
				.getDietModel().getAllManagedSoftware();
		if (managedSoftwaresFrom != null) {

			// TODO improve
			for (SoftwareManager<? extends Software> softwareManagerFrom : managedSoftwaresFrom) {
				Resource from = softwareManagerFrom.getPluggedOn();
				if (from == null) {
					// TODO error handler
					log.error(softwareManagerFrom.getSoftwareDescription()
							.getId()
							+ " not plugged!. Couldn't initialize forwarder");
					continue;

				}
				for (SoftwareManager<? extends Software> softwareManagerTo : managedSoftwaresTo) {
					Resource to = softwareManagerTo.getPluggedOn();
					if (to == null) {
						// TODO error handler
						log.error(softwareManagerTo.getSoftwareDescription()
								.getId()
								+ " not plugged!. Couldn't initialize forwarder");
						continue;
					}
					if (from != to)
						compute(from, to);
				}
			}
		}
	}

	private final ObjectFactory factory = new ObjectFactory();

	/**
	 * Add the minimum of forwarders needed to cross all domains covered by the
	 * path between 'from' and 'to'
	 * 
	 * @param from
	 * @param to
	 */
	private void compute(Resource from, Resource to) {
		if (from == null || to == null) {
			log.error("Couldn't initialize forwarder. From = " + " To = ");
			return;
		}
		try {
			Path p = tm.findPath(from, to);
			LinkedHashSet<Hop> path = p.getPath();
			Resource currentSource = from;
			Domain incomingDomain = null;
			for (Hop hop : path) {
				// Search when we cross domain

				// First Hop. We don't from which domain we come from. if source
				// and destination shared same domains, no need to create a
				// domain link
				if (incomingDomain == null) {
					boolean isSharingDomain = false;

					Set<Domain> domainsSource = domainsManager
							.getDomains(currentSource);
					Set<Domain> domainsDestination = domainsManager
							.getDomains(hop.getDestination());
					for (Domain domain : domainsSource) {
						if (domainsDestination.contains(domain)) {
							isSharingDomain = true;
							break;
						}
					}
					// If they are not share a domain, select one of the source
					// (and so create forwarder in the next part)
					if (!isSharingDomain) {
						incomingDomain = domainsSource.toArray(new Domain[0])[0];
						log.debug(incomingDomain.getId() + "->"
								+ hop.getLink().getDomain().getId());
					}

				}
				if (incomingDomain != null
						&& !domainsManager.isConnected(incomingDomain, hop
								.getLink().getDomain())) {
					Forwarder clientForwarder = buildForwarder(currentSource, hop.getDestination(),
							ForwarderType.CLIENT);
					Forwarder serverForwarder = buildForwarder(currentSource, hop.getDestination(),
							ForwarderType.SERVER);

					try {
						platformController.registerForwarders(clientForwarder,
								serverForwarder);
					} catch (DietResourceCreationException e) {
						// TODO error handler
						log.error("Unable to load forwarders"
								+ clientForwarder.getId() + " "
								+ serverForwarder.getId());
					} catch (IncubateException e) {
						// TODO error handler
						log.error("Unable to load forwarders"
								+ clientForwarder.getId() + " "
								+ serverForwarder.getId());
					}

				}
				currentSource = hop.getDestination();
				incomingDomain = hop.getLink().getDomain();

			}

		} catch (PathException e) {
			log.error("Unable to find path between " + from.getId() + " and "
					+ to.getId() + "Couldn't initialize forwarder");
		}
	}

	private Forwarder buildForwarder(Resource from, Resource to,
			ForwardersFactory.ForwarderType type) {
		Forwarder clientForwarder = factory.createForwarder();

		Config clientconfig = factory.createConfig();
		clientconfig.setRemoteBinary("dietForwarder");
		clientForwarder.setConfig(clientconfig);
		String name = "DietForwarder-" + from.getId() + "-" + to.getId() + "-";
		if (type == ForwarderType.CLIENT) {
			clientconfig.setServer(from.getId());
			clientForwarder.setId(name + "CLIENT");
			clientForwarder.setType("CLIENT");
		} else {
			clientconfig.setServer(to.getId());
			clientForwarder.setId(name + "SERVER");
			clientForwarder.setType("SERVER");
		}
		return clientForwarder;
	}

}
