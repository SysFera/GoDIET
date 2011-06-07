package com.sysfera.godiet.model;

import java.util.Iterator;
import java.util.LinkedHashSet;

import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * Represent a path
 * 
 * @author phi
 * 
 */
public class Path {

	

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
				s = "Ssh : " + getLink().getId() + " , " + "Node : " + getDestination().getId();				
			return s;
		}
	}

	class LinkPath {

	}

	private LinkedHashSet<Hop> path;

	public void setPath(LinkedHashSet<Hop> path) {
		this.path = path;
	}

	public LinkedHashSet<Hop> getPath() {
		return path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		Iterator<Hop> i = path.iterator();
		String s = null;
		while(i.hasNext()) {
			s += i.next().toString();
		}
		return s;
	}
}
