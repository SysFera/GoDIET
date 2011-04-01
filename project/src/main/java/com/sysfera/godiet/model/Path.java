package com.sysfera.godiet.model;

import java.util.LinkedHashSet;

import com.sysfera.godiet.model.generated.Resource;

/**
 * Represent a path 
 * @author phi
 *
 */
public class Path {

	
	//TODO: #1 Change resource to RemoteNode (ie ssh) 
	private LinkedHashSet<? extends Resource>  path;

	public void setPath(LinkedHashSet<? extends Resource>  path) {
		this.path = path;
	}
	
	public LinkedHashSet<? extends Resource> getPath() {
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


	
}
