package com.sysfera.godiet.remote;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sysfera.godiet.exceptions.RemoteAccessException;
import com.sysfera.godiet.remote.ssh.NCProxy;
import com.sysfera.godiet.remote.ssh.RemoteAccessJschImpl;
import com.sysfera.godiet.remote.ssh.TrustedUserInfo;

public class RemoteAccessJschImplIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testLocalhostScp() {

		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.jschDebug(true);
		String testCaseFile = "fakeuser/id_rsa";
		String urlFile = getClass().getClassLoader().getResource(testCaseFile)
				.getFile();

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + testCaseFile);

		remoteJsch.addKey(urlFile, null, "godiet");
		try {
			remoteJsch.run("ls", "godiet", "localhost", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine. Reason"
					+ e.getMessage());
		}
	}

	@Test
	public void testProxy() {
		RemoteAccessJschImpl remoteJsch = new RemoteAccessJschImpl();
		remoteJsch.jschDebug(true);

		JSch jsch = remoteJsch.getJsch();
		TrustedUserInfo tui = new TrustedUserInfo();

		String testCaseFile = "fakeuser/id_rsa";
		String urlFile = getClass().getClassLoader().getResource(testCaseFile)
				.getFile();

		if (urlFile == null)
			Assert.fail("Unable to load ssh key" + testCaseFile);

		remoteJsch.addKey(urlFile, null, "godiet");
		remoteJsch.addKey("/home/phi/.ssh/id_rsa", null, "mondedemerde0");

		NCProxy testbed1 = null;
		try {
			//kevin.connect(null, "140.77.166.19", 22, 0);
			Session session = jsch.getSession("godiet", "localhost", 40022);
			testbed1 = new NCProxy("pmartinez", "140.77.166.19", 22,jsch,tui);
			NCProxy proxylocalhost = new NCProxy("phi", "localhost", 22, jsch, tui);
			NCProxy proxylocalhost2 = new NCProxy("phi", "localhost", 22, jsch, tui);
			proxylocalhost.setProxy(proxylocalhost2);
			testbed1.setProxy(proxylocalhost);
			session.setUserInfo(tui);
			session.setProxy(testbed1);
			session.connect();
			
		
			run(session,"ls");
			//remoteJsch.run("ls", "godiet", "testBed1", 22);
		} catch (RemoteAccessException e) {
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine. Cause:"
					+ e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail("Unable access to TestBed1 machine. Cause:"
					+ e.getMessage());
		}finally{
		//	if(testbed1 != null)testbed1.close();
		}
	}

	private void run(Session session, String command) throws RemoteAccessException {
		Channel channel = null;
		try {

			// session = jsch.getSession(user, host, port);
			//
			// session.setUserInfo(new);
			// session.connect();

			channel = session.openChannel("exec");
			// TODO: Decor with tee to write on standard err and out stream and
			// alse remote scratch file
			((ChannelExec) channel).setCommand(command);

			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					log.debug(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {

					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					log.error("Runtime error. Thread Interupted.", e);
					throw new RemoteAccessException("Thread interupted", e);
				}
			}
			if (channel.getExitStatus() < 0)
				throw new RemoteAccessException("SSH connection close abruptly");

		} catch (Exception e) {
			throw new RemoteAccessException("Unable to SSH connect. Command: "
					+ command, e);
		} finally {
			try {
				if (channel != null)
					channel.disconnect();
				if (session != null)
					session.disconnect();
			} catch (Exception e) {
				log.error("SSH disconnect error", e);
			}
		}

	}
}
