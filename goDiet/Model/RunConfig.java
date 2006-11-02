/*@GODIET_LICENSE*/
/*
 * RunConfig.java
 *
 * Created on May 13, 2004, 11:00 AM
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class RunConfig {
    public int     debugLevel = 1;
    public boolean saveStdOut = true;
    public boolean saveStdErr = true;
    public boolean useUniqueDirs = true;
    public String localScratchBase = "/tmp";
    public String localScratch = null;
    public boolean localScratchReady = false;
    public String runLabel = "";
    
    /** Creates a new instance of RunConfig */
    public RunConfig() {
        
    }
    
    public void setDebugLevel(int debugLevel){
        this.debugLevel = debugLevel;
    }
    public int getDebugLevel(){
        return this.debugLevel;
    }
    
    public void setLocalScratchBase(String localScratchBase){
        this.localScratchBase = localScratchBase;
    }
    public String getLocalScratchBase(){
        return this.localScratchBase;
    }
            
    public void setRunLabel(String label){
        this.runLabel = label;
    }
    public String getRunLabel(){
        return this.runLabel;
    }
    
    public void setLocalScratch(String scratchDir){
        this.localScratch = scratchDir;
    }
    public String getLocalScratch(){
        return this.localScratch;
    }

    public void setLocalScratchReady(boolean flag){
        this.localScratchReady = flag;
    }
    public boolean isLocalScratchReady(){
        return this.localScratchReady;
    } 
}
