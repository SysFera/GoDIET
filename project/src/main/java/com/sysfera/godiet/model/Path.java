package com.sysfera.godiet.model;

import java.util.LinkedHashSet;

import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * Represent a path 
 * @author phi
 *
 */
public class Path {

	class LinkPath{
		
	}
	
	
	private LinkedHashSet<Ssh>  path;

	public void setPath(LinkedHashSet<Ssh>  path) {
		this.path = path;
	}
	
	public LinkedHashSet<Ssh> getPath() {
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
		if (this == obj) {
			return true;
                }
		if (obj == null) {
			return false;
                }
		if (getClass() != obj.getClass()) {
			return false;
                }
		Path other = (Path) obj;
		if (path == null) {
			if (other.path != null) {
				return false;
                        }
		} else {
                    if (!path.equals(other.path)) {
			return false;
                    }
                }
		return true;
	}


	
}
