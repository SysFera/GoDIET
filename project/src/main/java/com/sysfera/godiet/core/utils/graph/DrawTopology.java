package com.sysfera.godiet.core.utils.graph;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.OutputPolicy;
import org.graphstream.stream.file.FileSinkImages.Quality;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.common.exceptions.ExportException;
import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.core.managers.topology.domain.DomainTopologyManagerGSImpl;
import com.sysfera.godiet.core.managers.topology.infrastructure.TopologyManagerGSImpl;
//TODO: Bug #34
/**
 * Display of the different graph
 * 
 * @author Nicolas Quattropani
 * 
 */
public class DrawTopology implements Draw {

	private Logger log = LoggerFactory.getLogger(getClass());
	private TopologyManagerGSImpl topologyManager;
	private DomainTopologyManagerGSImpl domainManager;
	boolean domain; // useful to know whether we manipulate domainManager or
					// topologyManager

	private URL stylesheet;
	private Graph gs;
	private FileSinkImages pic;

	/**
	 * Constructor from TopologyManagerGSImpl
	 */
	public DrawTopology(TopologyManagerGSImpl tp) {
		topologyManager = tp;
		gs = topologyManager.getGraph();
		// get back the FileSinkImages that has been initialized previously
		//pic = topologyManager.getFileSinkImages();
		defineFileSinkImages(); // apply others features of the fileSinkImages
		domain = false;
		stylesheet = getClass().getResource("/draw/styleSheetTopology");
		applyStyle();
	}

	/**
	 * Constructor from DomainTopologyManagerGSImpl
	 */
	public DrawTopology(DomainTopologyManagerGSImpl dm) {
		domainManager = dm;
		gs = domainManager.getGraph();
		// get back the FileSinkImages that has been initialized previously
		//pic = domainManager.getFileSinkImages();
		defineFileSinkImages(); // apply others features of the fileSinkImages
		domain = true;
		stylesheet = getClass().getResource("/draw/styleSheetDomains");
		applyStyle();
	}

	/**
	 * add the localisation of the styleSheet to the graph
	 */
	private void applyStyle() {
		if(stylesheet ==  null) {
			log.error("Unable to find stylsheet");
			return;
		}
		String styleSheetpath = stylesheet.getPath();
		gs.addAttribute("ui.stylesheet", "url('file://" + styleSheetpath + "')");
	}

	/**
	 * Add the features to the FileSinkImages object
	 */
	private void defineFileSinkImages() {
		// resolution
//		pic.setResolution(Resolutions.WXGA_8by5);
//		pic.setOutputPolicy(OutputPolicy.BY_EVENT);
//		// set renderer to the display to take care of css style.
//		pic.setRenderer(FileSinkImages.RendererType.SCALA);
//		pic.setQuality(Quality.HIGH);
	}

	@Override
	public void display() {
		// Quality > Speed
		gs.addAttribute("ui.quality");
		gs.addAttribute("ui.antialias");
		gs.display(true); // automatic node placement
	}

	@Override
	public void drawShortestPath(String source, String dest)
			throws PathException {
		Path p;
		if (source == null || dest == null) {

		} else {
			// find the shortest path
			if (domain) {
				p = domainManager.shortestPath(source, dest);
			} else {
				p = topologyManager.shortestPath(source, dest);
				// conversion of the shortest path found on a toplogyManager
				// graph
			}

			// Coloration of the shortPath
			List<org.graphstream.graph.Node> ln = p.getNodePath();
			Iterator<? extends org.graphstream.graph.Node> k = ln.iterator();

			org.graphstream.graph.Node nPrec;
			org.graphstream.graph.Node nSuiv = null;
			Edge e;
			String css; // Used to merge two diffents css-style classes
			while (k.hasNext()) { // browse the path's nodes
				nPrec = nSuiv;
				nSuiv = k.next();

				css = nSuiv.getAttribute("ui.class") + ", shortestPath";
				nSuiv.addAttribute("ui.class", css);

				// Coloration of the edge between the colored nodes
				if (nPrec != null && nSuiv != null) {
					e = (nPrec.getEdgeBetween(nSuiv.getId()));
					css = e.getAttribute("ui.class") + ", shortestPath";
					e.addAttribute("ui.class", css);
				}
				// sleep(); // to see the progress of the selected path
			}

			// conversion of the shortest path found in the graphs
			// done in this order to ensure the good order of the generated
			// images
			if (domain) {
				domainManager.conversion(p);
			} else {
				topologyManager.conversion(p);
			}
		}
	}

	/**
	 * to delay the display step by step
	 */
	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	@Override
	public void startSnapshot(String path) throws ExportException {
		String prefix;
		if (domain) {
			prefix = path + "export/Domain/snapshots/image_";
		} else {
			prefix = path + "export/Infra/snapshots/image_";
		}
		try {
			pic.begin(prefix);
		} catch (IOException e) {
			String mess = "Unable to start the fileSinkImages";
			log.debug(mess, e);
			throw new ExportException(mess, e);
		}
		log.debug("begin of the set of snapshots");
	}

	@Override
	public void endSnapshot() throws ExportException {
		try {
			pic.flush();
			pic.end();
		} catch (IOException e) {
			log.debug("Unable to end the fileSinkImages", e);
			throw new ExportException("Unable to end the fileSinkImages", e);
		}
		log.debug("end of the set of snapshots");
	}

	@Override
	public void exportJPG(String path) throws ExportException {
//		pic.stabilizeLayout(0.99); // to wait until the graph is well displayed
//		String prefix;
//		if (domain) {
//			prefix = path + "export/Domain/snapshot.jpg";
//		} else {
//			prefix = path + "export/Infra/snapshot.jpg";
//		}
//		testFile(prefix);
//		pic.outputNewImage(prefix);
//		log.debug("snapshot taken");
	}

	/**
	 * Test if the file is�accessible.
	 * 
	 * @param path
	 * @throws ExportException
	 */
	public void testFile(String path) throws ExportException {
		File f = new File(path);
		if (f.exists()) {
			if (!f.canWrite()) {
				throw new ExportException("The file can not be overwritten");
			}
		} // if not : the file is created
	}

	@Override
	public void exportDOT(String path) throws ExportException {
//		try {
//			FileSinkDOT dot = new FileSinkDOT();
//			if (domain) {
//				dot.setDirected(false);
//				dot.writeAll(gs, path + "export/Domain/exportDot.dot");
//			} else {
//				dot.setDirected(true);
//				dot.writeAll(gs, path + "export/Infra/exportDot.dot");
//			}
//			log.debug("dot file created");
//		} catch (IOException e) {
//			String mess = "Unable to export a dot version of our graph";
//			log.debug(mess, e);
//			throw new ExportException(mess, e);
//		}
	}

	@Override
	public void exportDGS(String path) throws ExportException {
		try {
			if (domain) {
				gs.write(path + "export/Domain/exportDGS.dgs");
			} else {
				gs.write(path + "export/Infra/exportDGS.dgs");
			}
			log.debug("dgs file created");
		} catch (IOException e) {
			String mess = "Unable to export a dgs version of our graph";
			log.debug(mess, e);
			throw new ExportException(mess, e);
		}
	}

	@Override
	public void exportSVG(String path) {
		throw new java.lang.UnsupportedOperationException(
				"Method exportSVG() not yet implemented.");
	}
}