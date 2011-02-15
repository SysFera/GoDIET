/*@GODIET_LICENSE*/
/*
 * Defaults.java
 *
 * Created on June 19, 2004, 11:00 AM
 */

package com.sysfera.godiet;

/**
 *
 * @author  hdail
 */
public class Defaults {      
    /** Defaults for configuration of DIET hierarchy elements */
    public static final int TRACE_LEVEL = 1;
    public static final boolean USE_LOG_SERVICE = false;
    public static final boolean USE_DIET_STATS = false;

    /** Enumeration-like variables for deployment states */
    public static final int DEPLOY_beg = -1;        // internal use only
    public static final int DEPLOY_NONE = 0;
    public static final int DEPLOY_LAUNCHING = 1;
    public static final int DEPLOY_ACTIVE = 2;
    public static final int DEPLOY_STOPPING = 3;
    public static final int DEPLOY_INACTIVE = 4;
    public static final int DEPLOY_end = 5;         // internal use only
    public static final String[] deployStateStrings = {"None", "Launching", 
        "Active", "Stopping", "Inactive"};
  
    /** Enumeration-like variables for GoDIET's view on element's status */
    private static final int LAUNCH_STATE_beg = -1;   // internal use only
    public static final int LAUNCH_STATE_NONE = 0;
    public static final int LAUNCH_STATE_FAILED = 1;
    public static final int LAUNCH_STATE_CONFUSED = 2;
    public static final int LAUNCH_STATE_RUNNING = 3;
    public static final int LAUNCH_STATE_STOPPED = 4;
    private static final int LAUNCH_STATE_end = 5;  // internal use only
    private static final String[] launchStateStrings = 
        {"none", "failed", "confused","running", "stopped"};
    
    /** Enumeration-like variables for LogCentral's view on element's status */
    private static final int LOG_STATE_beg = -1;    // internal use only
    public static final int LOG_STATE_NONE = 0;
    public static final int LOG_STATE_CONFUSED = 1;
    public static final int LOG_STATE_RUNNING = 2;
    public static final int LOG_STATE_STOPPED = 3;
    private static final int LOG_STATE_end = 4;
    private static final String[] logStateStrings = 
        {"none", "confused","running", "stopped"};
        
    /** Creates a new instance of Defaults */
    public Defaults() {
        
    }
    
    /** Implementation of enum-like DEPLOY_STATE */
    public static boolean deployStateExists(int deployState){
        if((deployState > LOG_STATE_beg) &&
           (deployState < LOG_STATE_end)){
           return true;
        }
        return false;
    }
    public static String getDeployStateString(int deployState){
        if(deployStateExists(deployState)){
           return deployStateStrings[deployState];
        } else {
            return null;
        }     
    } 
    
    /** Implementation of enum-like LAUNCH_STATE */
    public static boolean launchStateExists(int launchState){
        if((launchState > LAUNCH_STATE_beg) &&
           (launchState < LAUNCH_STATE_end)){
           return true;
        }
        return false;
    }
    public static String getLaunchStateString(int launchState){
        if(launchStateExists(launchState)){
           return launchStateStrings[launchState];
        } else {
            return null;
        }     
    }
    
    /** Implementation of enum-like LOG_STATE */
    public static boolean logStateExists(int logState){
        if((logState > LOG_STATE_beg) &&
           (logState < LOG_STATE_end)){
           return true;
        }
        return false;
    }
    public static String getLogStateString(int logState){
        if(logStateExists(logState)){
           return logStateStrings[logState];
        } else {
            return null;
        }     
    } 
}
