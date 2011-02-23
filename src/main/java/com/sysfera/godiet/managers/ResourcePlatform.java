package com.sysfera.godiet.managers;

import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Model.deprecated.Domain;
import com.sysfera.godiet.Model.deprecated.Link;
import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;
import com.sysfera.godiet.Model.physicalresources.deprecated.GatewayResource;
import com.sysfera.godiet.Model.physicalresources.deprecated.StorageResource;

public interface ResourcePlatform {

	public abstract ComputeResource getComputeResource(String name);

	public abstract StorageResource getStorageResource(String name);

	public abstract int getStorageResourceCount();

	public abstract List<StorageResource> getUsedStorageResources();

	/**
	 * Return the gateway give by his name
	 * 
	 * @param attribute
	 * @return The gateway if exist, Null otherwise.
	 */
	public abstract GatewayResource getGateway(String attribute);

	/**
	 * Return the domain give by his name
	 * 
	 * @param attribute
	 * @return The domain if exist, Null otherwise.
	 */
	public abstract Domain getDomain(String domainName);

	public abstract Set<Domain> getDomains();

	public abstract Set<Link> getLinks();

}