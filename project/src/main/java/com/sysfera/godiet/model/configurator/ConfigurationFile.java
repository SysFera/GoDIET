package com.sysfera.godiet.model.configurator;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.model.generated.Resource;



/**
 * Software configuration file. 
 * @author phi
 *
 */
public class ConfigurationFile {

	//TODO: Do something better.
	private List<Resource> copiedOn = new ArrayList<Resource>();
	private String id;
	private String contents;
	private String absolutePath;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	protected void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	//TODO: see fixme
	public void copied(Resource on)
	{
		this.copiedOn.add(on);
	}
	
	public boolean isCopiedOn(Resource on)
	{
		return this.copiedOn.contains(on);
	}
}
