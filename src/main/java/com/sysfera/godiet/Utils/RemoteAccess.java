package com.sysfera.godiet.Utils;

import java.io.File;

import com.sysfera.godiet.exceptions.RemoteAccessException;
/**
 * Interface to execute and copy file on a host remote 
 * @author phi
 *
 */
public interface RemoteAccess {

	/**
	 * Execute a remote command
	 * @param command The Command to execute
	 * @param user User name
	 * @param host The destination host
	 * @param port The destination port
	 */
	public abstract void run(String command, String user,String host,int port) throws RemoteAccessException;

	/**
	 * Copy a file on remote host 
	 * @param file The file to copy
	 * @param user User name
	 * @param host The destination host
	 * @param port The destination port
	 */
	public abstract void copy(File file, String user,String host,int port)throws RemoteAccessException;
}
