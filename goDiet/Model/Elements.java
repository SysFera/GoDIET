/*
 * Elements.java
 *
 * Created on 13 avril 2004, 14:44
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class Elements extends java.util.Observable {
    // Required configuration options
    private String name = null;
    private ComputeResource compHost = null;
    private String binary = null;
    
    // Optional configuration options
    private int traceLevel = -1;
    private boolean haveTraceLevel = false;
    private boolean haveLogService = false;
    private boolean useLogService = false;
    private boolean useDietStats = false;
    private String cfgFileName = null;
    private boolean haveCfgFileName = false;

    private LaunchInfo launchInfo = null;
    
    /* Constructor for Elements.  Once an Element is created,
       the Name can not be changed. */
    public Elements(String name,ComputeResource compRes,String binary) {
        this.name = name;
        this.compHost = compRes;
        this.binary = binary;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {return this.name;}

    public void setComputeResource(ComputeResource compRes){
        this.compHost = compRes;
    }
    public ComputeResource getComputeResource(){
        return this.compHost;
    }

    public void setBinary(String binary){
        this.binary=binary;
    }
    public String getBinary() {return this.binary;}
    
    public void setTraceLevel(int traceLevel){
        this.traceLevel = traceLevel;
        this.haveTraceLevel = true;
    }
    public boolean isTraceLevelSet() {return this.haveTraceLevel;}
    public int getTraceLevel() {return this.traceLevel;}
 
    public void setCfgFileName(String fileName){
        this.cfgFileName = fileName;
        this.haveCfgFileName = true;
    }
    public boolean isCfgFileNameSet() {return this.haveCfgFileName;}
    public String getCfgFileName() {return this.cfgFileName;}
  
    public boolean isLogServiceSet() {return this.haveLogService;}
    public boolean getLogService() {return this.useLogService;}
   
    public void setUseDietStats(boolean flag){
        this.useDietStats = flag;
    }
    public boolean getUseDietStats(){
        return this.useDietStats;
    }
    
    public void setLaunchInfo(LaunchInfo launchInfo){
        this.launchInfo = launchInfo;        
    }
    public LaunchInfo getLaunchInfo(){
        return this.launchInfo;
    }
    public boolean isRunning(){
        return this.launchInfo.running;
    }
}
