package com.sysfera.godiet.core.managers.topology.infrastructure;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Ssh;

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

		private Path getOuterType() {
			return Path.this;
		}
	}
}
