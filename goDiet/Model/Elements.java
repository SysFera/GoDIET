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
    private String hostRef = null;    
    private String binary = null;
    
    // Optional configuration options
    private String ip = null;          
    private boolean haveIp = false;
    private int port = -1;          
    private boolean havePort = false;
    private int traceLevel = -1;
    private boolean haveTraceLevel = false;
    private boolean haveLogService = false;
    private boolean useLogService = false;
    private String cfgFileName = null;
    private boolean haveCfgFileName = false;

    /* Constructor for Elements.  Once an Element is created,
       the Name can not be changed. */
    public Elements(String name,String hostReference,String binary) {
        this.name = name;
        this.hostRef = hostReference;
        this.binary = binary;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setHostReference(String hostReference){
        this.hostRef = hostReference;
    }
    
    public void setBinary(String binary){
        this.binary=binary;
    }
    
    public void setIp(String ip){
        this.ip = ip;
        this.haveIp = true;
    }
    
    public void setPort(int port){
        this.port = port;
        this.havePort = true;
    }
    
    public void setTraceLevel(int traceLevel){
        this.traceLevel = traceLevel;
        this.haveTraceLevel = true;
    }
    
    public void setCfgFileName(String fileName){
        this.cfgFileName = fileName;
        this.haveCfgFileName = true;
    }
    
    public String getName() {return this.name;}
    public String getHostReference() {return this.hostRef;}  
    public String getBinary() {return this.binary;}
    
    public boolean isIpSet() {return this.haveIp;}
    public String getIp() {return this.ip;}
    
    public boolean isPortSet() {return this.havePort;}
    public int getPort() {return this.port;}
    
    public boolean isTraceLevelSet() {return this.haveTraceLevel;}
    public int getTraceLevel() {return this.traceLevel;}
    
    public boolean isLogServiceSet() {return this.haveLogService;}
    public boolean getLogService() {return this.useLogService;}
    
    public boolean isCfgFileNameSet() {return this.haveCfgFileName;}
    public String getCfgFileName() {return this.cfgFileName;}
}
