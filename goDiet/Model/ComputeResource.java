/*
 * ComputeResource.java
 *
 * Created on April 20, 2004, 10:56 AM
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class ComputeResource extends Resources {
    private StorageResource storRes = null;
    private String envPath = null;
    private String envLdLibraryPath = null;
    
    /** Creates a new instance of ComputeResource */
    public ComputeResource(String name, StorageResource storRes) {
        super(name);
        this.storRes = storRes;
    }
    
    public void setEnvPath(String path){
        this.envPath = path;
    }
    public String getEnvPath(){
        return this.envPath;
    }
   
    public void setEnvLdLibraryPath(String ldLibPath){
        this.envLdLibraryPath = ldLibPath;
    }
    public String getEnvLdLibraryPath(){
        return this.envLdLibraryPath;
    }
    
    public StorageResource getStorageResource(){
        return this.storRes;
    }
    
    
    
}