package com.sysfera.godiet.common.services;

import java.util.List;

import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Cluster;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;

public interface InfrastructureService {


	void registerDomains(List<Domain> domains) throws ResourceAddException, GraphDataException;


	void registerNodes(List<Node> nodes)throws ResourceAddException, GraphDataException;

	void registerClusters(List<Cluster> clusters)throws ResourceAddException;


	void registerLinks(List<Link> links) throws ResourceAddException,GraphDataException;


 	public abstract List<Node> getNodes();
 	

	
	
	
}
