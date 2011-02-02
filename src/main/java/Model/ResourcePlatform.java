/*@GODIET_LICENSE*/
/*
 * ResourcePlatform.java
 *
 * Created on 20 avril 2004, 14:51
 */

package goDiet.Model;

import goDiet.Events.*;

import java.util.*;

/**
 *
 * @author  hdail
 */
public class ResourcePlatform /*extends java.util.Observable*/ {
    /** config=related items
     * These should not be changed while jobs are running on the platform */
    private java.util.Vector computeCollections;
    private java.util.Vector storageResources;
       
    /** Creates a new instance of DietPlatform */
    public ResourcePlatform() {
        this.computeCollections = new java.util.Vector();
        this.storageResources = new java.util.Vector();
    }
    
    public void addComputeCollection(ComputeCollection newColl){   
        if( this.getComputeCollection(newColl.getName()) != null){
            System.err.println("Resource Platform: Compute Collection " + 
                newColl.getName() + " already exists. Addition refused.");
            return;
        }
        this.computeCollections.add(newColl);        
//        setChanged();
//        notifyObservers(new AddElementsEvent(newColl));
//        clearChanged();
        
    }
    public int getComputeCollectionCount(){
        return this.computeCollections.size();
    }
    public ComputeCollection getComputeCollection(String name) {
        ComputeCollection coll = null;
        for( Iterator it = computeCollections.iterator(); it.hasNext();){
            coll = (ComputeCollection) it.next();
            if(name.equals(coll.getName())) {
                return coll;
            }
        }
        // if not found, return null
        return null;
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
            resource = (StorageResource)storageResources.elementAt(i);
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
            
    public static void main(String args[]){
        System.out.println("Running ResourcePlatform unit test.");
        ResourcePlatform platform = new ResourcePlatform();
        StorageResource storRes = new StorageResource("localDisk");
        storRes.setScratchBase("/tmp/diet_scratch");
        
        ComputeCollection coll = new ComputeCollection("local");
        ComputeResource compRes = new ComputeResource("localHost", coll); 

        coll.addStorageResource(storRes);
        coll.addComputeResource(compRes);
        
        platform.addStorageResource(storRes);
        platform.addComputeCollection(coll);
        
        ComputeCollection newColl = platform.getComputeCollection("local");
        ComputeResource newComp = newColl.getComputeResource("localHost");
        StorageResource newStor = newColl.getStorageResource();
        
        if( (newComp == null) || (newStor == null) || (newColl == null)) {
            System.err.println("ResourcePlatform unit test failed. " +
                "Did not find expected objects.");
            return;
        }
        
        if( (newColl.getName() != "local") ||
            (newStor.getName() != "localDisk") ||
            (newComp.getName() != "localHost")) {
            System.err.println("ResourcePlatform unit test failed. " + 
                "Name retrieval gave unexpected answers.");
            return;
        }
        
        System.out.println("ResourcePlatform unit test succeeded.");
    }
    
    public Vector getUsedStorageResources(){        
        Vector usedStorageResouces = new Vector();
        for (Iterator it= storageResources.iterator();it.hasNext();){
            StorageResource stRes = (StorageResource)it.next();
            if (stRes.isUsed())
                usedStorageResouces.add(stRes);
        }
        return usedStorageResouces;
    }
    public Vector getUsedComputeResources(){
        Vector usedComputeResouces = new Vector();
        for (Iterator it1= computeCollections.iterator();it1.hasNext();){
            ComputeCollection cpColl = (ComputeCollection)it1.next();            
            for (Iterator it2= cpColl.getComputeResources().iterator();it2.hasNext();){
                ComputeResource cpRes = (ComputeResource)it2.next();
                if (cpRes.isUsed())
                    usedComputeResouces.add(cpRes);
            }
        }
        return usedComputeResouces;
    }
}
