/*
 * DietDeploymentController.java
 *
 * Created on 26 june 2004
 */

package goDiet.Controller;

import goDiet.Utils.*;
import goDiet.Model.*;
import goDiet.Events.*;
import goDiet.Defaults;

//import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author  hdail
 */
public class DeploymentController extends java.util.Observable
                                  implements Runnable,
                                             java.util.Observer {
    private ConsoleController consoleCtrl;
    private DietPlatformController modelCtrl;
    private LogCentralCommController logCommCtrl;
    
    private goDiet.Model.DietPlatform      dietPlatform;
    private goDiet.Model.ResourcePlatform  resourcePlatform;
    private goDiet.Utils.Launcher          launcher;
    private java.lang.Thread               dcThread;
    
    private java.util.Vector requestQueue;
    private int deployState;
    private Elements waitingOn = null;
    private boolean logCentralConnected = false;

    public DeploymentController(ConsoleController consoleController,
                                DietPlatformController modelController){
        this.consoleCtrl    = consoleController;
        this.consoleCtrl.addObserver(this);
        this.modelCtrl      = modelController;
        this.logCommCtrl = new LogCentralCommController(this.consoleCtrl, 
                                                        this.modelCtrl);
        this.logCommCtrl.addObserver(this);

        dietPlatform        = modelCtrl.getDietPlatform();
        resourcePlatform    = modelCtrl.getResourcePlatform();
        launcher            = new goDiet.Utils.Launcher(consoleController);
        this.requestQueue   = new java.util.Vector();
        this.deployState    = goDiet.Defaults.DEPLOY_NONE;
        dcThread            = new java.lang.Thread(this);
        dcThread.start();
    }
    
    public void run() {
        consoleCtrl.printOutput("DeploymentController thread starting up.",2);
        String request;
        while(true) {
            try {
                synchronized(this){
                    this.wait();
                }
            } catch (InterruptedException x){
                x.printStackTrace();
            }
            while( (request = deQueueRequest()) != null){
                if(request.compareTo("launch all") == 0){
                    consoleCtrl.printOutput("got launch all request",2);
                    requestLaunch("all");
                }
            }
        }
    }
    
    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
        String request;
        boolean msgAccept = false;
        if ( e instanceof LaunchRequest){
            request = ((LaunchRequest)e).getLaunchRequest();
            consoleCtrl.printOutput("Got launch request : " + request, 3);
            queueRequest("launch " + request);
            synchronized(this){
                notifyAll();
            }
        } else if (e instanceof LogStateChange){
            int newState = ((LogStateChange)e).getNewState();
            String elementName = ((LogStateChange)e).getElementName();
            String elementType = ((LogStateChange)e).getElementType();
            if(this.waitingOn != null){
                // for agents, sufficient to check name
                // for seds, have to check type
                if((elementName.compareTo(this.waitingOn.getName()) == 0) ||
                   (elementType.compareTo("SeD") == 0)){
                    consoleCtrl.printOutput("found log verify for stalled agent" +
                        this.waitingOn.getName(), 2);
                    msgAccept = true;
                    this.waitingOn.getLaunchInfo().setLogState(newState);
                    synchronized(this){
                        notifyAll();
                    }
                } 
            } 
            if(msgAccept == false){
                consoleCtrl.printError("Warning: received delayed launch" +
                    " verification by log for " + elementName, 1);
                // TODO: find element and change log state
                // [TODO: better ID handling for SeDs]
            }
        }
    }
    private void queueRequest(String request){
        synchronized(this.requestQueue) {
            this.requestQueue.add(request);
        }
    }
    private String deQueueRequest(){
        synchronized(this.requestQueue) {
            if(this.requestQueue.size() > 0){
                return ((String)this.requestQueue.remove(0));
            } else {
                return null;
            }
        }
    }

    /* Interfaces for launching the diet platform, or parts thereof */
    public void requestLaunch(String request){
        setChanged();
        consoleCtrl.printOutput("Deployer: Sending deploy state LAUNCHING.",3);
        notifyObservers(new goDiet.Events.DeployStateChange(
            this,goDiet.Defaults.DEPLOY_LAUNCHING));
        clearChanged();
        
        if(request.compareTo("all") == 0){
            launchPlatform();
        }
        
        setChanged();
        consoleCtrl.printOutput("Deployer: Sending deploy state ACTIVE.", 3);
        notifyObservers(new goDiet.Events.DeployStateChange(
            this,goDiet.Defaults.DEPLOY_ACTIVE));
        clearChanged();
    }
    
    public void launchPlatform() {
        java.util.Date startTime, endTime;
        double timeDiff;
        startTime = new java.util.Date();
        consoleCtrl.printOutput("* Launching DIET platform at " + 
                startTime.toString());

        prepareScratch();
        launchOmniNames();
        if(this.dietPlatform.getLogCentral() != null){
            launchLogCentral();
            LaunchInfo logInfo = this.dietPlatform.getLogCentral().getLaunchInfo();
            if((logInfo.getLaunchState() == goDiet.Defaults.LAUNCH_STATE_RUNNING) &&
               (logInfo.getLogState() == goDiet.Defaults.LOG_STATE_RUNNING) && 
               (this.dietPlatform.getTestTool() != null)){
                launchTestTool();
            }
        }
        launchMasterAgents();
        launchLocalAgents();
        launchServerDaemons();
        endTime = new java.util.Date();
        timeDiff = (endTime.getTime() - startTime.getTime())/1000;
        consoleCtrl.printOutput("* DIET launch done at " + endTime.toString() +
                " [time= " + timeDiff + " sec]");
    }
    
    public void prepareScratch() {
        String runLabel = null;
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if(runCfg.isLocalScratchReady()){
            consoleCtrl.printOutput("Local scratch " + 
                runCfg.getLocalScratch() + " already ready.", 2);
            return;
        }
        
        // Create physical scratch space and set runCfg variables 
        // runLabel, localScratch, and scratchReady
        launcher.createLocalScratch();
    }
    
    public void launchOmniNames() {
        OmniNames omni = this.dietPlatform.getOmniNames();
        launchService(omni);
        if(omni.getLaunchInfo().getLaunchState() != 
                goDiet.Defaults.LAUNCH_STATE_RUNNING){
            consoleCtrl.printError("OmniNames launch failed.  Exiting.", 0);
            System.exit(1);
        }
    }
    
    public void launchLogCentral() {
        logCentralConnected = false;
        Elements logger = this.dietPlatform.getLogCentral();
        launchService(logger);
        OmniNames omni = this.dietPlatform.getOmniNames();
        if(logCommCtrl.connectLogService(omni) == true){
            consoleCtrl.printOutput("* Connected to Log Central.", 1);
            logger.getLaunchInfo().setLogState(
                goDiet.Defaults.LOG_STATE_RUNNING);
            logCentralConnected = true;
        } else {
            consoleCtrl.printError("* Error connecting to log central. " + 
                "Continuing with sleep btwn launches.", 1);
            logger.getLaunchInfo().setLogState(
                goDiet.Defaults.LOG_STATE_CONFUSED);
            logCentralConnected = false;
        }
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
            consoleCtrl.printError("LaunchPlatform: Unexpected sleep " +
                "interruption.",0);
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
                if(logCentralConnected){
                    synchronized(this){
                        this.waitingOn = currElement;
                        this.wait(10000);
                    }
                } else {
                    Thread.sleep(2000);
                }
            }
            catch (InterruptedException x) {
                consoleCtrl.printError("LaunchPlatform: Unexpected sleep " +
                    "interruption.", 0);
            }
            if(logCentralConnected){
                if(currElement.getLaunchInfo().getLogState() == 
                        goDiet.Defaults.LOG_STATE_RUNNING){
                    consoleCtrl.printOutput("Element " + currElement.getName() +
                        " registered with log.", 2);
                } else {
                    consoleCtrl.printOutput("Element " + currElement.getName() +
                    " did not register with log before deadline.", 2);
                    // TODO: any special launch handling required here?
                }
            }
        }
    }
    
    private void launchElement(Elements element,
                               ComputeResource compRes) {
        //boolean userCont = true;
        if(element == null){
            consoleCtrl.printError("Can not launch null element.");
            //System.exit(1);
        }
        if(compRes == null){
            consoleCtrl.printError("Can not launch on null resource.");
            //System.exit(1);
        }
        if((element.getLaunchInfo() != null) &&
           (element.getLaunchInfo().getLaunchState() == 
                goDiet.Defaults.LAUNCH_STATE_RUNNING)){
            consoleCtrl.printError("Element " + element.getName() +
                " is already running.  Launch request ignored.", 0);
            return;
        }
        /*if(runConfig.debugLevel >= 3){
            userCont = waitUserReady(element);
        }
        if(userCont){*/
        launcher.launchElement(element,dietPlatform.useLogCentral());
        //}
    }
    
    /*private boolean waitUserReady(Elements element){
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
    }*/
    
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
                Thread.sleep(500);
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
                    Thread.sleep(100);
                }
                catch (InterruptedException x) {
                    consoleCtrl.printError("StopElements: Unexpected sleep " +
                        "interruption. Exiting.", 0);
                }
            }
        }
    }
    
    private boolean stopElement(Elements element,
                                ComputeResource compRes) {
        if(element == null){
            consoleCtrl.printError("StopElement: Can not run stop on null element.");
            return false;
        }
        if(element.getLaunchInfo() == null){
           consoleCtrl.printError("Element " + element.getName() + " is not " +
                "running. Ignoring stop command.");
           return false;
        }
        if( (element.getLaunchInfo().getLaunchState() != 
                    goDiet.Defaults.LAUNCH_STATE_RUNNING) && 
            (element.getLaunchInfo().getLaunchState() != 
                goDiet.Defaults.LAUNCH_STATE_CONFUSED)){
            consoleCtrl.printError("Element " + element.getName() + " is not " +
                "running. Ignoring stop command.");
            return false;
        }
        launcher.stopElement(element);
        return true;
    }
}