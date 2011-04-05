package com.sysfera.godiet.remote.ssh;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.sysfera.godiet.exceptions.remote.AddKeyException;
import com.sysfera.godiet.exceptions.remote.RemoteAccessException;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.Node;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

public class ChannelManagerJsch {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final JSch jsch;
	private final UserInfo trustedUI;

	private final Map<Path, Session> sessions;

	public ChannelManagerJsch() {
		this.jsch = new JSch();
		this.trustedUI = new TrustedUserInfo();
		this.sessions = new HashMap<Path, Session>();
	}

	/**
	 * Close all Sessions
	 */
	public void destroy() {
		for (Map.Entry<Path, Session> e : sessions.entrySet()) {
			e.getValue().disconnect();
		}

	}

	/**
	 * Create an exec channel on the destination path. Create a new Session on
	 * the destination if doesn't exist yet
	 * 
	 * @param path
	 * @param isDisk
	 * @return an open ChannelExec
	 * @throws RemoteAccessException
	 *             If the remote node isn't a Node
	 * @throws JSchException
	 *             if can't connect to the destination or all error in Jsch.
	 *             TODO: Test this error
	 * 
	 */
	// TODO: remove isDisk when Disk will be take care in topology. Currently
	// can't manage there is a bug if node.disk.ssh.ip != node.ssh.ip
	// So need to modify (or remove ? ) the remoteAccessException condition here
	public ChannelExec getExecChannel(Path path, boolean isDisk)
			throws RemoteAccessException, JSchException {
		Session session = getSession(path);

		if (session == null) {

			LinkedHashSet<? extends Resource> hops = path.getPath();
			if (hops.size() < 2)
				throw new RemoteAccessException(
						"Path length must be > 2 (source + destination)");

			// Create the session with the last Node
			Node last = null;
			try {
				last = (Node) hops.toArray()[hops.size() - 1];
			} catch (ClassCastException e) {
				// TODO: Remove when the model will be stable
				throw new RemoteAccessException(
						"The last node must be a Node. It's a "
								+ hops.toArray()[hops.size() - 1].getClass()
										.getCanonicalName());
			}
			Ssh lastNodeConfig = null;

			if (isDisk)
				lastNodeConfig = last.getDisk().getScp();
			else
				lastNodeConfig = last.getSsh();

			session = jsch.getSession(lastNodeConfig.getLogin(),
					lastNodeConfig.getServer(), lastNodeConfig.getPort());
			session.setUserInfo(trustedUI);
			initProxiesPath(hops, session, trustedUI);

			session.connect();
			sessions.put(path, session);
		}
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		return channelExec;

	}

	/**
	 * Return a Session or Null if doesn't exists
	 * 
	 * @param path
	 * @return null if the session doesn't exist or are closed
	 */
	private Session getSession(Path path) {

		Session session = sessions.get(path);
		if (session == null || !session.isConnected()) {
			return null;
		}
		return session;
	}

	/**
	 * Set the proxy on session
	 * 
	 * @param hops
	 * 
	 * @throws JSchException
	 * @throws RemoteAccessException
	 */
	private void initProxiesPath(LinkedHashSet<? extends Resource> hops,
			Session session, UserInfo ui) throws JSchException,
			RemoteAccessException {

		// TODO: #1 Change resource to RemoteNode (ie ssh) .=> remove switch
		Resource[] resources = hops.toArray(new Resource[0]);
		log.debug("hopsSize: " + resources.length);
		if (resources.length < 3)
			return; // no need to create proxy. Path contains source and
					// destination which are in the same domain
		NCProxy lastProxy = null;
		// i = 0 is the source. Don't create a proxy
		for (int i = 1; i <= resources.length - 2; i++) {
			Resource resource = resources[i];
			Ssh ssh = null;

			if (resource instanceof Gateway) {
				ssh = ((Gateway) resource).getSsh();
			} else if (resource instanceof Node) {
				ssh = ((Node) resource).getSsh();
			} else {
				new RemoteAccessException("Physical resource"
						+ resource.getClass().getCanonicalName()
						+ "not managed");
			}

			NCProxy proxy = new NCProxy(ssh.getLogin(), ssh.getServer(),
					ssh.getPort(), jsch, ui);
			if (lastProxy != null) {
				log.debug("Add proxy " + ssh.getServer() + " to "
						+ lastProxy.getHost());
				proxy.setProxy(lastProxy);
			}

			lastProxy = proxy;

		}
		log.debug("Add proxy " + lastProxy.getHost() + " to session");
		session.setProxy(lastProxy);

	}

	public void debug(boolean activate) {
//		if (activate) {
//			com.jcraft.jsch.Logger jschLog = new com.jcraft.jsch.Logger() {
//
//				@Override
//				public void log(int level, String message) {
//					switch (level) {
//					case 0:
//						log.debug(message);
//						break;
//					case 1:
//						log.info(message);
//						break;
//					case 2:
//						log.warn(message);
//						break;
//					case 3:
//						log.error(message);
//						break;
//					case 4:
//						log.error("FATAL" + message);
//						break;
//
//					default:
//						break;
//					}
//
//				}
//
//				@Override
//				public boolean isEnabled(int level) {
//					return true;
//				}
//			};
//			JSch.setLogger(jschLog);
//		} else
//			JSch.setLogger(null);

		
	}

	public void addIdentity(String privateKey, String publicKey, String passphrase) throws AddKeyException {
		try {
			jsch.addIdentity(privateKey, publicKey, passphrase.getBytes());
		} catch (JSchException e) {

			throw new AddKeyException("Unable to add key", e);
		} finally {
			// Give to GC
			passphrase = null;
		}
		
	}

	public JSch getJsch() {
		return jsch;
	}

}
