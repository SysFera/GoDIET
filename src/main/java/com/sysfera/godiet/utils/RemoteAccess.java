package com.sysfera.godiet.utils;

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
	 * @throws RemoteAccessException if can't execute command
	 */
	public abstract void run(String command, String user,String host,int port) throws RemoteAccessException;

	/**
	 * Copy a file on remote host 
	 * @param file The file to copy
	 * @param user User name
	 * @param host The destination host
	 * @param port The destination port
	 * @throws RemoteAccessException if can't copy file on remote host
	 */
	public abstract void copy(File file, String user,String host,int port)throws RemoteAccessException;
	
	/**
	 * Add key in bunch
	 * @param privKey Private key to add
	 * @param pubKey Public key associated to private key. Could be null if 
	 * @param passphrase
	 */
	public abstract void addKey(String privKey,String pubKey,String passphrase);
}
