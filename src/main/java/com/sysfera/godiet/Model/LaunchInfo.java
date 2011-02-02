/*@GODIET_LICENSE*/
/*
 * LaunchInfo.java
 *
 * Created on May 19, 2004, 11:00 AM
 */

package com.sysfera.godiet.Model;

/**
 *
 * @author  hdail
 */
public class LaunchInfo {
    private int launchState = com.sysfera.godiet.Defaults.LAUNCH_STATE_NONE;
    private int logState = com.sysfera.godiet.Defaults.LOG_STATE_NONE;
    private int pid = -1;
    private String[] commandArray = null; // undecided if needed?
    private String stdOutOfLaunch = null;   // undecided how to use?
    private String stdErrOfLaunch = null;     // undecided how to use?

    /** Creates a new instance of LaunchInfo */
    public LaunchInfo() {
    }
    
    public void clearLastLaunch(){
        this.pid = -1;
        this.launchState = com.sysfera.godiet.Defaults.LAUNCH_STATE_NONE;
        this.logState = com.sysfera.godiet.Defaults.LOG_STATE_NONE;
        this.commandArray = null;
        this.stdOutOfLaunch = null;
        this.stdErrOfLaunch = null;
    }
    
    public void setLaunchState(int launchState){
        if(com.sysfera.godiet.Defaults.launchStateExists(launchState)){
            this.launchState = launchState;
        } else {
            System.err.println("Ignoring attempt to set unknown launchState "
                + launchState);
        }
    }
    public int getLaunchState(){
        return this.launchState;
    }

    public void setLogState(int logState){
        if(com.sysfera.godiet.Defaults.logStateExists(logState)){
            this.logState = logState;
        } else {
            System.err.println("Ignoring attempt to set unknown logState "
                + logState);
        }
    }
    public int getLogState(){
        return this.logState;
    } 

    public void setPID(int pid){
        this.pid = pid;
    }
    public int getPID(){
        return this.pid;
    }    
    
    public void setLastLaunchCommand(String [] commands){
        this.commandArray = commands;
    }
    public String[] getLastLaunchCommand(){
        return this.commandArray;
    }
    
    public void setLastLaunchStdOut(String stdOut){
        this.stdOutOfLaunch = stdOut;
    }
    public String getLastLaunchStdOut(){
        return this.stdOutOfLaunch;
    }
    
    public void setLastLaunchStdErr(String stdErr){
        this.stdErrOfLaunch = stdErr;
    }
    public String getLastLaunchStdErr(){
        return this.stdErrOfLaunch;
    }
}
