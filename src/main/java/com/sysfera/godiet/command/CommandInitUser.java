package com.sysfera.godiet.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.model.xml.generated.GoDietConfiguration;

public class CommandInitUser implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private GoDietConfiguration configuration;

	@Override
	public String getDescription() {
		return "Init user configuration. Load ssh keys ...";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (configuration == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}

	}
	
	public void setConfiguration(GoDietConfiguration configuration) {
		this.configuration = configuration;
	}

}
