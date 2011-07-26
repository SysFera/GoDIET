package com.sysfera.godiet.core.managers.topology.domain;

import java.util.ArrayList;
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
import com.sysfera.godiet.core.managers.topology.domain.Path.Hop;

/**
 * Display of the infrastructure's domains and the associated forwarders into a graph
 * 
 * @author Nicolas Quattropani
 * 
 */
public class DomainTopologyManagerGSImpl implements DomainTopologyManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private static final String KEYWEIGHT = "weight";
	private final Graph gs;

	// list of the edges already to the graph
	private final List<String> addedEdges = new ArrayList<String>();
	// GS Shortest path calculator
	private final APSP apsp;
	// used to save snapshot of the graph
	private FileSinkImages fsi;

	/**
	 * Constructor
	 */
	public DomainTopologyManagerGSImpl() {
		this.gs = new MultiGraph("DomainTopology");

		// features of the fsi object
		fsi = new FileSinkImages(OutputType.jpg, Resolutions.WXGA_8by5); // resolution
		fsi.setLayoutPolicy(LayoutPolicy.COMPUTED_AT_NEW_IMAGE);	
		gs.addSink(fsi);

		this.apsp = new APSP();
		apsp.setDirected(false); // undirected graph
		apsp.setWeightAttributeName(KEYWEIGHT);
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
	 * 
	 * @return FileSinkImages
	 */
	public FileSinkImages getFileSinkImages() {
		return fsi;
	}

	@Override
	public void addDomain(Domain d) throws GraphDataException {
		try {
			String idDomain = d.getId();
			org.graphstream.graph.Node node = gs.addNode(idDomain); // adding
			node.addAttribute("ui.label", idDomain);
		} catch (Exception e) {
			throw new GraphDataException(e.getMessage());
		}
	}

	@Override
	public void addForwarder(Domain client, Domain server)
			throws GraphDataException {
		try {
			String idC = client.getId();
			String idS = server.getId();
			String idLink = idC + "_" + idS;
			// to avoid the double edge between 2 nodes
			String idLinkReversed = idS + "_" + idC;

			// if the edge is already added
			if (!addedEdges.contains(idLink)
					&& !addedEdges.contains(idLinkReversed)) {
				Edge addedEdge = gs.addEdge(idLink, idC, idS);
				addedEdge.addAttribute(KEYWEIGHT, "1");
				addedEdges.add(idLink);
			}
//				System.out.println("\t\t\tadded Edge : " + addedEdge);
//				System.out.println("\t\t\t" + addedEdges.size());
//			} else {
//				System.out.println("\t\t\tfound");
//			}
		} catch (Exception e) {
			throw new GraphDataException(e.getMessage());
		}
	}

	@Override
	public Path findPath(Domain source, Domain destination)
			throws PathException {

		// exception cases
		if (source == null || destination == null) {
			if (source != null && destination == null) {
				throw new PathException("Destination domain is null");
			} else if (source == null && destination != null) {
				throw new PathException("Source domain is null");
			} else if (source == destination) {
				throw new PathException(
						"Source and destination domains are null");
			}
		} else if (source == destination) {
			throw new PathException(
					"The source and the destination domains are equals");
		}
		String from = source.getId();
		String to = destination.getId();
		org.graphstream.graph.Path p = shortestPath(from, to);

		return conversion(p); // conversion of the found path
	}

	/**
	 * find the shortest path between 2 domains
	 * 
	 * @param from
	 *            id of the source domain.
	 * @param to
	 *            id of the destination domain.
	 * @throws PathException
	 *             if there is a problem with the found path.
	 */
	public org.graphstream.graph.Path shortestPath(String from, String to)
			throws PathException {
		apsp.init(gs); // registering apsp as a sink for the graph
		apsp.compute(); // the method that actually computes shortest paths
		APSPInfo info = gs.getNode(from).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		org.graphstream.graph.Path p = info.getShortestPathTo(to);
		if (p == null) {
			// TODO : lancement algo ajout forwarder si path unknown
			throw new PathException("Path not found");
		} else {
			log.debug("Shortest path to " + from + " to " + to + " : "
					+ p.toString());
			log.debug("Path's lenght : " + p.size()
					+ " / Number of domains in the path : " + p.getNodeCount());
			return p;
		}
	}

	/**
	 * Convert a GraphStream shortest path into a path usable by the data model
	 * 
	 * @param p
	 *            shortest path previously found.
	 * @return the converted path
	 * @throws PathException
	 *             if there is a problem with the found path.
	 */
	public Path conversion(org.graphstream.graph.Path p) throws PathException {

		Path path = new Path();
		LinkedHashSet<Hop> set = new LinkedHashSet<Path.Hop>();
		Iterator<Edge> ie = p.getEdgePath().iterator();

		Edge e;
		Path.Hop hop;
		while (ie.hasNext()) { // Browse the edge(s) of the shortest path

			hop = (path).new Hop();

			e = ie.next();
			// add the id of the targeted node of each edge of the path
			hop.setIdDomain(e.getNode1().getId());
			// add the id of the forwarder between 2 nodes
			hop.setIdForwarder(e.getId());

			log.debug("Forwarder / Domain add to the real Path : " + e + " / "
					+ e.getNode1().getId());

			set.add(hop);
		}
		for (Hop h : set) {
			log.debug(""+h);
		}
		path.setPath(set);
		return path;
	}
}