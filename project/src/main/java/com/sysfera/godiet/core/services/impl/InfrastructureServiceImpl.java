package com.sysfera.godiet.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.ResourceAddException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Cluster;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.services.InfrastructureService;
import com.sysfera.godiet.core.managers.InfrastructureManager;

@Component
public class InfrastructureServiceImpl implements InfrastructureService {

	@Autowired
	private InfrastructureManager instrastructureManager;

	@Override
	public void registerDomains(List<Domain> domains)
			throws ResourceAddException, GraphDataException {
		this.instrastructureManager.addDomains(domains);

	}

	@Override
	public void registerNodes(List<Node> nodes) throws ResourceAddException,
			GraphDataException {
		this.instrastructureManager.addNodes(nodes);
	}

	@Override
	public void registerClusters(List<Cluster> clusters)
			throws ResourceAddException {
		this.instrastructureManager.addClusters(clusters);
	}

	@Override
	public void registerLinks(List<Link> links) throws ResourceAddException,
			GraphDataException {
		this.instrastructureManager.addLinks(links);
	}

	@Override
	public List<Node> getNodes() {
		return this.instrastructureManager.getNodes();
	}
}
