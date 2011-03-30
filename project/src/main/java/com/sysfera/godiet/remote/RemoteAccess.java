package com.sysfera.godiet.remote;

import java.io.File;

import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * Interface to execute and copy file on a host remote
 * 
 * @author phi
 * 
 */
public interface RemoteAccess {

	/**
	 * Execute a remote command
	 * 
	 * @param command
	 *            The Command to execute
	 * @param path
	 *            The path to reach the resource destination. 
	 * @throws RemoteAccessException
	 *             if can't execute command
	 */
	public abstract void run(String command, Path path)
			throws RemoteAccessException;

	/**
	 * Copy a file on remote host
	 * 
	 * @param file
	 *            The file to copy
	 * @param remotePath The remote path where file must be copied
	 * @param path The resources path need to be cross 
	 * @throws RemoteAccessException
	 *             if can't copy file on remote host
	 */
	public abstract void copy(File file, String remotePath,Path path)
			throws RemoteAccessException;

	/**
	 * Add key in bunch
	 * 
	 * @param privKey
	 *            Private key to add
	 * @param pubKey
	 *            Public key associated to private key. Could be null if
	 * @param passphrase
	 * @throws AddKeyException
	 *             if error when key insertion. Unable to find private key,
	 *             public key or bad password TODO Check
	 */
	public abstract void addKey(String privKey, String pubKey, String passphrase)
			throws AddKeyException;
}
