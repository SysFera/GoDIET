/*
 * StorageResource.java
 *
 * Created on April 20, 2004, 10:56 AM
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class StorageResource extends Resources {
    private String scratchBase = null;
    private String runLabel = null;
    private boolean scratchReady = false;
    //private boolean omniOrbCfgStaged = false;
    
    /** Creates a new instance of StorageResource */
    public StorageResource(String name, String scratchBase) {
        super(name);
        this.scratchBase = scratchBase;
    }
    
    public void setScratchBase(String scratchBase){
        this.scratchBase = scratchBase;
    }
    public String getScratchBase() {
        return this.scratchBase;
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
    public boolean isScratchReady(){
        return this.scratchReady;
    }
    /*
    public void setOmniOrbCfgStaged(boolean flag){
        this.omniOrbCfgStaged = flag;
    }
    public boolean getOmniOrbCfgStaged(){
        return this.omniOrbCfgStaged;
    }*/
}
