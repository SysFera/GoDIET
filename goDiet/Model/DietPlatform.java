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
    private Elements omniNames;
    private Elements logCentral;
    private boolean haveLogCentral = false;
    private Elements testTool;
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
    
    public void addOmniNames(Elements omni){
        this.omniNames = omni;
        setChanged();
        notifyObservers(new AddElementsEvent(omni));
        clearChanged();
    }
    
    public void addLogCentral(Elements logCentral){
        this.logCentral = logCentral;
        this.haveLogCentral = true;
        setChanged();
        notifyObservers(new AddElementsEvent(logCentral));
        clearChanged();
    }
    
    public void addTestTool(Elements testTool){
        this.testTool = testTool;
        this.haveTestTool = true;
        setChanged();
        notifyObservers(new AddElementsEvent(testTool));
        clearChanged();
    }
    
    public void addMasterAgent(MasterAgent newMA){
        String label = newMA.getName();
        if( label != null ) {
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
        if( label != null ) {
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
        if( label != null ) {
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
    public Elements getOmniNames(){
        return this.omniNames;
    }
    public Elements getLogCentral(){
        return this.logCentral;
    }
    public boolean useLogCentral(){
        return this.haveLogCentral;
    }
    public Elements getTestTool(){
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
}
