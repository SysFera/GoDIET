/*
 * OmniNames.java
 *
 * Created on 26 May 2004, 14:44
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class LogCentral extends Services {
    // Does user want guided launch?
    private boolean useLogToGuideLaunch = false;
    
    // Did we connect successfully?  Will never be true unless useLogToGuideLaunch
    // is also true.
    private boolean logCentralConnected = false;

    public LogCentral(String name, ComputeResource compRes, String binary) {
        super(name, compRes, binary);
        this.useLogToGuideLaunch = true;
    }
    
    public LogCentral(String name, ComputeResource compRes, String binary, 
            boolean useLogToGuideLaunch) {
        super(name, compRes, binary);
        this.useLogToGuideLaunch = useLogToGuideLaunch;
    }
    
    public boolean useLogToGuideLaunch(){
        return this.useLogToGuideLaunch;
    }
    public void setUseLogToGuideLaunch(boolean flag){
        this.useLogToGuideLaunch = flag;
    }
    public boolean logCentralConnected(){
        return this.logCentralConnected;
    }
    public void setLogCentralConnected(boolean flag){
        this.logCentralConnected = flag;
    }
}
