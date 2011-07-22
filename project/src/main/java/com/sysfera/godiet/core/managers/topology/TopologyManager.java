package com.sysfera.godiet.core.managers.topology;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.core.model.Path;

/**
 * 
 * 
 * @author phi
 */
public interface TopologyManager {

	/**
	 * Add a domain into a node of the generated graph
	 * 
	 * @param d
	 *            the domain to add
	 * @throws GraphDataException
	 *             ??
	 */
	public abstract void addDomain(Domain d) throws GraphDataException;

	/**
	 * Add a node into a node of the generated graph
	 * 
	 * @param n
	 *            the node to add
	 * @throws GraphDataException
	 *             ??
	 */
	public abstract void addNode(Node n) throws GraphDataException;

	/**
	 * Add a link into a edge of the generated graph
	 * 
	 * @param link
	 *            the link to add between two nodes (which are nodes or domains)
	 * @throws GraphDataException
	 *             ??
	 */
	public abstract void addLink(Link link) throws GraphDataException;

	/**
	 * Find a path between from resource and to resource
	 * 
	 * @param from
	 *            start node
	 * @param to
	 *            end node
	 * @return The path or null if no exist
	 * @throws PathException
	 *             ??
	 */
	public abstract Path findPath(Resource from, Resource to)
			throws PathException;

}
