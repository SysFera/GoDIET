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
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
    
    private ConsoleController consoleController;
    
    /** Creates a new instance of DietPlatformController */
    public DietPlatformController(){
        runConfig           = new goDiet.Model.RunConfig();
        xmlScanner          = new goDiet.Utils.XmlScanner();
        dietPlatform        = new goDiet.Model.DietPlatform();
        resourcePlatform    = new goDiet.Model.ResourcePlatform();
        launcher            = new goDiet.Utils.Launcher(this);
        dietPlatform.addObserver(this);
    }
    public DietPlatformController(ConsoleController consoleController){
        this.consoleController=consoleController;
        runConfig           = new goDiet.Model.RunConfig();
        xmlScanner          = new goDiet.Utils.XmlScanner();
        dietPlatform        = new goDiet.Model.DietPlatform();
        resourcePlatform    = new goDiet.Model.ResourcePlatform();
        launcher            = new goDiet.Utils.Launcher(this);
        dietPlatform.addObserver(this);
    }
    
  
    
    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
        if ( e instanceof AddElementsEvent){
            //System.out.println("New Elements : "+((Elements)e.getSource()).getName());
            ((Elements)e.getSource()).addObserver(this);
            //if (this.runConfig.debugLevel >= 2)
            printToConsole("New Elements : "+((Elements)e.getSource()).getName(),2);
        }else if (e instanceof AddServiceEvent){
            //System.out.println("New service : "+e.getSource());
            printToConsole("New service : "+e.getSource(),1);
        }else if (e instanceof StatusInfosEvent){
            printToConsole("Infos :"+e.getSource(),0);
        }/*else if (e instanceof ElementLaunchEvent){
            printToConsole("LaunchInfo : PID :"+((LaunchInfo)e.getSource()).pid,0);
            printToConsole("LaunchInfo : running ?"+((LaunchInfo)e.getSource()).running,0);
        }*/
        
    }
    /* Interfaces for parsing the platform description xml */
    public void parseXmlFile(String xmlFile){
        this.printToConsole("load xml File :"+xmlFile,0);
        try {
            xmlScanner.buildDietModel(xmlFile, this);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Can not continue without valid XML.  Exiting.");
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
    public void addOmniNames(OmniNames omni){
        this.dietPlatform.addOmniNames(omni);
        omni.addObserver(this);
    }
    public void addLogCentral(Services newService){
        this.dietPlatform.addLogCentral(newService);
        newService.addObserver(this);
    }
    public void addTestTool(Services newService){
        this.dietPlatform.addTestTool(newService);
        newService.addObserver(this);
    }
    public void setUseDietStats(boolean flag){
        this.dietPlatform.setUseDietStats(flag);
        // addObserver?
    }
    public boolean getUseDietStats(){
        return this.dietPlatform.getUseDietStats();
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
        if(this.dietPlatform.getLogCentral() != null){
            launchLogCentral();
            if(this.dietPlatform.getTestTool() != null){
                launchTestTool();
            }
        }
        launchMasterAgents();
        launchLocalAgents();
        launchServerDaemons();
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
        
        runLabel = launcher.createLocalScratch(scratch,this.runConfig);
        if(runLabel != null){
            resourcePlatform.setRunLabel(runLabel);
            resourcePlatform.setLocalScratchReady(true);
        } else {
            System.err.println("Controller: RunLabel improperly set by createLocalScratch.");
        }
    }
    
    public void launchOmniNames() {
        OmniNames omni = this.dietPlatform.getOmniNames();
        launchService(omni);
        if(!omni.isRunning()){
            System.err.println("OmniNames launch failed.  Exiting.");
            System.exit(1);
        }
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
        ComputeResource compRes = service.getComputeResource();
        launchElement(service,compRes);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException x){
            System.err.println("LaunchPlatform: Unexpected sleep " +
            "interruption. Exiting.");
            System.exit(1);
        }
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
            ComputeResource compRes = currElement.getComputeResource();
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
        boolean userCont = true;
        if(element == null){
            System.err.println("Can not launch null element.");
            System.exit(1);
        }
        if(compRes == null){
            System.err.println("Can not launch on null resource.");
            System.exit(1);
        }
        if((element.getLaunchInfo() != null) &&
        (element.getLaunchInfo().running)){
            System.err.println("Element " + element.getName() +
            " is already running.  Launch request ignored.");
            return;
        }
        if(runConfig.debugLevel >= 3){
            userCont = waitUserReady(element);
        }
        if(userCont){
            launcher.launchElement(element,
            resourcePlatform.getLocalScratchBase(),
            resourcePlatform.getRunLabel(),
            dietPlatform.useLogCentral(),
            this.runConfig);
        }
    }
    
    private boolean waitUserReady(Elements element){
        System.out.println("\nType <return> to launch " + element.getName() +
        ", <no> to skip this element, or <stop> to quit ...");
        String userInput = "";
        BufferedReader stdin = new BufferedReader(
        new InputStreamReader(System.in));
        try {
            userInput = stdin.readLine();
        } catch(Exception x) {
            System.err.println("Exception caught while waiting for input. " +
            "Ignoring exception.");
        }
        userInput = userInput.trim();
        if(userInput.equals("no")){
            System.out.println("Skipping launch of " + element.getName() +
            ".  The launch of any sub-elements will fail!");
            return false;
        } else if(userInput.equals("stop")){
            stopPlatform();
            System.exit(1);
        }
        return true;
    }
    
    /* Interfaces for stopping the diet platform, or parts thereof */
    public void stopPlatform() {
        stopServerDaemons();
        stopLocalAgents();
        stopMasterAgents();
        
        if(this.dietPlatform.getLogCentral() != null){
            if(this.dietPlatform.getTestTool() != null){
                stopTestTool();
            }
            stopLogCentral();
        }
        stopOmniNames();
    }
    
    public void stopServerDaemons() {
        java.util.Vector seds = this.dietPlatform.getServerDaemons();
        stopElements(seds);
    }
    
    public void stopLocalAgents() {
        java.util.Vector lAgents = this.dietPlatform.getLocalAgents();
        stopElements(lAgents);
    }
    
    public void stopMasterAgents() {
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        stopElements(mAgents);
    }
    
    public void stopOmniNames() {
        Elements omni = this.dietPlatform.getOmniNames();
        stopService(omni);
    }
    
    public void stopLogCentral() {
        Elements logger = this.dietPlatform.getLogCentral();
        stopService(logger);
    }
    
    public void stopTestTool() {
        Elements testTool = this.dietPlatform.getTestTool();
        stopService(testTool);
    }
    
    public void stopService(Elements service){
        ComputeResource compRes = service.getComputeResource();
        if(stopElement(service,compRes)){
            // If stop command was run, sleep afterwards for cleanup time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException x){
                System.err.println("StopService: Unexpected sleep " +
                "interruption.  Exiting.");
                System.exit(1);
            }
        }
    }
    
    public void stopElements(java.util.Vector elements) {
        Elements currElement = null;
        String hostRef = null;
        for( int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            ComputeResource compRes = currElement.getComputeResource();
            if( stopElement(currElement,compRes) ){
                // If stop command was run, sleep afterwards for cleanup time
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException x) {
                    System.err.println("StopElements: Unexpected sleep " +
                    "interruption. Exiting.");
                    System.exit(1);
                }
            }
        }
    }
    
    private boolean stopElement(Elements element,
    ComputeResource compRes) {
        if(element == null){
            System.err.println("Can not run stop on null element.");
            return false;
        }
        if((element.getLaunchInfo() == null) ||
        (!element.getLaunchInfo().running)){
            System.err.println("Element " + element.getName() + " is not " +
            "running. Ignoring stop command.");
            return false;
        }
        launcher.stopElement(element,
        this.runConfig);
        return true;
    }
    
    public void printPlatformStatus(){
        this.dietPlatform.printStatus();
    }
    public void printToConsole(String msg,int debugLevel){
        //System.out.println("ask debugLevel :"+debugLevel);
        //System.out.println("runConfig.debugLevel :"+this.runConfig.debugLevel);
        if (debugLevel <= this.runConfig.debugLevel){
            consoleController.printToConsole(msg);
        }
    }
}