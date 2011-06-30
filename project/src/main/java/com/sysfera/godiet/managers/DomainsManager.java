package com.sysfera.godiet.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.managers.DomainsManager.DomainGraph.EdgeDomain;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.OmniNamesManaged;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * 
 * 
 * 
 * @author phi
 * 
 */
@Component
public class DomainsManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	Set<Domain> domains;
	private final DomainGraph graph;

	public DomainsManager() {
		this.domains = new HashSet<Domain>();
		this.graph = new DomainGraph();
	}

	public void addAll(List<Domain> domainsList) {
		this.domains.addAll(domainsList);
	}

	public void addOmniNames(OmniNamesManaged omniName) {
		List<Ssh> sshs = omniName.getPluggedOn().getSsh();
		for (Ssh ssh : sshs) {
			ssh.getDomain().setOmniNames(omniName.getSoftwareDescription());
		}
	}

	/**
	 * Add edges on domain graph. One edge from each domain client resource to
	 * each domain server server
	 * 
	 * @param client
	 * @param server
	 */
	public void addForwarders(DietResourceManaged<Forwarder> client,
			DietResourceManaged<Forwarder> server) {
		Set<Domain> domainsClient = getDomains(client.getPluggedOn());
		Set<Domain> domainsServer = getDomains(server.getPluggedOn());
		for (Domain domainServer : domainsServer) {
			for (Domain domainClient : domainsClient) {
				if(domainClient == domainServer) break;
				
				EdgeDomain edge1 = this.graph.new EdgeDomain();
				edge1.source = domainClient;
				edge1.destination = domainServer;
				edge1.client = client;
				edge1.server = server;
				
				graph.edges.add(edge1);

				EdgeDomain edge2 = this.graph.new EdgeDomain();
				edge2.source = domainServer;
				edge2.destination = domainClient;
				edge2.client = client;
				edge2.server = server;
				graph.edges.add(edge2);
				log.debug("Add edge between " + domainClient + " and " +domainServer );
			}
		}
	}

	public Set<Domain> getDomains() {
		return domains;
	}

	public class DomainGraph {
		private Set<EdgeDomain> edges = new HashSet<DomainsManager.DomainGraph.EdgeDomain>();

		public class EdgeDomain {
			private DietResourceManaged<Forwarder> client;
			private DietResourceManaged<Forwarder> server;
			private Domain source;
			private Domain destination;

			public DietResourceManaged<Forwarder> getClient() {
				return client;
			}

			public DietResourceManaged<Forwarder> getServer() {
				return server;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime
						* result
						+ ((destination == null) ? 0 : destination.getId()
								.hashCode());
				result = prime * result
						+ ((source == null) ? 0 : source.getId().hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				EdgeDomain other = (EdgeDomain) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (destination == null) {
					if (other.destination != null)
						return false;
				} else if (!destination.getId().equals(
						other.destination.getId()))
					return false;
				if (source == null) {
					if (other.source != null)
						return false;
				} else if (!source.getId().equals(other.source.getId()))
					return false;
				return true;
			}

			private DomainGraph getOuterType() {
				return DomainGraph.this;
			}
		}
	}

	/**
	 * Search if a domain of currentSource is connected with domainDest (i.e
	 * exist forwarders between this two domains)
	 * 
	 * @param currentSource
	 * @param domainDest
	 * @return
	 */
	public boolean isConnected(Domain domainSource, Domain domainDest) {

		// check if forwarder already exists between all domains source and
		// domainDest.

		EdgeDomain edge = this.graph.new EdgeDomain();
		edge.source = domainSource;
		edge.destination = domainDest;
		if (graph.edges.contains(edge)) {
			return true;

		}

		return false;
	}

	public Set<Domain> getDomains(Resource resource) {
		Set<Domain> domains = new HashSet<Domain>();
		for (Ssh ssh : resource.getSsh()) {
			domains.add(ssh.getDomain());
		}

		return domains;

	}

	public Domain getDomains(String domainId) {
		for (Domain domain : domains) {
			if(domain.getId().equals(domainId)){
				return domain;
			}
		}
		return null;
	}
}
