package com.sysfera.godiet.managers.topology;


import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
/**
 * 
 * 
 * @author phi
 */
public interface TopologyManager {

	/**
	 * Add a link in the graph.
	 * @param link
	 * @throws GraphDataException
	 */
	public abstract void addLink(Link link) throws GraphDataException;
	
	
	/**
	 * Add a resource in graph. Could be
	 * @param node
	 * 
	 * @see Node
	 */
	public abstract void addResource(Resource resource) throws GraphDataException;
	
	
	 /** Find the shortest path between from resource and to resource.
	 * 
	 * @param from Start node
	 * @param to End node
	 * @return The Path or null if no path exist
	 * TODO Split PATHEXCEPTION 
	 */
	public abstract Path findPath(Resource from, Resource to) throws PathException;
}
