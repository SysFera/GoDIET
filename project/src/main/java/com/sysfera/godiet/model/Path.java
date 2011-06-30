package com.sysfera.godiet.model;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * Represent a path
 * 
 * @author phi
 * 
 */
public class Path {
	private LinkedHashSet<Hop> path;

	public void setPath(LinkedHashSet<Hop> path) {
		this.path = path;
	}

	public LinkedHashSet<Hop> getPath() {
		return path;
	}

	public Resource getDestination() {
		if (path == null || path.size() < 1) {
			return null;
		}
		
		return path.toArray(new Hop[0])[path.size() - 1].getDestination();

	}

	@Override
	public String toString() {
		Iterator<Hop> i = path.iterator();
		String s = null;
		while (i.hasNext()) {
			s += i.next().toString();
		}
		return s;
	}

	public class Hop {

		private Node destination;
		private Ssh link;
		private Domain crossedDomain = null;

		public Node getDestination() {
			return destination;
		}

		public void setDestination(Node destination) {
			this.destination = destination;
		}

		public Ssh getLink() {
			return link;
		}

		public void setLink(Ssh link) {
			this.link = link;
		}

		@Override
		public String toString() {
			String s = null;
			s = "Ssh : " + getLink().getId() + " , " + "Node : "
					+ getDestination().getId();
			return s;
		}

		public void crossDomain(Domain domain) {
			this.crossedDomain = domain;
		}

		/**
		 * 
		 * @return the crossed domain. Null if cross no domain
		 */
		public Domain getCrossedDomain() {
			return crossedDomain;
		}

		private Path getOuterType() {
			return Path.this;
		}
	}
}
