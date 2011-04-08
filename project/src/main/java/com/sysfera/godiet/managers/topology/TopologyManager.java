package com.sysfera.godiet.managers.topology;

import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.graph.PathException;
import com.sysfera.godiet.model.Path;
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
	 * @param from
	 * @param to
	 * @throws GraphDataException
	 */
	public abstract void addLink(Resource from, Resource to) throws GraphDataException;
	
	
	 /** Find the shortest path between from resource and to resource.
	 * 
	 * @param from Start node
	 * @param to End node
	 * @return The Path or null if no path exist
	 * TODO CHECK ALL PATHEXCEPTION THROWN REASON
	 */
	public abstract Path findPath(Resource from, Resource to) throws PathException;
}
