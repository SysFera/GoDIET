package com.sysfera.godiet.managers.topology;

import java.util.HashMap;
import java.util.Map;

import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Node;
/**
 * Store calculated paths.  
 * TODO: Listen Model modification ( Node down ... ) 
 * @author phi
 *
 */
public class PathManager {

	Map<PathDesc, Path> paths;
	
	PathManager() {
		 this.paths = new HashMap<PathManager.PathDesc, Path>();
	}
	class PathDesc{
		private final Node from;
		private final Node to;
		public PathDesc(Node from, Node to) {
			this.from = from;
			this.to = to;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
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
			PathDesc other = (PathDesc) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.getId().equals(other.from.getId()))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.getId().equals(other.to.getId()))
				return false;
			return true;
		}
		private PathManager getOuterType() {
			return PathManager.this;
		}
		
		@Override
		public String toString() {
			return "from " + from.getId() + " to " + to.getId(); 
			
		}
	}
}
