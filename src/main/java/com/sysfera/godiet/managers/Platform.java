package com.sysfera.godiet.managers;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Model.xml.generated.Cluster;
import com.sysfera.godiet.Model.xml.generated.Domain;
import com.sysfera.godiet.Model.xml.generated.Frontend;
import com.sysfera.godiet.Model.xml.generated.Gateway;
import com.sysfera.godiet.Model.xml.generated.Infrastructure;
import com.sysfera.godiet.Model.xml.generated.Link;
import com.sysfera.godiet.Model.xml.generated.Node;

/**
 * Physical infrastructure description
 * 
 * @author phi
 * 
 */
public class Platform {

	// All nodes platform (even cluster node)
	private final List<Node> nodes;
	private final List<Cluster> clusters;
	private final List<Gateway> gateways;
	private final List<Frontend> frontends;
	private final List<Link> links;
	private final List<Domain> domains;

	public Platform() {

		this.domains = new ArrayList<Domain>();
		this.nodes = new ArrayList<Node>();
		this.clusters = new ArrayList<Cluster>();
		this.gateways = new ArrayList<Gateway>();
		this.frontends = new ArrayList<Frontend>();
		this.links = new ArrayList<Link>();
	}

	void init(Infrastructure infrastructure) {
		List<Domain> domains = infrastructure.getDomain();
		this.domains.addAll(domains);
		if (domains != null) {
			for (Domain domain : domains) {
				this.gateways.addAll(domain.getGateway());
				this.nodes.addAll(domain.getNode());
				List<Cluster> clusters = domain.getCluster();
				this.clusters.addAll(clusters);
				if (clusters != null) {
					for (Cluster cluster : clusters) {
						nodes.addAll(cluster.getComputingNode());
						frontends.addAll(cluster.getFrontend());
					}
				}
			}
		}

		List<Link> links = infrastructure.getLink();
		this.links.addAll(links);
	}

	/**
	 * @return the nodes
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
	public List<Frontend> getFrontends() {
		return frontends;
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
}
