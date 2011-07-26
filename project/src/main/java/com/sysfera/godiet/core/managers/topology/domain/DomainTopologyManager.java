package com.sysfera.godiet.core.managers.topology.domain;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.graph.GraphDataException;
import com.sysfera.godiet.common.model.generated.Domain;

public interface DomainTopologyManager {

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
	 * Add a forwarder into an edge of the generated graph
	 * 
	 * @param client
	 *            Source domain of the forwarder
	 * @param server
	 *            Destination domain of the forwarder
	 * @throws GraphDataException
	 *             To manage the errors related to the semantic of the graph.
	 */
	public abstract void addForwarder(Domain client, Domain server)
			throws GraphDataException;


	/**
	 * Find a shortestpath between two domains
	 * 
	 * @param from
	 *            Source domain
	 * @param to
	 *            Destination domain
	 * @return The found path
	 * @throws PathException
	 *             To manage the errors related to the semantic of the graph
	 */
	public abstract Path findPath(Domain from, Domain to) throws PathException;
}
