package com.sysfera.godiet.managers.topology;


import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;

/**
 * 
 * 
 * @author phi
 */
public interface TopologyManager {

	public abstract void addDomain(Domain d) throws GraphDataException;	
	public abstract void addNode(Node n) throws GraphDataException;
	public abstract void addLink(Link link) throws GraphDataException;
	public abstract Path findPath(Resource from, Resource to) throws PathException;	
	
}
