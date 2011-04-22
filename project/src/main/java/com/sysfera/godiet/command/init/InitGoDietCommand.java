package com.sysfera.godiet.command.init;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.model.generated.GoDietConfiguration;
import com.sysfera.godiet.model.generated.User.Ssh.Key;
import com.sysfera.godiet.remote.RemoteAccess;

/**
 * Command to initialize Godiet. TODO: Currently only register keys.
 * 
 * @author phi
 * 
 */
public class InitGoDietCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private GoDietConfiguration configuration;

	private RemoteAccess remoteAccess;

	@Override
	public String getDescription() {
		return "Initialize remote access with the ";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (remoteAccess == null || configuration == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");

		}
		
		List<Key> keys = configuration.getUser().getSsh().getKey();
		for (Key key : keys) {
			String privateKeyPath = key.getPath();
			String publicKeyPath = key.getPathPub();
			
//			remoteAccess.addItentity(privateKeyPath, publicKeyPath, passphrase);
		}
		
	}

}
