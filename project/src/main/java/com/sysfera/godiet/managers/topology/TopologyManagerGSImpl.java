package com.sysfera.godiet.managers.topology;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

public class TopologyManagerGSImpl implements TopologyManager{

	private final Graph gs;	
	
	public TopologyManagerGSImpl() {
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.gs = new SingleGraph("Construction Infrastructure");	
	}
	
	public Graph getGraph(){
		return gs;
	}
	
	@Override
	public void addLink(Link link) throws GraphDataException {
		String nodeFrom = link.getFrom().getId();
		String nodeTo = link.getTo().getId();
		String idLink = nodeFrom+"_"+nodeTo;
		gs.addEdge(idLink, nodeFrom, nodeTo, true);
		gs.getEdge(idLink).addAttribute("ui.class", "nodeToNode");
        gs.getEdge(idLink).addAttribute("id", idLink); // Storage of the id of the link node > node	
        gs.getEdge(idLink).addAttribute("weight", 1);
	}

	@Override
	public void addNode(Node n) throws GraphDataException {	
		String idNode = n.getId();
		gs.addNode(idNode); // adding
        gs.getNode(idNode).addAttribute("ui.label", idNode);
        gs.getNode(idNode).addAttribute("ui.class", "noeud"); // for the management of the node's display
        gs.getNode(idNode).addAttribute("id", idNode); // Storage of the id of the node
        gs.getNode(idNode).addAttribute("noeud", n); // Storage of the node under the id "noeud"
        gs.getNode(idNode).addAttribute("weight", 2);
        // Add the link node > domain
        List<Ssh> sshs =  n.getSsh();
        if(sshs != null)
        {
        	for (Ssh ssh : sshs) {
        		String idDomain = ssh.getDomain().getId(); // Storage of the id of our domain
        		String idLink = idNode+"_"+idDomain;
				gs.addEdge(idLink, idNode, idDomain);
		        gs.getEdge(idLink).addAttribute("ui.class", "belongto");
		        gs.getEdge(idLink).addAttribute("id", idLink); // Storage of the id of the link node > domain	
		        gs.getEdge(idLink).addAttribute("weight", 1);
        	}
        }
	}
	
	@Override
	public void addDomain(Domain d) throws GraphDataException {
		String idDomain = d.getId();
		gs.addNode(idDomain); // adding
		gs.getNode(idDomain).addAttribute("ui.label", idDomain);
        gs.getNode(idDomain).addAttribute("ui.class", "domain"); // for the management of the domain's display
        gs.getNode(idDomain).addAttribute("id", idDomain); // Storage of the id of the domain
        gs.getNode(idDomain).addAttribute("domain", d); // Storage of the domain under the id "domain"
        gs.getNode(idDomain).addAttribute("weight", 3);
	}

}
