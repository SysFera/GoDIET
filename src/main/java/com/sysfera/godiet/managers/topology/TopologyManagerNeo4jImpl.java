package com.sysfera.godiet.managers.topology;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.exceptions.graph.PathException;
import com.sysfera.godiet.managers.Platform;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Resource;

/**
 * Topology manager based on Neo4j implementation It store relationship with
 * domains. Each domains are linked with Resource (i.e Gateways)
 * 
 * @author phi
 * 
 */
public class TopologyManagerNeo4jImpl implements TopologyManager {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final String DB_PATH = "neo4j-shortest-path";
	// private static final String DOMAINNAME_KEY = "domainname";
	private static final String RESOURCE_KEY = "resource";
	private static RelationshipType KNOWS = DynamicRelationshipType
			.withName("KNOWS");

	private static GraphDatabaseService graphDb;
	private static IndexService indexService;

	// Associate a list of resource(value) for each domain(key) given by name.
	private final Map<String, Set<Resource>> resourcesDomain;

	private final Platform platform;

	public TopologyManagerNeo4jImpl(Platform platform) {
		this.platform = platform;
		this.resourcesDomain = new HashMap<String, Set<Resource>>();
		deleteFileOrDirectory(new File(DB_PATH));
		graphDb = new EmbeddedGraphDatabase(DB_PATH);
		indexService = new LuceneIndexService(graphDb);
		registerShutdownHook();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.managers.topology.TopologyManager#searchPath(com.sysfera
	 * .godiet.model.generated.Resource,
	 * com.sysfera.godiet.model.generated.Resource)
	 */
	@Override
	public Path findPath(Resource from, Resource to) throws PathException {
		// Temporary add node on graph for computation path
		Node nodeFrom = getNode(from);
		Node nodeTo = getNode(to);
		boolean resourceFromExist = true;
		boolean resourceToExist = true;
		try {
			if (nodeFrom == null) {
				resourceFromExist = false;
				// temporary add resource on the graph
				addResource(from);
				nodeFrom = getNode(from);

			}

			if (nodeTo == null) {
				resourceToExist = false;
				// temporary add resource on the graph
				addResource(to);
				nodeTo = getNode(to);
			}
		} catch (GraphDataException e) {
			throw new IllegalStateException("Find path error", e);
		}

		// Shorted Path
		PathFinder<org.neo4j.graphdb.Path> finder = GraphAlgoFactory
				.shortestPath(
						Traversal.expanderForTypes(KNOWS, Direction.OUTGOING),
						40);
		org.neo4j.graphdb.Path foundPath = finder.findSinglePath(nodeFrom,
				nodeTo);

		Path dietPath = null;
		try {
			if (foundPath == null)
				return null;
			dietPath = DietPathBuilder.build(foundPath, platform);
		} catch (GraphDataException e) {
			throw new PathException("Unable to build path", e);
		} finally {
			// Delete resources if needed
			if (resourceFromExist == false)
				deleteNode(from);
			if (resourceToExist == false)
				deleteNode(to);

		}
		return dietPath;
	}

	private static class DietPathBuilder {

		static Path build(final org.neo4j.graphdb.Path neo4JPath,
				final Platform platform) throws GraphDataException {
			Path path = new Path();
			LinkedHashSet<Resource> res = new LinkedHashSet<Resource>();
			Iterable<Node> nodes = neo4JPath.nodes();
			for (Node node : nodes) {
				Resource resource = platform.getResource((String) node
						.getProperty(RESOURCE_KEY, node.getId()));
				if (resource == null)
					throw new GraphDataException(
							"Fatal. Resource null, will never appears");
				res.add(resource);
			}
			path.setPath(res);
			return path;
		}
	}

	/**
	 * Add a resource on the graph. Create Fullmesh with all existing resources
	 * in the same domain
	 * 
	 * 
	 * @param Resource
	 *            to add
	 * @throws GraphDataException
	 *             if the domain is not referenced yet
	 */
	private void addResource(Resource resToAdd) throws GraphDataException {
		if (resToAdd.getDomain() == null
				|| !resourcesDomain
						.containsKey(resToAdd.getDomain().getLabel()))
			throw new GraphDataException("Incorrect domain for "
					+ resToAdd.getId());
		Set<Resource> resources = resourcesDomain.get(resToAdd.getDomain()
				.getLabel());
		for (Resource resource : resources) {
			if (resource.equals(resToAdd))
				continue;
			Transaction tx = graphDb.beginTx();
			try {
				log.info("Mesh " + resToAdd.getId() + " with "
						+ resource.getId());
				createChain(resToAdd, resource);
				createChain(resource, resToAdd);
				tx.success();
			} finally {
				tx.finish();
			}
		}
		resources.add(resToAdd);
	}

	/**
	 * @throws GraphDataException
	 * 
	 * 
	 */
	@Override
	public void addLink(Resource from, Resource to) throws GraphDataException {
		try {
			addGateway(from);
			addGateway(to);
			Transaction tx = graphDb.beginTx();
			try {
				createChain(from, to);
				tx.success();
			} finally {
				tx.finish();
			}
		} catch (IllegalArgumentException e) {
			throw new GraphDataException("Error when try to add link between " + from.getId() + " and " +to.getId(),e);
		}
	}

	/**
	 * Add a gateway on domain. Create a new domain if the gateway domain
	 * doesn't exist.
	 * 
	 * @param gateway
	 */
	private void addGateway(Resource gateway) throws GraphDataException {

		Domain domain = gateway.getDomain();
		if (resourcesDomain.get(domain.getLabel()) == null) {
			Set<Resource> resources = new HashSet<Resource>();
			resourcesDomain.put(domain.getLabel(), resources);
		}
		addResource(gateway);

	}

	private static void createChain(Resource... resources) {
		for (int i = 0; i < resources.length - 1; i++) {
			Node firstNode = getOrCreateNode(resources[i]);
			Node secondNode = getOrCreateNode(resources[i + 1]);
			firstNode.createRelationshipTo(secondNode, KNOWS);

		}
	}

	/**
	 * Remove resource from graph and domain resources.
	 * 
	 * @param resource
	 */
	private void deleteNode(Resource resource) {
		Transaction tx = graphDb.beginTx();
		try {
			// Remove from resource referenced in the domain
			Set<Resource> resources = resourcesDomain.get(resource.getDomain()
					.getLabel());
			if (resources == null || !resources.remove(resource))
				log.error("Unable to remove element from list");

			// Remove from graph
			Node nodeToDelete = getNode(resource);
			Iterable<Relationship> relationShips = nodeToDelete
					.getRelationships();
			if (relationShips != null) {
				for (Relationship rel : relationShips) {
					rel.delete();
				}
			}
			nodeToDelete.delete();
			tx.success();
		} finally {
			tx.finish();
		}
	}

	/**
	 * 
	 * @param resource
	 * @return Node if exist, null otherwise
	 */
	private static Node getNode(Resource resource) {
		return indexService.getSingleNode(RESOURCE_KEY, resource.getId());
	}

	private static Node getOrCreateNode(Resource resource) {
		Node node = getNode(resource);
		if (node == null) {
			Transaction tx = graphDb.beginTx();
			try {
				node = graphDb.createNode();
				// node.setProperty(DOMAINNAME_KEY, resource.getDomain()
				// .getLabel());
				node.setProperty(RESOURCE_KEY, resource.getId());
				// indexService.index(node, DOMAINNAME_KEY, resource.getDomain()
				// .getLabel());
				indexService.index(node, RESOURCE_KEY, resource.getId());
				tx.success();
			} finally {
				tx.finish();
			}
		} else {
			// if domain are referenced yet. Check if it's the same gateway
			// if yes nothing to do
			// if no create full mesh between both gateways
		}
		return node;
	}

	// Registers a shutdown hook for the Neo4j instance so that it
	// shuts down nicely when the VM exits
	private static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				indexService.shutdown();
				graphDb.shutdown();
			}
		});
	}

	private static void deleteFileOrDirectory(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (File child : file.listFiles()) {
					deleteFileOrDirectory(child);
				}
			}
			file.delete();
		}
	}

}
