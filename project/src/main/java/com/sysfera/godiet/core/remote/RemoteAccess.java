package com.sysfera.godiet.core.remote;

import com.sysfera.godiet.common.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.common.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.common.exceptions.remote.CheckException;
import com.sysfera.godiet.common.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.common.model.ConfigurationFile;
import com.sysfera.godiet.core.managers.topology.infrastructure.Path;
import com.sysfera.godiet.core.managers.user.SSHKeyManager;


/**
 * Interface to execute and copy file on a host remote
 * 
 * @author phi
 * 
 */
public interface RemoteAccess {

	/**
	 * Execute a remote command and return the pid
	 * 
	 * @param command
	 *            The Command to execute
	 * @param path
	 *            The path to reach the resource destination.
	 * @return pid the Process identifier or null if can't get it
	 * @throws RemoteAccessException
	 *             if can't execute command
	 */
	public abstract Integer launch(String command, Path path)
			throws RemoteAccessException;

	/**
	 * Copy a file on remote host
	 * 
	 * @param configurationFile
	 *            The file to copy
	 * @param remotePath
	 *            The remote path where file must be copied
	 * @param path
	 *            The resources path need to be cross
	 * @throws RemoteAccessException
	 *             if can't copy file on remote host
	 */
	public abstract void copy(ConfigurationFile configurationFile, String remotePath, Path path)
			throws RemoteAccessException;

	/**
	 * Add key in bunch
	 * 
	 * @param sshkey ssh key description
	 * @throws AddAuthentificationException
	 *             if error when key insertion. Unable to find private key,
	 *             public key or bad password TODO Check
	 */
	public abstract void addItentity(SSHKeyManager sshkey)
			throws AddAuthentificationException;

	/**
	 * Remove  key in bunch
	 * 
	 * @param sshkey ssh key description
	 * @throws RemoveAuthentificationException
	 *             if error when key removing. Unable to find private key,
	 *             public key or bad password TODO Check
	 */
	public abstract void removeItentity(SSHKeyManager sshkey) throws RemoveAuthentificationException;

	/**
	 * Check the process given by pid is running
	 * @param pid The pid of process to check
	 * @param path
	 * @return
	 * @throws CheckException If unable to check or if the process doesn't running
	 */
	public abstract void check(String pid, Path path) throws RemoteAccessException;
}
