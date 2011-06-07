package com.sysfera.godiet.managers.topology;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

public class TopologyManagerGSImpl implements TopologyManager {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	private final Graph gs;

	// Constructor
	public TopologyManagerGSImpl() {
		this.gs = new SingleGraph("Construction Infrastructure");
	}

	public Graph getGraph() {
		return gs;
	}

	@Override
	public void addLink(Link link) throws GraphDataException {
		String nodeFrom = link.getFrom().getId();
		String nodeTo = link.getTo().getId();
		String idLink = nodeFrom + "_" + nodeTo;
		Edge edge = gs.addEdge(idLink, nodeFrom, nodeTo, true); // oriented edge
		edge.addAttribute("ui.class", "nodeToNode"); // the link node > node
		edge.addAttribute("ssh", link.getAccessref()); // Storage of the ssh in
														// the edge
	}

	@Override
	public void addNode(Node n) throws GraphDataException {
		String idNode = n.getId();
		org.graphstream.graph.Node node = gs.addNode(idNode); // adding
		node.addAttribute("ui.label", idNode);
		node.addAttribute("ui.class", "noeud"); // for the management of the
												// node's display
		node.addAttribute("id", idNode); // Storage of the id of the node
		node.addAttribute("node", n); // Storage of the node under the id "node"
		// Add the link node > domain
		List<Ssh> sshs = n.getSsh();
		if (sshs != null) {
			for (Ssh ssh : sshs) {
				String idDomain = ssh.getDomain().getId(); // Storage of the id
															// of our domain
				String idLink = idNode + "_" + idDomain;
				Edge edge = gs.addEdge(idLink, idNode, idDomain);
				edge.addAttribute("ui.class", "nodeToDomain");
				edge.addAttribute("id", idLink); // Storage of the id of the
													// link node > domain
				edge.addAttribute("ssh", ssh); // Storage of the ssh in the edge
			}
		}
	}

	@Override
	public void addDomain(Domain d) throws GraphDataException {
		String idDomain = d.getId();
		org.graphstream.graph.Node node = gs.addNode(idDomain); // adding
		node.addAttribute("ui.label", idDomain);
		node.addAttribute("ui.class", "domain"); // for the management of the
													// domain's display
		node.addAttribute("id", idDomain); // Storage of the id of the domain
		// node.addAttribute("domain", d);
	}

	@Override
	public Path findPath(Resource f, Resource t) {

		String from = f.getId();
		String to = t.getId();

		org.graphstream.graph.Path p = shortestPath(from, to);
		return conversion(p);
	}

	public org.graphstream.graph.Path shortestPath(String from, String to) {

		// Calculation of the shortpaths
		APSP apsp = new APSP();
		apsp.init(gs); // registering apsp as a sink for the graph
		apsp.setDirected(true); // directed grap
		apsp.setWeightAttributeName("weight");
		apsp.compute(); // the method that actually computes shortest paths
		APSPInfo info = gs.getNode(from).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		// Prints
		log.debug("Shortest path to " + from + " to " + to + " : "
				+ info.getShortestPathTo(to));
		log.debug("Path's lenght : " + info.getLengthTo(to) + " / ");
		org.graphstream.graph.Path p = info.getShortestPathTo(to);
		log.debug("Number of nodes in the path : " + p.getNodeCount()+ " \n ");
		return p;
	}

	// TODO : private class
	public Path conversion(org.graphstream.graph.Path p) {

		Path path = new Path();
		LinkedHashSet<Hop> set = new LinkedHashSet<Hop>();

		List<Edge> le = p.getEdgePath();
		Iterator<? extends Edge> k = le.iterator();

		Edge e;
		Ssh ssh;
		Node node;

		while (k.hasNext()) { // Browse the edge(s) of the shortest path
			e = k.next();
			Path.Hop hop = (path).new Hop();

			// Selection of the edge
			org.graphstream.graph.Node nGS = e.getNode1();
			if (nGS.getAttribute("node") != null) { // if the node is a machine
													// and NOT a domain then we
													// add it
				node = nGS.getAttribute("node");
				hop.setDestination(node); // Add the node at the end of the edge
				ssh = e.getAttribute("ssh");
				hop.setLink(ssh); // Add the ssh
				set.add(hop);
				
				// Display of the selected path
				nGS.addAttribute("ui.class", "noeud, pathSelected");
				String s = e.getAttribute("ui.class");
				String[] ts = s.split(",");
				if (ts[0] == "noeud") {
					e.addAttribute("ui.class", ts[0] + ", pathSelected");
				} else {
					e.addAttribute("ui.class", ts[0] + ", pathSelected");
				}
			}
		}
		// print
		// System.out.println("\t --> " + le.size() + " edges.\n");
		// Iterator<Hop> i = set.iterator();
		// while(i.hasNext()) {
		// Hop h = i.next();
		// System.out.println(h);
		// }
		path.setPath(set);
		return path;
	}

}
