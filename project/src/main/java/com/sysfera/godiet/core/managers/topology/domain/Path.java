package com.sysfera.godiet.core.managers.topology.domain;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Represent a path
 * 
 * @author phi
 * 
 */
public class Path {

	// list of the element kept in the graph from GraphStream
	private LinkedHashSet<Hop> path;

	public void setPath(LinkedHashSet<Hop> path) {
		this.path = path;
	}

	public LinkedHashSet<Hop> getPath() {
		return path;
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

		private String idDomain;
		private String idForwarder;

		public String getIdDomain() {
			return idDomain;
		}

		public void setIdDomain(String idDomain) {
			this.idDomain = idDomain;
		}

		public String getIdForwarder() {
			return idForwarder;
		}

		public void setIdForwarder(String idForwarder) {
			this.idForwarder = idForwarder;
		}

		@Override
		public String toString() {
			return "Hop [idDomain=" + idDomain + ", idForwarder=" + idForwarder
					+ "]";
		}

		private Path getOuterType() {
			return Path.this;
		}
	}
}
