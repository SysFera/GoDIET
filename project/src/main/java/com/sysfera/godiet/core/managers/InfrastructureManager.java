package com.sysfera.godiet.core.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Cluster;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Fronted;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Ssh;
import com.sysfera.godiet.core.managers.topology.TopologyManager;
import com.sysfera.godiet.core.managers.topology.TopologyManagerGSImpl;
import com.sysfera.godiet.core.model.Path;

/**
 * Physical infrastructure description TODO: check the unique id resource (like
 * dietReousceid in DietManager)
 * 
 * @author phi
 * 
 */
@Component
public class InfrastructureManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	// Reference all nodes,gateways, fronted by is id.
	private final Map<String, Node> resources;
	// voir dossier bureau GoDiet pour old version de ce fichier java.
	// All nodes platform (even cluster node)
	private final List<Node> nodes;
	private final List<Cluster> clusters;
	private final List<Fronted> fronteds;
	private final List<Link> links;

	private final TopologyManager topologyManager;
	@Autowired
	private DomainsManager domainManager;

	public InfrastructureManager() {
		this.topologyManager = new TopologyManagerGSImpl();
		this.nodes = new ArrayList<Node>();
		this.clusters = new ArrayList<Cluster>();
		this.fronteds = new ArrayList<Fronted>();
		this.links = new ArrayList<Link>();
		this.resources = new HashMap<String, Node>();
	}

	/**
	 * 
	 * @return the list nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @return the clusters
	 */
	public List<Cluster> getClusters() {
		return clusters;
	}

	/**
	 * @return the frontends
	 */
	public List<Fronted> getFrontends() {
		return fronteds;
	}

	/**
	 * @return the links
	 */
	public List<Link> getLinks() {
		return links;
	}

	/**
	 * @return the domains
	 */
	public Collection<Domain> getDomains() {
		return domainManager.getDomains();
	}

	public void addLinks(List<Link> links) throws GraphDataException {
		for (Link link : links) {
			topologyManager.addLink(link);
		}
		this.links.addAll(links);
	}

	public void addNodes(List<Node> computingNodes) throws GraphDataException {
		if (computingNodes == null) {
			log.warn("Try to add empty list of nodes");
			return;
		}
		for (Node node : computingNodes) {
			resources.put(node.getId(), node);

			topologyManager.addNode(node);
		}
		this.nodes.addAll(computingNodes);

	}

	public void addClusters(List<Cluster> clusters) throws ResourceAddException {
		this.clusters.addAll(clusters);

	}

	public void addDomains(List<Domain> domains) throws GraphDataException {
		if (domains == null) {
			log.warn("Try to add empty list of domains");
			return;
		}
		for (Domain domain : domains) {
			topologyManager.addDomain(domain);
		}

		this.domainManager.addAll(domains);

	}

	/**
	 * Find a path between from resource and to resource.
	 * 
	 * @param from
	 *            Start node
	 * @param to
	 *            End node
	 * @return The Path or null if no path exist
	 * @throws PathException
	 *             If one of argument are null.
	 */
	public Path findPath(Resource from, Resource to) throws PathException {
		if (from == null || to == null) {
			throw new PathException("Try to find path between null argument");
		} else {
			return topologyManager.findPath(from, to);
		}
	}

	public TopologyManager getTopologyManager() {
		return topologyManager;
	}

	/**
	 * TODO: Change name to getNode
	 * 
	 * @param resourceId
	 * @return Resource given is id
	 */
	public Node getResource(String resourceId) {
		return resources.get(resourceId);
	}

	/**
	 * Return all the domains where resource is include
	 * 
	 * @param resource
	 * @return
	 */
	public List<Domain> getDomains(Resource resource) {
		List<Domain> domainsCovered = new ArrayList<Domain>();
		for (Ssh ssh : resource.getSsh()) {
			domainsCovered.add(ssh.getDomain());
		}

		return domainsCovered;
	}

	public Domain getDomains(String domain) {
		return domainManager.getDomains(domain);
	}

	/**
	 * Return the Ssh of resource corresponding to the given domain
	 * 
	 * @param resource
	 *            the resource looked.
	 * @param domain
	 *            the domain
	 * @return the Ssh or null if doesn't exist
	 */
	public Ssh getSsh(Resource resource, Domain domain) {
		List<Ssh> sshs = resource.getSsh();
		for (Ssh ssh : sshs) {
			if (ssh.getDomain().equals(domain)) {
				return ssh;
			}
		}
		return null;
	}

	// public Domain getDomain(String server) {
	// Resource res = resources.get(server);
	// if(res == null)
	// {
	// log.error("Unable to find physical resource with name: "+server);
	// return null;
	// }
	// return res.getDomain();
	// }
}
