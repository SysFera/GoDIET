package com.sysfera.godiet.common.model;

import java.io.Serializable;

public interface ConfigurationFile extends Serializable{

	public abstract String getId();

	public abstract String getContents();

	public abstract String getAbsolutePath();

}