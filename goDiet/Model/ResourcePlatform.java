/*
 * ResourcePlatform.java
 *
 * Created on 20 avril 2004, 14:51
 */

package goDiet.Model;

import goDiet.Events.*;

/**
 *
 * @author  hdail
 */
public class ResourcePlatform extends java.util.Observable {
    private java.util.Vector computeResources;
    private java.util.Vector storageResources;
    
    private String localScratchBase = null;     // base dir for local tmp space
    private String runLabel = null;             // Unique label for all scratch space
    private boolean localScratchReady = false;  // Has dir been created?
       
    /** Creates a new instance of DietPlatform */
    public ResourcePlatform() {
        this.computeResources = new java.util.Vector();
        this.storageResources = new java.util.Vector();
    }
    
    public void setLocalScratchBase(String localScratchBase){
        this.localScratchBase = localScratchBase;
    }
    
    public String getLocalScratchBase(){
        return this.localScratchBase;
    }
    
    public void setRunLabel(String runLabel){
        this.runLabel = runLabel;
    }
    
    public String getRunLabel(){
        return this.runLabel;
    }
    
    public void setLocalScratchReady(boolean flag){
        this.localScratchReady = flag;
    }
    
    public boolean isLocalScratchReady(){
        return this.localScratchReady;
    }
    
    public void addComputeResource(ComputeResource newComp){    
        if( this.getComputeResource(newComp.getName()) != null){
            System.err.println("Resource Platform: Compute Resource " + 
                newComp.getName() + "already exists. Addition refused.");
            return;
        }
        this.computeResources.add(newComp);
        setChanged();
        notifyObservers(new AddElementsEvent(newComp));
        clearChanged();
    }
    public void addStorageResource(StorageResource newStore){
        if( this.getStorageResource(newStore.getName()) != null){
            System.err.println("Resource Platform: Storage Resource " + 
                newStore.getName() + " already exists. Addition refused.");
            return;
        }
        this.storageResources.add(newStore);
        setChanged();
        notifyObservers(new AddElementsEvent(newStore));
        clearChanged();
    }
    
    public int getComputeResourceCount(){
        return this.computeResources.size();
    }
    
    public ComputeResource getComputeResource(String name) {
        ComputeResource resource = null;
        for( int i = 0; i < computeResources.size(); i++) {
            resource = (ComputeResource)computeResources.elementAt(i);
            if(name.equals(resource.getName())) {
                return resource;
            }
        }
        // if not found, return null
        return null;
    }
    
    public java.util.Vector getComputeResources() {
        return this.computeResources;
    }
    
    public int getStorageResourceCount(){
        return this.storageResources.size();
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
    
    public java.util.Vector getStorageResources() {
        return this.storageResources;
    }
    
    public static void main(String args[]){
        System.out.println("Running ResourcePlatform unit test.");
        ResourcePlatform platform = new ResourcePlatform();
        StorageResource storRes = new StorageResource("localDisk", "/tmp/diet_scratch");
        ComputeResource compRes = new ComputeResource("localHost", storRes);
        
        platform.addStorageResource(storRes);
        platform.addComputeResource(compRes);
        
        ComputeResource newComp = platform.getComputeResource("localHost");
        StorageResource newStor = platform.getStorageResource("localDisk");
        
        if( (newComp == null) || (newStor == null) ) {
            System.err.println("ResourcePlatform unit test failed. " +
                "Did not find expected objects.");
            return;
        }
        
        if( (newStor.getName() != "localDisk") ||
            (newComp.getName() != "localHost")) {
            System.err.println("ResourcePlatform unit test failed. " + 
                "Name retrieval gave unexpected answers.");
            return;
        }
        
        System.out.println("ResourcePlatform unit test succeeded.");
    }
}
