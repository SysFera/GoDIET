package com.sysfera.godiet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.exceptions.ResourceAddException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.PlatformManager;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Fronted;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;

@Component
public class InfrastructureControllerImpl implements InfrastructureController{


	@Autowired
	private PlatformManager instrastructureManager;


	@Override
	public void registerDomains(List<Domain> domains) throws ResourceAddException{
		this.instrastructureManager.addDomains(domains);
		
	}

	@Override
	public void registerGateways(List<Gateway> gateways)
			throws ResourceAddException {
		this.instrastructureManager.addGateways(gateways);
		
	}

	@Override
	public void registerNodes(List<Node> nodes) throws ResourceAddException {
		this.instrastructureManager.addNodes(nodes);		
	}

	@Override
	public void registerClusters(List<Cluster> clusters)
			throws ResourceAddException {
		this.instrastructureManager.addClusters(clusters);		
	}

	@Override
	public void registerFrontends(List<Fronted> fronteds)
			throws ResourceAddException {
		this.instrastructureManager.addFrontends(fronteds);		
	}

	@Override
	public void registerLinks(List<Link> links) throws ResourceAddException, GraphDataException {
		this.instrastructureManager.addLinks(links);		
	}

}
