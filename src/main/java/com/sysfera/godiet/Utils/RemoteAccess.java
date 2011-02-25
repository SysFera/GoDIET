package com.sysfera.godiet.Utils;

import java.io.File;

public interface RemoteAccess {

	public abstract void execute(String command,String host,int port);
	public abstract void copy(File file, String host,int port);
}
