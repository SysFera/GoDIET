/*
 * StorageResource.java
 *
 * Created on April 20, 2004, 10:56 AM
 */

package goDiet.Model;
import java.util.Vector;
/**
 *
 * @author  hdail
 */
public class StorageResource extends Resources {
    /** Config-related items */
    /** These items should never be changed if jobCount > 0 */
    private String scratchBase = null;

    /** Run-time related items */
    private int jobCount = 0;  // Number of jobs using this disk space
    private String runLabel = null; // necessary?
    private boolean scratchReady = false;
    private boolean omniCfgReady = false;
      
    
    /** Creates a new instance of StorageResource */
    public StorageResource(String name){
        super(name);    
    }
    
    /** config-related methods */
    
    public void setScratchBase(String scratchBase){
        this.scratchBase = scratchBase;
    }
    public String getScratchBase(){
        return this.scratchBase;
    } 
    
    /** Run-time related methods. */
    public void incJobCount(){
        this.jobCount++;
    }    
    public void decJobCount(){
        this.jobCount--;
    } 
    public int getJobCount(){
        return this.jobCount;
    }
    public void setRunLabel(String runLabel){
        this.runLabel = runLabel;
    }
    public String getRunLabel(){
        return this.runLabel;
    }

    public void setScratchReady(boolean flag){
        this.scratchReady = flag;
    }
    public boolean getScratchReady(){
        return this.scratchReady;
    }

    public void setOmniCfgReady(boolean flag){
        this.omniCfgReady = flag;
    }
    public boolean getOmniCfgReady(){
        return this.omniCfgReady;
    }    
}
