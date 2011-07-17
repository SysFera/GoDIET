package com.sysfera.godiet.managers.topology;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.MultiGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * 
 * @author Nicolas Quattropani
 * 
 */
public class TopologyManagerGSImpl implements TopologyManager {

	private final static String ATTRIBUTEKEY_DOMAIN = "domain";

	private Logger log = LoggerFactory.getLogger(getClass());
	private final Graph gs;
	// GS Shortest path calculator
	private final APSP apsp;

	// Constructor
	public TopologyManagerGSImpl() {
		this.gs = new MultiGraph("Construction Infrastructure");

		this.apsp = new APSP();
		apsp.setDirected(true); // directed grap
		apsp.setWeightAttributeName("weight");
	}

	public Graph getGraph() {
		return gs;
	}

	@Override
	public void addLink(Link link) throws GraphDataException {
		try {
			String nodeFrom;
			// Whether the from node is a node or a domain
			if (link.getFrom() != null) {
				nodeFrom = link.getFrom().getId();
			} else {
				nodeFrom = link.getFromDomain().getId();
			}
			String nodeTo = link.getTo().getId();
			String idLink = nodeFrom + "_" + nodeTo;
			Edge edge = gs.addEdge(idLink, nodeFrom, nodeTo, true); // oriented
																	// edge
			edge.addAttribute("ui.class", "nodeToNode"); // link node > node
			edge.addAttribute("ssh", link.getAccessref()); // Storage of the ssh
															// in
															// the edge
		} catch (IdAlreadyInUseException e) {
			throw new GraphDataException("Link between "
					+ link.getFrom().getId() + " and " + link.getTo().getId()
					+ "already exist", e);
		}
	}

	@Override
	public void addNode(Node n) throws GraphDataException {
		String idNode = n.getId();
		try {
			org.graphstream.graph.Node node = gs.addNode(idNode); // adding
			node.addAttribute("ui.label", idNode);
			node.addAttribute("ui.class", "noeud"); // for the management of the
													// node's display
			node.addAttribute("id", idNode); // Storage of the id of the node
			node.addAttribute("node", n); // Storage of the node under the id
											// "node"
			// Add the link node > domain
			List<Ssh> sshs = n.getSsh();
			if (sshs != null) {
				for (Ssh ssh : sshs) {
					String idDomain = ssh.getDomain().getId();
					// Oriented edge
					// TODO : 1 edge unoriented like DOMAIN > NODE instead of 2
					// orientend edge or 1 unoriented NODE > DOMAIN ??
					// maybe that's enough to handle what we need ?
					String idLink1 = idNode + "_" + idDomain;
					String idLink2 = idDomain + "_" + idNode;
					Edge edge1 = gs.addEdge(idLink1, idNode, idDomain, true);
					Edge edge2 = gs.addEdge(idLink2, idDomain, idNode, true);
					// Storage of the id of the link node > domain
					edge1.addAttribute("id", idLink1);
					edge2.addAttribute("id", idLink2);
					// Storage of the ssh in the edge
					edge1.addAttribute("ssh", ssh);
					edge2.addAttribute("ssh", ssh);
					edge1.addAttribute("ui.class", "nodeToDomain");
					edge2.addAttribute("ui.class", "nodeToDomain");
				}
			}
		} catch (IdAlreadyInUseException e) {
			throw new GraphDataException("Node " + idNode + " already exist.je con");
		}
	}

	@Override
	public void addDomain(Domain d) throws GraphDataException {
		try {
			String idDomain = d.getId();
			org.graphstream.graph.Node node = gs.addNode(idDomain); // adding
			node.addAttribute("ui.label", idDomain);
			node.addAttribute("ui.class", "domain"); // for the management of
														// the
														// domain's display
			node.addAttribute("id", idDomain); // Storage of the id of the
												// domain
			node.addAttribute(ATTRIBUTEKEY_DOMAIN, d);
		} catch (IdAlreadyInUseException e) {
			throw new GraphDataException("Domain " + d.getId()
					+ " already exist", e);
		}
	}

	@Override
	public Path findPath(Resource source, Resource destination)
			throws PathException {

		// TODO : Error message for the cases :
		// dest = source = null et dest = null & source = null !
		if (source == null || destination == null) {
			if (source != null && destination == null) {
				throw new PathException("Destination is null");
			} else if (source == null && destination != null) {
				throw new PathException("Source is null");
			} else if (source == destination) {
				throw new PathException("Source and destination are null");
			}
		}
		String from = source.getId();
		String to = destination.getId();
		org.graphstream.graph.Path p = shortestPath(from, to);

		return conversion(p);
	}

	//
	public org.graphstream.graph.Path shortestPath(String from, String to)
			throws PathException {

		apsp.init(gs); // registering apsp as a sink for the graph
		apsp.compute(); // the method that actually computes shortest paths
		APSPInfo info = gs.getNode(from).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		// Prints
		org.graphstream.graph.Path p = info.getShortestPathTo(to);
		if (p == null) {
			throw new PathException("Path not found");
		} else {
			log.debug("Shortest path to " + from + " to " + to + " : "
					+ p.toString());
			log.debug("Path's lenght : " + p.size()
					+ " / Number of nodes in the path : " + p.getNodeCount());
			return p;
		}
	}

	// TODO : private class
	public Path conversion(org.graphstream.graph.Path p) throws PathException {

		Path path = new Path();
		LinkedHashSet<Hop> set = new LinkedHashSet<Hop>();

		Iterator<Edge> ie = p.getEdgePath().iterator();

		Edge e;
		Ssh ssh;
		Node node;
		Domain crossedDomain = null;
		while (ie.hasNext()) { // Browse the edge(s) of the shortest path
			e = ie.next();
			Path.Hop hop = (path).new Hop();

			// Selection of the edge
			org.graphstream.graph.Node nGS = e.getNode1();
			// if the node is a machine and NOT a domain then we add it
			if (nGS.getAttribute("node") != null) {
				node = nGS.getAttribute("node");
				hop.setDestination(node); // Add the node at the end of the
											// edge
				ssh = e.getAttribute("ssh");
				hop.setLink(ssh); // Add the ssh
				log.debug("Node / ssh add to the real Path : " + node.getId()
						+ " / " + ssh.getId());
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
				hop.crossDomain(crossedDomain);
				crossedDomain = null;

			} else if (nGS.hasAttribute(ATTRIBUTEKEY_DOMAIN)) {
				crossedDomain = (Domain) nGS.getAttribute(ATTRIBUTEKEY_DOMAIN);

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
