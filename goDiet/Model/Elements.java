/*
 * Elements.java
 *
 * Created on 13 avril 2004, 14:44
 */

package goDiet.Model;
import goDiet.Defaults;
/**
 *
 * @author  hdail
 */
public class Elements extends java.util.Observable {
    /** Config-related items.  These should never be changed if the element
     * is currently running */
    private String          name =          null;
    private ComputeResource compHost =      null;
    private String          binaryName =    null;
    private int             traceLevel =    goDiet.Defaults.TRACE_LEVEL;
    private boolean         useLogService = goDiet.Defaults.USE_LOG_SERVICE;
    private boolean         useDietStats =  goDiet.Defaults.USE_DIET_STATS;
    private String          cfgFileName =   null;
   
    /** Run-related items */
    private LaunchInfo launchInfo = null;
    
    /* Constructor for Elements. */
    public Elements(String name, ComputeResource compRes, String binary){
        this.name = name;
        this.compHost = compRes;
        this.binaryName = binary;
        this.cfgFileName = this.getName() + ".cfg";
        this.launchInfo = new LaunchInfo();
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

    public void setBinaryName(String binaryName){
        this.binaryName=binaryName;
    }
    public String getBinaryName() {return this.binaryName;}
    
    public void setTraceLevel(int traceLevel){
        this.traceLevel = traceLevel;
    }
    public int getTraceLevel(){
        return this.traceLevel;
    }

    public void setUseLogService(boolean flag){
        this.useLogService = flag;
    }
    public boolean getUseLogService() {
        return this.useLogService;
    }

    public void setUseDietStats(boolean flag){
        this.useDietStats = flag;
    }
    public boolean getUseDietStats(){
        return this.useDietStats;
    }
        
    public void setCfgFileName(String fileName){
        this.cfgFileName = fileName;
    }
    public String getCfgFileName() {
        return this.cfgFileName;
    }
    
    public void setLaunchInfo(LaunchInfo launchInfo){
        this.launchInfo = launchInfo;
    }
    public LaunchInfo getLaunchInfo(){
        return this.launchInfo;
    }
}
