/*@GODIET_LICENSE*/
/*
 * ResourcePlatform.java
 *
 * Created on 20 avril 2004, 14:51
 */

package com.sysfera.godiet.Model.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sysfera.godiet.Model.ComputeCollection;
import com.sysfera.godiet.Model.Domain;
import com.sysfera.godiet.Model.Link;
import com.sysfera.godiet.Model.physicalresources.ComputeResource;
import com.sysfera.godiet.Model.physicalresources.GatewayResource;
import com.sysfera.godiet.Model.physicalresources.StorageResource;

/**
 *
 * @author  hdail
 */
public class ResourcePlatformManager {
    /** config=related items
     * These should not be changed while jobs are running on the platform */
    private Set<ComputeCollection> computeCollections;
    private Set<Domain> domains;
    private List<StorageResource> storageResources;
	private Set<Link> links;
       
    /** Creates a new instance of DietPlatform */
    public ResourcePlatformManager() {
        this.computeCollections = new HashSet<ComputeCollection>();
        this.storageResources = new ArrayList<StorageResource>();
        this.domains = new HashSet<Domain>();
        this.links = new HashSet<Link>();
    }
	public void addDomain(Domain domain) {
		this.domains.add(domain);

	}
    public void addComputeCollection(ComputeCollection newColl){   
       
        this.computeCollections.add(newColl);        
//        setChanged();
//        notifyObservers(new AddElementsEvent(newColl));
//        clearChanged();
        
    }
       
    
    public ComputeResource getComputeResource(String name){
        ComputeResource res = null;
        ComputeCollection coll = null;
        for(Iterator it = computeCollections.iterator(); it.hasNext();){
            coll = (ComputeCollection) it.next();
            res = coll.getComputeResource(name);
            if(res != null){
                return res;
            }
        }
        return null;
    }
    
    public void addStorageResource(StorageResource newStore){
        if( this.getStorageResource(newStore.getName()) != null){
            System.err.println("Resource Platform: Storage Resource " + 
                newStore.getName() + " already exists. Addition refused.");
            return;
        }
        this.storageResources.add(newStore);        
//        setChanged();
//        notifyObservers(new AddElementsEvent(newStore));
//        clearChanged();
    }
     public StorageResource getStorageResource(String name) {
        StorageResource resource = null;
        for( int i = 0; i < storageResources.size(); i++) {
            resource = (StorageResource)storageResources.get(i);
            if(name.equals(resource.getName())) {
                return resource;
            }
        }
        // if not found, return null
        return null;
    }
    public int getStorageResourceCount(){
        return this.storageResources.size();
    }
            
  
    
    public List getUsedStorageResources(){        
        List usedStorageResouces = new ArrayList();
        for (Iterator it= storageResources.iterator();it.hasNext();){
            StorageResource stRes = (StorageResource)it.next();
            if (stRes.isUsed())
                usedStorageResouces.add(stRes);
        }
        return usedStorageResouces;
    }
    /**
	 * Return the gateway give by his name
	 * 
	 * @param attribute
	 * @return The gateway if exist, Null otherwise.
	 */
	public GatewayResource getGateway(String attribute) {
		if (domains != null) {
			for (Domain domain : domains) {
				Set<GatewayResource> gateways = domain.getGateways();
				if (gateways != null) {
					for (GatewayResource gateway : gateways) {
						if (gateway.getName().equals(attribute))
							return gateway;
					}
				}
			}
		}

		return null;
	}
	/**
	 * Return the domain give by his name
	 * 
	 * @param attribute
	 * @return The domain if exist, Null otherwise.
	 */
	public Domain getDomain(String domainName) {

		for (Domain domain : domains) {
			if (domain.getName().equals(domainName)) {
				return domain;
			}
		}
		return null;
	}

	public Set<Domain> getDomains() {
		return domains;
	}
	public void addLink(Link link) {
		this.links.add(link);
		
	}
	public Set<Link> getLinks() {
		return links;
	}

}
