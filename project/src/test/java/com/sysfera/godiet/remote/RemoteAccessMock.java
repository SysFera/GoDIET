/**
 * 
 */
package com.sysfera.godiet.remote;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.configurator.ConfigurationFile;
import com.sysfera.godiet.model.generated.Ssh;

/**
 * 
 * Mock of ssh. See integration test to test all features
 * 
 * @author phi
 * 
 */

public class RemoteAccessMock implements RemoteAccess {
	private Logger log = LoggerFactory.getLogger(getClass());
	Set<SSHKeyManager> keyBunch = new HashSet<SSHKeyManager>();

	private boolean remoteAccessDown = false;

	/**
	 * TODO: randomize the connection error. If remoteAccessDown is true,
	 * randomize on the a
	 */
	@Override
	public Integer launch(String command, Path path)
			throws RemoteAccessException {
		Hop[] pathResources = ((Hop[]) path.getPath().toArray(new Hop[0]));
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:" +pathResources[0].getDestination().getId();
				
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + pathResources[i].getDestination().getId();
		}
		Ssh remoteNode = pathResources[pathResources.length- 1].getLink();
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to run " + command + " on "
					+ remoteNode.getServer() + ":" + remoteNode.getPort()
					+ " . Login: " + remoteNode.getLogin());
		}

		log.debug("Execute: \"" + command + "\" on "
				+ remoteNode.getServer() + "(" + remoteNode.getId()
				+ "). Path: " + pathInfo);
		return 1234;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.RemoteAccess#copy(java.io.File,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	public void copy(ConfigurationFile file, String remotePath, Path path)
			throws RemoteAccessException {
		Hop[] pathResources = ((Hop[]) path.getPath().toArray(new Hop[0]));
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:" +pathResources[0].getDestination().getId();
				
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + pathResources[i].getDestination().getId();
		}
		Ssh remoteNode = pathResources[pathResources.length- 1].getLink();
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to scp "
					+ remoteNode.getServer() + ":" + remoteNode.getPort()
					+ " . Login: " + remoteNode.getLogin());
		}

		if (remoteAccessDown)
			throw new RemoteAccessException("Unable to copy file "
					+ file.getId() + " on " + remoteNode.getPort()
					+ " . Login: " + remoteNode.getPort());

		log.debug("scp " + file.getAbsolutePath() + " "
				+ remoteNode.getLogin() + "@"
				+ remoteNode.getServer() + ":"
				+ remoteNode.getPort() + ":" + remotePath);

	}

	public void setRemoteAccessDown(boolean remoteAccessDown) {
		this.remoteAccessDown = remoteAccessDown;
	}

	@Override
	public void addItentity(SSHKeyManager key) {
		keyBunch.add(key);
	}

	@Override
	public void check(String pid, Path path) throws RemoteAccessException {
		Hop[] pathResources = ((Hop[]) path.getPath().toArray(new Hop[0]));
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:" +pathResources[0].getDestination().getId();
				
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + pathResources[i].getDestination().getId();
		}
		Ssh remoteNode = pathResources[pathResources.length- 1].getLink();
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to check process " + pid
					+ " on " + remoteNode + ":" + remoteNode.getPort()
					+ " . Login: " + remoteNode.getPort());
		}

		log.debug("Process " + pid + " running on " + remoteNode.getId());

	}

	@Override
	public void removeItentity(SSHKeyManager sshkey)
			throws RemoveAuthentificationException {
		keyBunch.remove(sshkey);

	}

	
}
