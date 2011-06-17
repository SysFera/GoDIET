package com.sysfera.godiet.services;

import java.util.List;

import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;

public interface InfrastructureController {


	void registerDomains(List<Domain> domains) throws ResourceAddException, GraphDataException;


	void registerNodes(List<Node> nodes)throws ResourceAddException, GraphDataException;

	void registerClusters(List<Cluster> clusters)throws ResourceAddException;


	void registerLinks(List<Link> links) throws ResourceAddException,GraphDataException;

	
	
	
}
