package com.sysfera.godiet.model.configurator;

/**
 * Software configuration file. 
 * @author phi
 *
 */
public class ConfigurationFile {

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
}
