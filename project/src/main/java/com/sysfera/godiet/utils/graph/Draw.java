package com.sysfera.godiet.utils.graph;

import java.io.IOException;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.graphstream.graph.Graph;

import com.sysfera.godiet.managers.topology.TopologyManagerGSImpl;

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

		//Generator gen = new WattsStrogatzGenerator(20, 2, 0.5);
		//gen.addSink(gs);
	}

	public void display() {
		gs.addAttribute("ui.stylesheet",
				"url('file:///C:/Users/Soon/GoDIET/project/styleSheetI')");
		gs.display(true); // automatic node placement
	}

	public void exportDGS() {
		try {
			gs.write("DGSFormat.dgs");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shortestPath() {


         Dijkstra dijkstra = new Dijkstra(Element.edge, "weight", "Node1_Domain1");
         dijkstra.init(gs);
         dijkstra.compute();

         System.out.println(dijkstra.getShortestPath(gs.getNode("Node8_Domain6")));
         }	
}
