/**
 * 
 */
package com.sysfera.godiet.remote;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Resource;

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
		Object[] pathResources = ((Object[]) path.getPath().toArray());
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:"
				+ ((Resource) pathResources[0]).getId();
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + ((Resource) pathResources[i]).getId();
		}
		Resource remoteNode = ((Resource) pathResources[pathResources.length - 1]);
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to run " + command + " on "
					+ remoteNode + ":" + remoteNode.getSsh().getPort()
					+ " . Login: " + remoteNode.getSsh().getPort());
		}

		log.debug("Execute: \"" + command + "\" on "
				+ remoteNode.getSsh().getServer() + "(" + remoteNode.getId()
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
	public void copy(File file, String remotePath, Path path)
			throws RemoteAccessException {
		Object[] pathResources = ((Object[]) path.getPath().toArray());
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:"
				+ ((Resource) pathResources[0]).getId();
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + ((Resource) pathResources[i]).getId();
		}
		Resource remoteNode = (Resource) pathResources[pathResources.length - 1];
		if (remoteAccessDown)
			throw new RemoteAccessException("Unable to copy file "
					+ file.getName() + " on " + remoteNode.getSsh().getPort()
					+ " . Login: " + remoteNode.getSsh().getPort());

		log.debug("scp " + file.getAbsolutePath() + " "
				+ remoteNode.getDisk().getScp().getLogin() + "@"
				+ remoteNode.getDisk().getScp().getServer() + ":"
				+ remoteNode.getDisk().getScp().getPort() + ":" + remotePath);

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
		Object[] pathResources = ((Object[]) path.getPath().toArray());
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:"
				+ ((Resource) pathResources[0]).getId();
		for (int i = 1; i < pathResources.length; i++) {
			pathInfo += "-->" + ((Resource) pathResources[i]).getId();
		}
		Resource remoteNode = ((Resource) pathResources[pathResources.length - 1]);
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to check process " + pid
					+ " on " + remoteNode + ":" + remoteNode.getSsh().getPort()
					+ " . Login: " + remoteNode.getSsh().getPort());
		}

		log.debug("Process " + pid + " running on " + remoteNode.getId());

	}



	@Override
	public void removeItentity(SSHKeyManager sshkey)
			throws RemoveAuthentificationException {
		keyBunch.remove(sshkey);
		
	}
}
