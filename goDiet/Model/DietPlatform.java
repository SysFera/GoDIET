/*
 * DietPlatform.java
 *
 * Created on 13 avril 2004, 14:51
 */

package goDiet.Model;

import goDiet.Events.*;

/**
 *
 * @author  hdail
 */
public class DietPlatform extends java.util.Observable {
    private OmniNames omniNames;
    private Services logCentral;
    private boolean haveLogCentral = false;
    private Services testTool;
    private boolean haveTestTool = false;
    
    private java.util.Vector masterAgents;
    private java.util.Vector localAgents;
    private java.util.Vector serverDaemons;
    
    // each new DIET component is assigned unique ID.
    // ID counts incremented at each addition & are not re-used.
    private int MA_ID = 0;
    private int LA_ID = 0;
    private int SeD_ID = 0;
    
    /** Creates a new instance of DietPlatform */
    public DietPlatform() {
        this.masterAgents= new java.util.Vector();
        this.localAgents= new java.util.Vector();
        this.serverDaemons= new java.util.Vector();
    }
    
    public void addOmniNames(OmniNames omni){
        this.omniNames = omni;
        setChanged();
        notifyObservers(new AddElementsEvent(omni));
        clearChanged();
    }
    
    public void addLogCentral(Services logCentral){
        this.logCentral = logCentral;
        this.haveLogCentral = true;
        setChanged();
        notifyObservers(new AddElementsEvent(logCentral));
        clearChanged();
    }
    
    public void addTestTool(Services testTool){
        this.testTool = testTool;
        this.haveTestTool = true;
        setChanged();
        notifyObservers(new AddElementsEvent(testTool));
        clearChanged();
    }
    
    public void addMasterAgent(MasterAgent newMA){
        String label = newMA.getName();
        if( (label != null) && (label.length() > 0)) {
            newMA.setName(label + "_" + MA_ID);
        } else {
            newMA.setName("MA_" + MA_ID);
        }
        MA_ID++;
        this.masterAgents.add(newMA);
        setChanged();
        notifyObservers(new AddElementsEvent(newMA));
        clearChanged();
    }
    public void addLocalAgent(LocalAgent newLA){
        String label = newLA.getName();
        if((label != null) & (label.length() > 0)) {
            newLA.setName(label + "_" + LA_ID);
        } else {
            newLA.setName("LA_" + LA_ID);
        }
        LA_ID++;
        this.localAgents.add(newLA);
        setChanged();
        notifyObservers(new AddElementsEvent(newLA));
        clearChanged();
    }
    public void addServerDaemon(ServerDaemon newSeD){
        String label = newSeD.getName();
        if( (label != null) && (label.length() > 0)) {
            newSeD.setName(label + "_" + SeD_ID);
        } else {
            newSeD.setName("SeD_" + SeD_ID);
        }
        SeD_ID++;
        this.serverDaemons.add(newSeD);
        setChanged();
        notifyObservers(new AddElementsEvent(newSeD));
        clearChanged();
    }
    public OmniNames getOmniNames(){
        return this.omniNames;
    }
    public Services getLogCentral(){
        return this.logCentral;
    }
    public boolean useLogCentral(){
        return this.haveLogCentral;
    }
    public Services getTestTool(){
        return this.testTool;
    }
    public boolean useTestTool(){
        return this.haveTestTool;
    }
    public java.util.Vector getMasterAgents() {
        return this.masterAgents;
    }
    public java.util.Vector getLocalAgents() {
        return this.localAgents;
    }
    public java.util.Vector getServerDaemons() {
        return this.serverDaemons;
    }
    
    public void printStatus(){
        java.util.Iterator it;
        MasterAgent mAgent;
        LocalAgent lAgent;
        ServerDaemon sed;
        
        String platStatus = "Platform status is:" +
            "\n\tOmniNames: " + getRunStatus(omniNames);
        if(haveLogCentral){
            platStatus +=
                "\n\tLogCentral: " + getRunStatus(logCentral);
            if(haveTestTool){
                platStatus += 
                    "\n\ttestTool: " + getRunStatus(testTool);
            }
        }
        for(it = masterAgents.iterator(); it.hasNext();){
            mAgent = (MasterAgent) it.next();
            platStatus += "\n\t" + mAgent.getName() + ": " +
                getRunStatus(mAgent);
        }
        for(it = localAgents.iterator(); it.hasNext();){
            lAgent = (LocalAgent) it.next();
            platStatus += "\n\t" + lAgent.getName() + ": " +
                getRunStatus(lAgent);
        }
        for(it = serverDaemons.iterator(); it.hasNext();){
            sed = (ServerDaemon) it.next();
            platStatus += "\n\t" + sed.getName() + ": " +
                getRunStatus(sed);
        }
       
        System.out.println(platStatus);
    }
    private String getRunStatus(Elements element){
        String status = null;
        if((element == null) ||
           (element.getLaunchInfo() == null) ||
           (!element.getLaunchInfo().running)) {
            status = "not running  [sched for " + 
                element.getComputeResource().getName() + "]";
        } else {
            status = "running on " + element.getComputeResource().getName() +
                " with pid " + element.getLaunchInfo().pid;
        }
        return status;
    }
}
