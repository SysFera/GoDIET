package com.sysfera.godiet.core.managers.topology.infrastructure;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.LayoutPolicy;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Ssh;

/**
 * Display of the infrastructure into a graph
 * 
 * @author Nicolas Quattropani
 * 
 */
public class TopologyManagerGSImpl implements TopologyManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final Graph gs;

	// GS Shortest path calculator
	private final APSP apsp;
	
	// used to save snapshot of the graph
	private FileSinkImages fsi;
		
	/**
	 * Constructor
	 */
	public TopologyManagerGSImpl() {		
		this.gs = new MultiGraph("Infrastructure");		
		
		// features of the fsi object
		fsi = new FileSinkImages(OutputType.jpg, Resolutions.WXGA_8by5); // resolution
		fsi.setLayoutPolicy(LayoutPolicy.COMPUTED_AT_NEW_IMAGE);
		gs.addSink(fsi);
		
		this.apsp = new APSP();
		apsp.setDirected(true); // directed graph
		apsp.setWeightAttributeName("weight");
	}

	/**
	 * Get the graph on which we are working
	 * 
	 * @return graph
	 */
	public Graph getGraph() {
		return gs;
	}
	
	/**
	 * Get the FileSinkImages who is connected to the graph
	 * @return FileSinkImages
	 */
	public FileSinkImages getFileSinkImages(){
		return fsi;
	}

	@Override
	public void addLink(Link link) throws GraphDataException {

		try {
			String nodeFrom;
			// Whether the from node is a node or a domain
			String s = "";
			if (link.getFrom() != null) {
				nodeFrom = link.getFrom().getId();
				s = "nodeToNode";
			} else {
				nodeFrom = link.getFromDomain().getId();
				s = "domainToNode";
			}
			String nodeTo = link.getTo().getId();
			String idLink = nodeFrom + "_" + nodeTo;
			// oriented edge
			Edge edge = gs.addEdge(idLink, nodeFrom, nodeTo, true);
			// for the management of the edge's display
			edge.addAttribute("ui.class", "edge");
			// storage of the ssh in the edge
			edge.addAttribute("ssh", link.getAccessref());
			edge.addAttribute("edgeType", s);
		} catch (Exception e) {
			throw new GraphDataException(e.getMessage());
		}
	}

	@Override
	public void addNode(Node n) throws GraphDataException {
		try {
			String idNode = n.getId();
			org.graphstream.graph.Node node = gs.addNode(idNode); // adding
			node.addAttribute("ui.label", idNode);
			// for the management of the node's display
			node.addAttribute("ui.class", "noeud");
			// Storage of the node under the id "node"
			node.addAttribute("node", n);

			// Add the link node > domain
			List<Ssh> sshs = n.getSsh();
			if (sshs != null) {
				for (Ssh ssh : sshs) { // browse the domains of the node
					String idDomain = ssh.getDomain().getId();
					String idLink1 = idNode + "_" + idDomain;
					String idLink2 = idDomain + "_" + idNode;
					Edge edge1 = gs.addEdge(idLink1, idNode, idDomain, true);
					Edge edge2 = gs.addEdge(idLink2, idDomain, idNode, true);
					// Storage of the ssh in the edge
					edge1.addAttribute("ssh", ssh);
					edge2.addAttribute("ssh", ssh);
					edge1.addAttribute("ui.class", "edge");
					edge2.addAttribute("ui.class", "edge");
					edge1.addAttribute("edgeType", "nodeToDomain");
					edge2.addAttribute("edgeType", "nodeToDomain");
				}
			}
		} catch (Exception e) {
			throw new GraphDataException(e.getMessage());
		}
	}

	@Override
	public void addDomain(Domain d) throws GraphDataException {
		try {
			String idDomain = d.getId();
			org.graphstream.graph.Node node = gs.addNode(idDomain); // adding
			node.addAttribute("ui.label", idDomain);
			// for the management of the domain's display
			node.addAttribute("ui.class", "domain");
		} catch (Exception e) {
			throw new GraphDataException(e.getMessage());
		}
	}

	@Override
	public Path findPath(Resource source, Resource destination)
			throws PathException {

		// exception cases
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

		return conversion(p); // conversion of the found path
	}

	/**
	 * find the shortest path between 2 nodes
	 * 
	 * @param from
	 *            id of the source node.
	 * @param to
	 *            id of the destination node.
	 * @throws PathException
	 *             if there is a problem with the found path.
	 */
	public org.graphstream.graph.Path shortestPath(String from, String to)
			throws PathException {

		apsp.init(gs); // registering APSP as a sink for the graph
		apsp.compute(); // the method that actually computes shortest paths
		APSPInfo info = gs.getNode(from).getAttribute(APSPInfo.ATTRIBUTE_NAME);

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

	// TODO : private class?
	/**
	 * Convert a GraphStream shortest path into a path usable by the data model
	 * (retains only part of the original path)
	 * 
	 * @param p
	 *            shortest path previously found.
	 * @return the converted path
	 * @throws PathException
	 *             if there is a problem with the found path.
	 */
	public Path conversion(org.graphstream.graph.Path p) throws PathException {

		Path path = new Path();
		LinkedHashSet<Path.Hop> set = new LinkedHashSet<Path.Hop>();
		Iterator<Edge> ie = p.getEdgePath().iterator();

		Edge e = null;
		Ssh ssh = null;
		Node node = null;
		Path.Hop hop;

		while (ie.hasNext()) { // Browse the edge(s) of the shortest path

			e = ie.next();
			// Selection of the targeted node of each edge of the path
			org.graphstream.graph.Node nGS = e.getNode1();

			// if the node is a machine and NOT a domain then we add it
			if (nGS.hasAttribute("node")) {
				hop = (path).new Hop();
				node = nGS.getAttribute("node");
				hop.setDestination(node); // Add the edge's ending node
				ssh = e.getAttribute("ssh");
				hop.setLink(ssh); // Add the ssh

				log.debug("Ssh / Node add to the real Path : " + ssh.getId()
						+ " / " +  node.getId());
				
				set.add(hop);

				// Display of the selected path
				String ncss = nGS.getAttribute("ui.class");
				String ecss = e.getAttribute("ui.class");
				String[] tn = ncss.split(",");
				String[] te = ecss.split(",");
				nGS.addAttribute("ui.class", tn[0] + ", pathSelected");
				e.addAttribute("ui.class", te[0] + ", pathSelected");
			}
		}
		path.setPath(set);
		return path;
	}
}
