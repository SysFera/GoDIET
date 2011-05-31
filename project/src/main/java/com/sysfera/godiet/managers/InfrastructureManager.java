package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.topology.TopologyManager;
import com.sysfera.godiet.managers.topology.TopologyManagerGSImpl;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Fronted;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;

/**
 * Physical infrastructure description
 * TODO: check the unique id resource (like dietReousceid in DietManager)
 * @author phi
 * 
 */
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
	private final List<Domain> domains;
	//FIXME:
	private final TopologyManager topologyManager;

	public InfrastructureManager() {
	//	this.topologyManager = new TopologyManagerNeo4jImpl(this);
		this.topologyManager = new TopologyManagerGSImpl();
		this.domains = new ArrayList<Domain>();
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
	public List<Domain> getDomains() {
		return domains;
	}

	public void addLinks(List<Link> links) throws GraphDataException {
		if (links == null) {
			log.warn("Try to add empty list of links");
			return;
		}
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

	public void addDomains(List<Domain> domains) throws GraphDataException {
		if (domains == null) {
			log.warn("Try to add empty list of domains");
			return;
		}
		for (Domain domain : domains) {
			topologyManager.addDomain(domain);
		}
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
		if (from == null || to == null) {
			throw new PathException("Try to find path between null argument");
                }
	//	return topologyManager.findPath(from, to);
		throw new IllegalAccessError("Topology Manager not yet implemented");
		//return null;
	}

	public TopologyManager getTopologyManager() {
		return topologyManager;
	}
	/**
	 * TODO: Change name to getNode
	 * @param resourceId
	 * @return Resource given is id
	 */
	public Node getResource(String resourceId) {
		return resources.get(resourceId);
	}
}
