/*
 * DietPlatformController.java
 *
 * Created on 13 avril 2004, 14:50
 */

package goDiet.Controller;

import goDiet.Utils.*;
import goDiet.Model.*;
import goDiet.Events.*;
import java.io.IOException;

/**
 *
 * @author  hdail
 */
public class DietPlatformController implements java.util.Observer {
    private String                         xmlFile; 
    private goDiet.Model.RunConfig         runConfig;
    
    private goDiet.Utils.XmlScanner        xmlScanner;
    private goDiet.Model.DietPlatform      dietPlatform;
    private goDiet.Model.ResourcePlatform  resourcePlatform;
    private goDiet.Utils.Launcher          launcher;
        
    /** Creates a new instance of DietPlatformController */
    public DietPlatformController(){
        runConfig           = new goDiet.Model.RunConfig();
        xmlScanner          = new goDiet.Utils.XmlScanner();
        dietPlatform        = new goDiet.Model.DietPlatform();
        resourcePlatform    = new goDiet.Model.ResourcePlatform();
        launcher            = new goDiet.Utils.Launcher();
        dietPlatform.addObserver(this);
    }
    
    /** Creates a new instance of DietPlatformController */
    public DietPlatformController(RunConfig runConfig,
                                  XmlScanner xmlScanner,
                                  DietPlatform dietPlatform, 
                                  ResourcePlatform resourcePlatform,
                                  Launcher launcher) {
        this.runConfig      = runConfig;
        this.xmlScanner     = xmlScanner;
        this.dietPlatform   = dietPlatform;
        this.resourcePlatform = resourcePlatform;
        this.launcher       = launcher;
        dietPlatform.addObserver(this);
    }
    
    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
         if ( (e instanceof AddElementsEvent) &&
              (this.runConfig.debugLevel >= 2)){            
            System.out.println("New Elements : "+((Elements)e.getSource()).getName());;
        }else if (e instanceof AddServiceEvent){
            System.out.println("New service : "+e.getSource());
        }
    }
   
    /* Interfaces for parsing the platform description xml */
    public void parseXmlFile(String xmlFile){
        try {
            xmlScanner.buildDietModel(xmlFile, this);
        }
        catch (IOException ioe) {
            System.err.println("Parsing of " + xmlFile + " failed. Exiting.");
            System.exit(1);
        }
    }
    
    public void addRunConfig(RunConfig runCfg){
        this.runConfig = runCfg;
    }
    public RunConfig getRunConfig(){
        return this.runConfig;
    }
    
    /* Interfaces for building the diet platform model */   
    public void addOmniNames(Elements newElement){
        this.dietPlatform.addOmniNames(newElement);
        newElement.addObserver(this);
    }
    public void addLogCentral(Elements newElement){
        this.dietPlatform.addLogCentral(newElement);
        newElement.addObserver(this);
    }
    public void addTestTool(Elements newElement){
        this.dietPlatform.addTestTool(newElement);
        newElement.addObserver(this);
    }
    public void addMasterAgent(MasterAgent newMA){
        this.dietPlatform.addMasterAgent(newMA);
        newMA.addObserver(this);
    }
    public void addLocalAgent(LocalAgent newLA, Agents parent){
        parent.addChild(newLA);
        this.dietPlatform.addLocalAgent(newLA);
        newLA.addObserver(this);
    }
    public void addServerDaemon(ServerDaemon newSeD, Agents parent){
        parent.addChild(newSeD);
        this.dietPlatform.addServerDaemon(newSeD);
        newSeD.addObserver(this);
    }
    
    /* Interfaces for building the resource platform model */
    public void addLocalScratchBase(String scratchDir){
        this.resourcePlatform.setLocalScratchBase(scratchDir);
    }
    public String getLocalScratchBase(){
        return this.resourcePlatform.getLocalScratchBase();
    }
    
    public void addComputeResource(ComputeResource compRes){
        this.resourcePlatform.addComputeResource(compRes);
        compRes.addObserver(this);
    }
    public ComputeResource getComputeResource(String resourceLabel){
        return this.resourcePlatform.getComputeResource(resourceLabel);
    }
    
    public void addStorageResource(StorageResource storRes){
        this.resourcePlatform.addStorageResource(storRes);
        storRes.addObserver(this);
    }
    public StorageResource getStorageResource(String resourceLabel){
        return this.resourcePlatform.getStorageResource(resourceLabel);
    }
    
    /* Interfaces for launching the diet platform, or parts thereof */
    public void launchPlatform() {
        verifyPlatform();
        prepareScratch();
        launchOmniNames();
        try { 
            Thread.sleep(5000);
            if(this.dietPlatform.getLogCentral() != null){
                launchLogCentral();
                Thread.sleep(3000);
                if(this.dietPlatform.getTestTool() != null){
                    launchTestTool();
                    Thread.sleep(1000);
                }
            }
            launchMasterAgents();
            Thread.sleep(3000);
            launchLocalAgents();
            Thread.sleep(3000);
            launchServerDaemons();
        } catch (InterruptedException x) {
            System.err.println("LaunchPlatform: Unexpected sleep " +
                "interruption. Exiting.");
            System.exit(1);
        }
    }
    
    public void verifyPlatform(){
        
    }
    
    public void prepareScratch() {
        String runLabel = null;
        String scratch = resourcePlatform.getLocalScratchBase();
        if(resourcePlatform.isLocalScratchReady()){
            System.out.println("Local scratch " + scratch + " already ready.");
            return;
        }
        if(scratch == null){
            System.err.println("Controller: You must set local scratch base before calling prepare.");
            System.exit(1);
        }
        
        runLabel = launcher.createLocalScratch(scratch,this.runConfig.debugLevel);
        if(runLabel != null){
            resourcePlatform.setRunLabel(runLabel);
            resourcePlatform.setLocalScratchReady(true);
        } else {
            System.err.println("Controller: RunLabel improperly set by createLocalScratch.");
        }
    }
    
    public void launchOmniNames() {
        Elements omni = this.dietPlatform.getOmniNames();
        launchService(omni);
    }
    
    public void launchLogCentral() {
        Elements logger = this.dietPlatform.getLogCentral();
        launchService(logger);
    }
    
    public void launchTestTool() {
        Elements testTool = this.dietPlatform.getTestTool();
        launchService(testTool);
    }
        
    public void launchService(Elements service){
        String hostRef = service.getHostReference();
        ComputeResource compRes = 
                resourcePlatform.getComputeResource(hostRef);
        launchElement(service,compRes);
    }
    
    public void launchMasterAgents() {
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        launchElements(mAgents);
    }
    
    public void launchLocalAgents() {
        java.util.Vector lAgents = this.dietPlatform.getLocalAgents();
        launchElements(lAgents);
    }
    
    public void launchServerDaemons() {
        java.util.Vector seds = this.dietPlatform.getServerDaemons();
        launchElements(seds);
    }
    
    public void launchElements(java.util.Vector elements) {
        Elements currElement = null;
        String hostRef = null;
        for( int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            hostRef = currElement.getHostReference();
            ComputeResource compRes = 
                resourcePlatform.getComputeResource(hostRef);
            launchElement(currElement,compRes);
        
            try {
               Thread.sleep(2000);
            }
            catch (InterruptedException x) {
                System.err.println("LaunchPlatform: Unexpected sleep " +
                    "interruption. Exiting.");
                System.exit(1);
            }
        }
    }    
       
    private void launchElement(Elements element,
                               ComputeResource compRes) {
        // TODO before making interface public
        // check that agent is already in the hierarchy.  
        // if not, report warning that can not launch unknown agent.
        if(element == null){
            System.err.println("Can not launch null element.");
            System.exit(1);
        }
        if(compRes == null){
            System.err.println("Can not launch on null resource.");
            System.exit(1);
        }
        launcher.launchElement(element, 
                               compRes,
                               resourcePlatform.getLocalScratchBase(),
                               resourcePlatform.getRunLabel(),
                               dietPlatform.useLogCentral(), 
                               this.runConfig);
    }
    
}
