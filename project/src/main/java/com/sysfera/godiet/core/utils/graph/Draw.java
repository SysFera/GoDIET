package com.sysfera.godiet.core.utils.graph;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.core.managers.topology.TopologyManagerGSImpl;

public class Draw {

	private TopologyManagerGSImpl topologyManager;
	Graph gs;

	// Affichage graphe
	public Draw(TopologyManagerGSImpl tp) {

		topologyManager = tp;
		gs = topologyManager.getGraph();
		// Quality > Speed
		gs.addAttribute("ui.quality");
		gs.addAttribute("ui.antialias");


		Generator gen = new DorogovtsevMendesGenerator();
		gen.addSink(gs);
		gen.begin();
		for(int i=0; i<100; i++) {
		    gen.nextEvents();
		}
		gen.end();
		
	}

	public void display() {
		URL stylesheet = getClass().getResource("/draw/styleSheet");
		String styleSheetpath = stylesheet.getPath();
		gs.addAttribute("ui.stylesheet", "url('file://" + styleSheetpath
				+ "')\"");
		gs.display(true); // automatic node placement
	}

	public void exportDGS() {
		try {
			gs.write("DGSFormat.dgs");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawShortestPath(String source, String dest) throws PathException {
		Path p = topologyManager.shortestPath(source, dest);

		// Coloration of the shortPath
		List<org.graphstream.graph.Node> ln = p.getNodePath();
		Iterator<? extends org.graphstream.graph.Node> k = ln.iterator();

		org.graphstream.graph.Node nPrec;
		org.graphstream.graph.Node nSuiv = null;
		Edge e;
		String css; // Used to merge two diffents css-style classes
		while (k.hasNext()) {
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
			sleep();
		}
		topologyManager.conversion(p);
	}

	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
}
