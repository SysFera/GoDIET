package com.sysfera.godiet.core.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Forwarder;
import com.sysfera.godiet.common.model.generated.OmniNames;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Ssh;
import com.sysfera.godiet.core.managers.topology.domain.DomainTopologyManager;
import com.sysfera.godiet.core.managers.topology.domain.DomainTopologyManagerGSImpl;
import com.sysfera.godiet.core.managers.topology.domain.Path;
import com.sysfera.godiet.core.model.softwares.DietResourceManaged;
import com.sysfera.godiet.core.model.softwares.OmniNamesManaged;

/**
 * 
 * @author phi
 * 
 */
@Component
public class DomainManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	private final Set<Domain> domains; // store the domains
	private final Set<OmniNames> omninames;

	private final DomainTopologyManager domainTopology;

	public DomainManager() {
		domains = new HashSet<Domain>();
		omninames = new HashSet<OmniNames>();
		domainTopology = new DomainTopologyManagerGSImpl();
	}

	// return all the domains
	public Set<Domain> getDomains() {
		return domains;
	}

	// return the OmniNames
	public Set<OmniNames> getOmniNames() {
		return omninames;
	}

	public void addDomains(List<Domain> domains) throws GraphDataException {
		if (domains == null) {
			log.warn("Try to add empty list of domain");
			return;
		}
		for (Domain domain : domains) {
			domainTopology.addDomain(domain);
		}
		this.domains.addAll(domains);
	}

	public void addForwarders(DietResourceManaged<Forwarder> client,
			DietResourceManaged<Forwarder> server) throws GraphDataException {
		Set<Domain> domainsClient = getDomains(client.getPluggedOn());
		Set<Domain> domainsServer = getDomains(server.getPluggedOn());

		for (Domain domainClient : domainsClient) {
//			System.out.println("\t- " + domainClient.getId());
			for (Domain domainServer : domainsServer) {
//				System.out.println("\t\t" + domainServer.getId());
				if (domainClient == domainServer) {
//					System.out.println("\t\t\tBREAK");
					// throw new
					// GraphDataException("domainClient and domainSource are identical");
					continue;
				}
				domainTopology.addForwarder(domainClient, domainServer);
			}
		}
	}


	public Path findPath(Domain from, Domain to) throws PathException {
		return domainTopology.findPath(from, to);
	}


	public DomainTopologyManager getDomainTopologyManager() {
		return domainTopology;
	}

	public void addOmniNames(OmniNamesManaged omniName) {
		List<Ssh> sshs = omniName.getPluggedOn().getSsh();
		for (Ssh ssh : sshs) {
			ssh.getDomain().setOmniNames(omniName.getSoftwareDescription());
		}
	}

	// get the domain affected to a Resource (Node?)
	public Set<Domain> getDomains(Resource resource) {
		Set<Domain> domains = new HashSet<Domain>();
		for (Ssh ssh : resource.getSsh()) {
			domains.add(ssh.getDomain());
		}
		return domains;
	}

	public Domain getDomains(String domainId) {
		for (Domain domain : domains) {
			if (domain.getId().equals(domainId)) {
				return domain;
			}
		}
		return null;
	}

	public boolean isConnected(Domain incomingDomain, Domain domain) {
		// TODO Auto-generated method stub
		return false;
	}
	// /**
	// * Search if a domain domainSource is connected with domainDest (i.e
	// * exist forwarders between this two domains)
	// *
	// * @param domainSource
	// * @param domainDest
	// * @return
	// */
	// public boolean isConnected(Domain domainSource, Domain domainDest) {
	//
	// // check if forwarder already exists between all domains source and
	// // domainDest.
	//
	// EdgeDomain edge = this.graph.new EdgeDomain();
	// edge.source = domainSource;
	// edge.destination = domainDest;
	// if (graph.edges.contains(edge)) {
	// return true;
	//
	// }
	//
	// return false;
	// }

}
