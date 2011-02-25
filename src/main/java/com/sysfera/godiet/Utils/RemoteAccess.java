package com.sysfera.godiet.Utils;

import java.io.File;
/**
 * Interface to execute and copy file on a host remote 
 * @author phi
 *
 */
public interface RemoteAccess {

	/**
	 * Execute a remote command
	 * @param command The Command to execute
	 * @param host The destination host
	 * @param port The destination port
	 */
	public abstract void execute(String command,String host,int port);

	/**
	 * Copy a file on remote host 
	 * @param file The file to copy
	 * @param host The destination host
	 * @param port The destination port
	 */
	public abstract void copy(File file, String host,int port);
}
