package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.graph.PathException;
import com.sysfera.godiet.managers.topology.TopologyManager;
import com.sysfera.godiet.managers.topology.TopologyManagerNeo4jImpl;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Fronted;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;

/**
 * Physical infrastructure description
 * 
 * @author phi
 * 
 */
public class Platform {
	private Logger log = LoggerFactory.getLogger(getClass());

	// Reference all nodes,gateways, fronted by is id.
	private final Map<String, Resource> resources;
	// All nodes platform (even cluster node)
	private final List<Node> nodes;
	private final List<Cluster> clusters;
	private final List<Gateway> gateways;
	private final List<Fronted> fronteds;
	private final List<Link> links;
	private final List<Domain> domains;
	private final TopologyManager topologyManager;

	public Platform() {
		this.topologyManager = new TopologyManagerNeo4jImpl(this);
		this.domains = new ArrayList<Domain>();
		this.nodes = new ArrayList<Node>();
		this.clusters = new ArrayList<Cluster>();
		this.gateways = new ArrayList<Gateway>();
		this.fronteds = new ArrayList<Fronted>();
		this.links = new ArrayList<Link>();
		this.resources = new HashMap<String, Resource>();
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
	 * @return the gateways
	 */
	public List<Gateway> getGateways() {
		return gateways;
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
	public List<Domain> getDomains() {
		return domains;
	}

	public void addLinks(List<Link> links) throws GraphDataException {
		for (Link link : links) {
			topologyManager.addLink(link.getFrom(), link.getTo());
		}
		this.links.addAll(links);
	}

	public void addFrontends(List<Fronted> frontends) {
		if (fronteds == null) {
			log.warn("Try to add empty list of fronteds");
			return;
		}
		for (Fronted fronted : frontends) {
			resources.put(fronted.getId(), fronted);
		}
		this.fronteds.addAll(frontends);

	}

	public void addNodes(List<Node> computingNodes) {
		if (computingNodes == null) {
			log.warn("Try to add empty list of nodes");
			return;
		}
		for (Node node : computingNodes) {
			resources.put(node.getId(), node);
		}
		this.nodes.addAll(computingNodes);

	}

	public void addClusters(List<Cluster> clusters2) {
		this.clusters.addAll(clusters);

	}

	public void addGateways(List<Gateway> gateways) {
		if (gateways == null) {
			log.warn("Try to add empty list of gateways");
			return;
		}
		for (Gateway gateway : gateways) {
			this.resources.put(gateway.getId(), gateway);
		}
		this.gateways.addAll(gateways);

	}

	public void addDomains(List<Domain> domains) {
		this.domains.addAll(domains);

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
		if (from == null || to == null)
			throw new PathException("Try to find path between null argument");
		return topologyManager.findPath(from, to);
	}

	// TODO : Path findpath(FromDomain, ToNode); Le lancement de la config se
	// fait depuis un domain. Pas nécessairement depuis un noeud existant dans
	// la description

	/**
	 * 
	 * @param resourceId
	 * @return Resource given is id
	 */
	public Resource getResource(String resourceId) {
		return resources.get(resourceId);
	}
}
