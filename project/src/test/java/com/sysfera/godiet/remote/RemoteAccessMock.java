/**
 * 
 */
package com.sysfera.godiet.remote;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
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

	private boolean remoteAccessDown = false;

	/**
	 * TODO: randomize the connection error. If remoteAccessDown is true,
	 * randomize on the a
	 */
	@Override
	public void run(String command, Path path) throws RemoteAccessException {
		Object[] pathResources = ((Object[]) path.getPath().toArray());
		if (pathResources == null || pathResources.length == 0)
			throw new RemoteAccessException(
					"Unable to run a command. The path is empty");
		String pathInfo = "Connection path:" + ((Resource)pathResources[0]).getId();
		for(int i = 1 ; i < pathResources.length  ; i++)
		{
			pathInfo+= "-->"+((Resource)pathResources[i]).getId();
		}
		Node remoteNode  = ((Node)pathResources[pathResources.length - 1]);
		if (remoteAccessDown) {
			throw new RemoteAccessException("Unable to run " + command + " on "
					+ remoteNode + ":" + remoteNode.getSsh().getPort() + " . Login: "
					+ remoteNode.getSsh().getPort());
		}

		log.debug("Execute: \"" + command + "\" on " + remoteNode.getSsh().getServer()+"("+remoteNode.getId() +"). Path: "+pathInfo);
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
		String pathInfo = "Connection path:" + ((Resource)pathResources[0]).getId();
		for(int i = 1 ; i < pathResources.length  ; i++)
		{
			pathInfo+= "-->"+((Resource)pathResources[i]).getId();
		}
		Node remoteNode  = ((Node)pathResources[pathResources.length - 1]);
		if (remoteAccessDown)
			throw new RemoteAccessException("Unable to copy file "
					+ file.getName() + " on " + remoteNode.getSsh().getPort() + " . Login: "
					+ remoteNode.getSsh().getPort());

		log.debug("scp " + file.getAbsolutePath() + " " +remoteNode.getDisk().getScp().getLogin()+ "@" + remoteNode.getDisk().getScp().getServer() + ":" + remoteNode.getDisk().getScp().getPort()
				+ ":" + remotePath);

	}

	public void setRemoteAccessDown(boolean remoteAccessDown) {
		this.remoteAccessDown = remoteAccessDown;
	}

	@Override
	public void addKey(String key, String pubkey, String pass) {
		log.debug("Add key: " + key);

	}
}
