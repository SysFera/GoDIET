package com.sysfera.godiet.remote.ssh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.sysfera.godiet.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.exceptions.remote.RemoveAuthentificationException;
import com.sysfera.godiet.managers.ConfigurationManager;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.Path;
import com.sysfera.godiet.model.Path.Hop;
import com.sysfera.godiet.model.generated.GoDietConfiguration.Proxy;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Ssh;

public class ChannelManagerJsch {

	private Logger log = LoggerFactory.getLogger(getClass());
	private final JSch jsch;
	private final UserInfo trustedUI;
	private Set<SSHKeyManager> keybunch = new HashSet<SSHKeyManager>();

	// FIXME. Do something better
	@Autowired
	private ConfigurationManager goDietConfiguration;
	private final Map<Path, Session> bufferedSessions;

	public ChannelManagerJsch() {
		this.jsch = new JSch();
		this.trustedUI = new TrustedUserInfo();
		this.bufferedSessions = new HashMap<Path, Session>();
	}

	/**
	 * Close all Sessions
	 */
	
	public void destroy() {
		for (Map.Entry<Path, Session> e : bufferedSessions.entrySet()) {
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

		if (session == null || !session.isConnected()) {

			LinkedHashSet<Hop> hops = path.getPath();
			//FIXME : handle localhost
//			if (hops.size() < 2) {
//				throw new RemoteAccessException(
//						"Path length must be > 2 (source + destination)");
//			}

			// Create the session with the last Node
			Ssh last = null;
			try {
				last = (Ssh) hops.toArray()[hops.size() - 1];
			} catch (ClassCastException e) {
				// TODO: Remove when the model will be stable
				throw new RemoteAccessException(
						"The last node must be a Node. It's a "
								+ hops.toArray()[hops.size() - 1].getClass()
										.getCanonicalName());
			}
			

			session = jsch.getSession(last.getLogin(),
					last.getServer(), last.getPort());
			session.setUserInfo(trustedUI);
			initProxiesPath(hops, session, trustedUI);

			session.connect();
			bufferedSessions.put(path, session);
		}
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setAgentForwarding(true);
		return channelExec;

	}

	/**
	 * Return a Session or Null if doesn't exists
	 * 
	 * @param path
	 * @return null if the session doesn't exist or are closed
	 */
	private Session getSession(Path path) {

		Session session = bufferedSessions.get(path);
		if (session == null || !session.isConnected()) {
			return null;
		}
		return session;
	}

	/**
	 * Create a ssh proxy in case of destination is referenced in configuration
	 * file FIXME: This workaround have been created to fix florent needed.
	 * 
	 * @return a proxy if
	 * @see Proxy
	 * 
	 */

	private NCProxy getShadowProxy(Resource destination, UserInfo ui) {
		NCProxy shadowProxy = null;

	
		List<Proxy> proxies = goDietConfiguration.getShadowedProxy();

		for (Proxy proxy : proxies) {
			if (proxy.getHost().equals(destination.getId())) {
				log.debug("find shadow proxy");
				shadowProxy = new NCProxy(proxy.getLogin(), proxy.getServer(),
						proxy.getPort(), jsch, ui);
			}
		}
		return shadowProxy;
	}

	/**
	 * Set the proxy on session
	 * 
	 * @param hops
	 * 
	 * @throws JSchException
	 * @throws RemoteAccessException
	 */
	private void initProxiesPath(LinkedHashSet<Hop> hops,
			Session session, UserInfo ui) throws JSchException,
			RemoteAccessException {

		Hop[] resources = hops.toArray(new Hop[0]);
		log.debug("hopsSize: " + resources.length);
		if (resources.length < 3) {
			return; // no need to create proxy. Path contains source and
					// destination which are in the same domain
		}
		NCProxy lastProxy = null;
		// i = 0 is the source. Don't create a proxy
		for (int i = 1; i <= resources.length - 2; i++) {
			
			Hop hop = resources[i];

			// FIXME
			NCProxy shadowProxy = getShadowProxy(hop.getDestination(), ui);
			if (shadowProxy != null) {
				if (lastProxy != null) {
					log.debug("Add proxy " + hop.getDestination().getId() + " to "
							+ lastProxy.getHost());
					shadowProxy.setProxy(lastProxy);
				}

				lastProxy = shadowProxy;
			}
			// END

			Ssh link = hop.getLink();
			NCProxy proxy = new NCProxy(link.getLogin(), link.getServer(),
					link.getPort(), jsch, ui);
			if (lastProxy != null) {
				log.debug("Add proxy " + link.getServer() + " to "
						+ lastProxy.getHost());
				proxy.setProxy(lastProxy);
			}

			lastProxy = proxy;

		}
		log.debug("Add proxy " + lastProxy.getHost() + " to session");
		session.setProxy(lastProxy);

	}

	public void debug(boolean activate) {
		 if (activate) {
		 com.jcraft.jsch.Logger jschLog = new com.jcraft.jsch.Logger() {
		
		 @Override
		 public void log(int level, String message) {
		 switch (level) {
		 case 0:
		 log.debug(message);
		 break;
		 case 1:
		 log.info(message);
		 break;
		 case 2:
		 log.warn(message);
		 break;
		 case 3:
		 log.error(message);
		 break;
		 case 4:
		 log.error("FATAL" + message);
		 break;
		
		 default:
		 break;
		 }
		
		 }
		
		 @Override
		 public boolean isEnabled(int level) {
		 return true;
		 }
		 };
		 JSch.setLogger(jschLog);
		 } else
		 JSch.setLogger(null);

	}

	public void addIdentity(SSHKeyManager managedKey)
			throws AddAuthentificationException {
		try {
			jsch.addIdentity(managedKey.getPrivKeyPath(), managedKey
					.getPubKeyPath(), managedKey.getPassword().getBytes());
			keybunch.add(managedKey);
		} catch (JSchException e) {

			throw new AddAuthentificationException("Unable to add key", e);
		}

	}

	public JSch getJsch() {
		return jsch;
	}

	public Set<SSHKeyManager> getKeyBunch() {
		return keybunch;
	}

	public void removeIdentity(SSHKeyManager sshkey)
			throws RemoveAuthentificationException {
		if (sshkey == null) {
			log.error("Try to remove an empty key");
			return;
		}
		try {
			jsch.removeIdentity(sshkey.getPrivKeyPath());
			keybunch.remove(sshkey);
		} catch (JSchException e) {
			throw new RemoveAuthentificationException("Unable to remove key", e);
		}
	}
}
