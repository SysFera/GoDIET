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
    private String endPointContact = null;
    private int endPntStartPort = -1;
    private int endPntEndPort = -1;
    
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
    
    public void setEndPointContact(String endPoint){
        this.endPointContact = endPoint;
    }
    public String getEndPointContact(){
        return this.endPointContact;
    }
    
    public void setEndPointRange(int startPort, int endPort){
        this.endPntStartPort = startPort;
        this.endPntEndPort = endPort;
    }
    public int getEndPointStartPort(){
        return this.endPntStartPort;
    }
    public int getEndPointEndPort(){
        return this.endPntEndPort;
    }
}