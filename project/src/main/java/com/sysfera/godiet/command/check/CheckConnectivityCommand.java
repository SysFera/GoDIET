package com.sysfera.godiet.command.check;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.Command;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.generics.PathException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.SoftwareManager;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;

/**
 * First check if all the resources concerned by diet deployment are
 * reachable.
 * TODO: Finish to open a channel on this resources
 * 
 * @author phi
 * 
 */
public class CheckConnectivityCommand implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private InfrastructureManager platform;
	private DietManager diet;

	private Resource fromResource;

	@Override
	public String getDescription() {
		return "First check if all the resources concerned by diet deployment are reachable. TODO: Finish to open a channel on this resources";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (diet == null || platform == null || fromResource == null)
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		pexecute();

	}

	/*
	 * Business code here without parameters checking
	 */
	void pexecute() throws CommandExecutionException {
		// Get all resources concerned by the diet deployment
		Set<Resource> concernedResources = new HashSet<Resource>();
		List<SoftwareManager<? extends Software>> dietResourcesManaged = this.diet
				.getAllManagedSoftware();
		for (SoftwareManager<? extends Software> softwareManaged : dietResourcesManaged) {
			concernedResources.add(softwareManaged.getPluggedOn());
		}
		//Check if a path exist with all concerned resources
		for (Resource toResource : concernedResources) {
			try {
				platform.findPath(fromResource, toResource);
			} catch (PathException e) {
				throw new CommandExecutionException("Unable to find path from "
						+ fromResource.getId() + " to " + toResource.getId(), e);
			}
		}

		//Open a channel on all resources
		

	}

	/**
	 * Set the physical platform on which diet will be deployed
	 * 
	 * @param platform
	 */
	public void setPlatform(InfrastructureManager platform) {
		this.platform = platform;
	}

	/**
	 * Set the diet deployment description
	 * 
	 * @param diet
	 */
	public void setDiet(DietManager diet) {
		this.diet = diet;
	}

	/**
	 * Set the resource from where the deployment will be done
	 * 
	 * @param fromResource
	 */
	public void setFromResource(Resource fromResource) {
		this.fromResource = fromResource;
	}

}
