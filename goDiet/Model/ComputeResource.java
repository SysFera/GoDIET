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
    private int allocatedPorts = 0;
    
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
    public int allocateEndPointPort(){
        // Check if we even need to use port
        if(this.endPntStartPort < 0){
            return -1;
        }
        // In case a maximum range has been given, check we haven't passed it
        if( (this.endPntEndPort > 0) &&
            ((this.endPntStartPort + this.allocatedPorts) >= 
                    this.endPntEndPort) ) {
           System.err.println("Port allocation on " + 
                this.getName() + " failed.  All ports [" + this.endPntStartPort
                + "," + this.endPntEndPort + "] already allocated.");
           return -1;
        }
        
        int newPort = this.endPntStartPort + this.allocatedPorts;
        this.allocatedPorts++;
        return newPort;
    }
}