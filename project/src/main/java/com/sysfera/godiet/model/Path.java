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
	
	
	
}
