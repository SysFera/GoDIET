package com.sysfera.godiet.core.managers.topology.infrastructure;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Domain;
import com.sysfera.godiet.common.model.generated.Link;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;

/**
 * Interface of 
 * 
 * @author phi
 */
public interface TopologyManager {

	/**
	 * Add a domain into a node of the generated graph.
	 * 
	 * @param d
	 *            The domain to add.
	 * @throws GraphDataException
	 *             To manage the errors related to the semantic of the graph (if
	 *             d is already existed for example).
	 */
	public abstract void addDomain(Domain d) throws GraphDataException;

	/**
	 * Add a node into a node of the generated graph.
	 * 
	 * @param n
	 *            the node to add.
	 * @throws GraphDataException
	 *             To manage the errors related to the semantic of the graph (if
	 *             n is already existed for example).
	 */
	public abstract void addNode(Node n) throws GraphDataException;

	/**
	 * Add a link into a edge of the generated graph.
	 * 
	 * @param link
	 *            the link to add between two graph's nodes (which are nodes or domains)
	 * @throws GraphDataException
	 *             To manage the errors related to the semantic of the graph (if
	 *             link is already existed for example).
	 */
	public abstract void addLink(Link link) throws GraphDataException;

	/**
	 * Find a path between resources from and to.
	 * 
	 * @param from
	 *            start node
	 * @param to
	 *            end node
	 * @return The found path
	 * @throws PathException
	 *             To manage the errors related to the shortest path.
	 */
	public abstract Path findPath(Resource from, Resource to)
			throws PathException;
}
