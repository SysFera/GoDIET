package com.sysfera.godiet.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.services.InfrastructureService;

@Component
public class InfrastructureServiceImpl implements InfrastructureService{


	@Autowired
	private InfrastructureManager instrastructureManager;


	@Override
	public void registerDomains(List<Domain> domains) throws ResourceAddException, GraphDataException{
		this.instrastructureManager.addDomains(domains);
		
	}



	@Override
	public void registerNodes(List<Node> nodes) throws ResourceAddException, GraphDataException {
		this.instrastructureManager.addNodes(nodes);		
	}

	@Override
	public void registerClusters(List<Cluster> clusters)
			throws ResourceAddException {
		this.instrastructureManager.addClusters(clusters);		
	}


	@Override
	public void registerLinks(List<Link> links) throws ResourceAddException, GraphDataException {
		this.instrastructureManager.addLinks(links);		
	}

}
