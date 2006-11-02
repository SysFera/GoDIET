/*@GODIET_LICENSE*/
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
    /** Config items.  Never change if jobs are active (jobCount > 0) */
    private ComputeCollection myCollection;
    private String endPointContact = null;
    private int begAllowedPorts = -1;
    private int endAllowedPorts = -1;

    /** Runtime-related items. */
    private int jobCount = 0;  // Number of items launched on this machine
    private int allocatedPorts = 0;

    /** Creates a new instance of ComputeResource */
    public ComputeResource(String name, ComputeCollection collection) {
        super(name);
        this.myCollection = collection;
    }
    
    public ComputeCollection getCollection(){
        return this.myCollection;
    }
    
    /** Config-related methods */
    public void setEndPointContact(String endPoint) {
        this.endPointContact = endPoint;
    }
    public String getEndPointContact(){
        return this.endPointContact;
    }

    public void setBegAllowedPorts(int begPort) {
        this.begAllowedPorts = begPort;
    }
    public int getBegAllowedPorts(){
        return this.begAllowedPorts;
    }

    public void setEndAllowedPorts(int endPort) {
        this.endAllowedPorts = endPort;
    }
    public int getEndAllowedPorts(){
        return this.endAllowedPorts;
    }
        /*public Exception throwJobsExistException() throws Exception {
            Exception x = new Exception("Machine " + outerThis.getName() +
                    " has " + outerThis.jobCount + " active jobs.  Can not" +
                    " change the config.");
            throw (x);
        }*/  
    
    /** Runtime-related methods */
    public void incJobCount(){
        this.jobCount++;
        this.myCollection.incJobCount();
        this.myCollection.getStorageResource().incJobCount();
    }
    public void decJobCount(){
        this.jobCount--;
        this.myCollection.decJobCount();
        this.myCollection.getStorageResource().decJobCount();
    }
    public int getJobCount(){
        return this.jobCount;
    }
                 
    /** If user specified constraints on ports to be used as end points, 
     * the next unallocated port is allocated and returned.  If the
     * user did not specify constraints or all ports have been allocated,
     * -1 is returned. */
    public int allocateAllowedPort(){
        // Check if we even need to use port
        if(this.begAllowedPorts < 0){
            return -1;
        }
        // In case a maximum range has been given, check we haven't passed it
        if( (this.endAllowedPorts > 0) &&
            ((this.begAllowedPorts + this.allocatedPorts) >= 
                    this.endAllowedPorts) ) {
           System.err.println("Port allocation on " + 
                this.getName() + " failed.  All ports [" + 
                this.begAllowedPorts + "," + this.endAllowedPorts + 
                "] already allocated.");
           return -1;
        }

        int newPort = this.begAllowedPorts + this.allocatedPorts;
        this.allocatedPorts++;
        return newPort;
    }   
}