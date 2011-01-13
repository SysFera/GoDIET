/*@GODIET_LICENSE*/
/*
 * DietDeploymentController.java
 *
 * Created on 26 june 2004
 */
package goDiet.Controller;

import goDiet.Model.*;
import goDiet.Events.*;

import java.io.*;
import java.util.Iterator;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

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
    private goDiet.Model.DietPlatform dietPlatform;
    private goDiet.Model.ResourcePlatform resourcePlatform;
    private goDiet.Utils.Launcher launcher;
    private java.lang.Thread dcThread;
    private java.util.Vector requestQueue;
    private int deployState;
    private Elements waitingOn = null;
    private boolean stageFileBefore = true;
    private boolean checkingPlatform = false;
    private java.util.Timer watchdog = null;
    private Watcher watcher = null;
    private static final int WAITING_TIME_FOR_SERVICE = 3000;
    private static final int WAITING_TIME_FOR_ELEMENT_LOG_CONNEXION = 10000;
    private static final int WAITING_TIME_FOR_ELEMENT = 2000;
    private static final int WAITING_TIME_FOR_RECONNECT = 1000;
    private static final int WATCHER_PERIOD = 5000;
    private static final int MAX_RECONNECT_TRY = 3;
    
    public final static String MA_IOR = "MA_IOR";
    public final static String MADAG_IOR = "MADAG_IOR";
    public final static String LA_IOR = "LA_IOR";
    public final static String SED_IOR = "SED_IOR";
    public final static String STATE_DOWN = "DOWN";
    public final static String STATE_OK = "OK";
    public final static String STATE_FAILED = "PROABLY FAILED";
    public DeploymentController(ConsoleController consoleController,
            DietPlatformController modelController) {
        this.consoleCtrl = consoleController;
        this.consoleCtrl.addObserver(this);
        this.modelCtrl = modelController;
        this.logCommCtrl = new LogCentralCommController(this.consoleCtrl,
                this.modelCtrl);
        this.logCommCtrl.addObserver(this);

        dietPlatform = modelCtrl.getDietPlatform();
        resourcePlatform = modelCtrl.getResourcePlatform();
        launcher = new goDiet.Utils.Launcher(consoleController);
        this.requestQueue = new java.util.Vector();
        this.deployState = goDiet.Defaults.DEPLOY_NONE;
        dcThread = new java.lang.Thread(this);
        dcThread.start();
    }

    public void run() {
        consoleCtrl.printOutput("DeploymentController thread starting up.", 2);
        String request;
        while (true) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException x) {
                x.printStackTrace();
            }
            while ((request = deQueueRequest()) != null) {
                if (request.compareTo("launch all") == 0) {
                    consoleCtrl.printOutput("got launch all request", 2);
                    requestLaunch("all");
                }
                if (request.compareTo("launch_check all") == 0) {
                    consoleCtrl.printOutput("got launch_check all request", 2);
                    requestLaunchCheck("all");
                }
            }
        }
    }

    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent) obj;
        String request;
        boolean msgAccept = false;
        if (e instanceof LaunchRequest) {
            request = ((LaunchRequest) e).getLaunchRequest();
            consoleCtrl.printOutput("Got launch request : " + request, 3);
            queueRequest("launch " + request);
            synchronized (this) {
                notifyAll();
            }
        } 
        else if (e instanceof LogStateChange) {
            int newState = ((LogStateChange) e).getNewState();
            String elementName = ((LogStateChange) e).getElementName();
            String elementType = ((LogStateChange) e).getElementType();
            if (this.waitingOn != null) {
                // for agents, sufficient to check name
                // for seds, have to check type
                if ((elementName.compareTo(this.waitingOn.getName()) == 0) ||
                        (elementType.compareTo("SeD") == 0)) {
                    consoleCtrl.printOutput("found log verify for stalled agent" +
                            this.waitingOn.getName(), 2);
                    msgAccept = true;
                    this.waitingOn.getLaunchInfo().setLogState(newState);
                    synchronized (this) {
                        notifyAll();
                    }
                }
            }
            if (msgAccept == false) {
                consoleCtrl.printError("Warning: received delayed launch" +
                        " verification by log for " + elementName, 1);
            // TODO: find element and change log state
            // [TODO: better ID handling for SeDs]
            }
        }
        else if(e instanceof LaunchCheckRequest){
        	request = ((LaunchCheckRequest) e).getLaunchAndRequest();
            consoleCtrl.printOutput("Got launch_check request : " + request, 3);
            queueRequest("launch_check " + request);
            synchronized (this) {
                notifyAll();
            }
        }
    }

    private void queueRequest(String request) {
        synchronized (this.requestQueue) {
            this.requestQueue.add(request);
        }
    }

    private String deQueueRequest() {
        synchronized (this.requestQueue) {
            if (this.requestQueue.size() > 0) {
                return ((String) this.requestQueue.remove(0));
            } else {
                return null;
            }
        }
    }

    /* Interfaces for launching the diet platform, or parts thereof */
    public void requestLaunch(String request) {
        boolean deploySuccess = false;
        setChanged();
        consoleCtrl.printOutput("Deployer: Sending deploy state LAUNCHING.", 3);
        notifyObservers(new goDiet.Events.DeployStateChange(
                this, goDiet.Defaults.DEPLOY_LAUNCHING));
        clearChanged();

        if (request.compareTo("all") == 0) {
            if (stageFileBefore) {
                deploySuccess = launchPlatform2();
            } else {
                deploySuccess = launchPlatform();
            }
        }
        
        setChanged();
        if (deploySuccess) {
            consoleCtrl.printOutput("Deployer: Sending deploy state ACTIVE.", 3);
            notifyObservers(new goDiet.Events.DeployStateChange(
                    this, goDiet.Defaults.DEPLOY_ACTIVE));            
        } else {
            consoleCtrl.printOutput("Deployer: Sending deploy state INACTIVE.", 3);
            notifyObservers(new goDiet.Events.DeployStateChange(
                    this, goDiet.Defaults.DEPLOY_INACTIVE));
        }
        clearChanged();
    }
    
    /* Interfaces for launching the diet platform, or parts thereof */
    public void requestLaunchCheck(String request) {
        boolean deploySuccess = false;
        setChanged();
        consoleCtrl.printOutput("Deployer: Sending deploy state LAUNCHING.", 3);
        notifyObservers(new goDiet.Events.DeployStateChange(
                this, goDiet.Defaults.DEPLOY_LAUNCHING));
        clearChanged();

        if (request.compareTo("all") == 0) {
            if (stageFileBefore) {
                deploySuccess = launchPlatform2();
            } else {
                deploySuccess = launchPlatform();
            }
        }
        checkPlatform();
        setChanged();
        if (deploySuccess) {
            consoleCtrl.printOutput("Deployer: Sending deploy state ACTIVE.", 3);
            notifyObservers(new goDiet.Events.DeployStateChange(
                    this, goDiet.Defaults.DEPLOY_ACTIVE));            
        } else {
            consoleCtrl.printOutput("Deployer: Sending deploy state INACTIVE.", 3);
            notifyObservers(new goDiet.Events.DeployStateChange(
                    this, goDiet.Defaults.DEPLOY_INACTIVE));
        }
        
        clearChanged();
    }

    public boolean launchPlatform() {
        java.util.Date startTime, endTime;
        double timeDiff;
        startTime = new java.util.Date();
        consoleCtrl.printOutput("* Launching DIET platform at " +
                startTime.toString());

        prepareScratch();
        if (launchOmniNames() == false) {
            return false;
        }
        if (this.dietPlatform.useLogCentral()) {
            launchLogCentral();
            if (this.dietPlatform.getLogCentral().useLogToGuideLaunch()) {
                connectLogCentral();
            }
            if (this.dietPlatform.useTestTool()) {
                launchTestTool();
            }
        /*LogCentral logCentral = this.dietPlatform.getLogCentral();
        LaunchInfo logInfo = logCentral.getLaunchInfo();
        if((logInfo.getLaunchState() == goDiet.Defaults.LAUNCH_STATE_RUNNING) &&
        (this.dietPlatform.getTestTool() != null)) {
        if(logCentral.getConnectDuringLaunch() == false){
        launchTestTool();
        } else if(logCentral.getConnectDuringLaunch() &&
        (logInfo.getLogState() == goDiet.Defaults.LOG_STATE_RUNNING)){
        launchTestTool();
        }
        }*/
        }
        launchMasterAgents();
        launchMa_dags();
        launchLocalAgents();
        launchServerDaemons();
        endTime = new java.util.Date();
        timeDiff = (endTime.getTime() - startTime.getTime()) / 1000;
        consoleCtrl.printOutput("* DIET launch done at " + endTime.toString() +
                " [time= " + timeDiff + " sec]");
        consoleCtrl.printOutput("* StorageResource used =" + resourcePlatform.getUsedStorageResources().size());
        consoleCtrl.printOutput("* ComputeResource used =" + resourcePlatform.getUsedComputeResources().size());
        return true;
    }

    public boolean launchPlatform2() {
        java.util.Date startTime, endTime;
        double timeDiff;
        startTime = new java.util.Date();
        consoleCtrl.printOutput("* Launching Method 2");
        consoleCtrl.printOutput("* Launching DIET platform at " +
                startTime.toString());

        prepareScratch();
        createAllCfgFiles();
        stageAllCfgFiles();
        if (launchOmniNames() == false) {
            return false;
        }
        if (this.dietPlatform.useLogCentral()) {
            launchLogCentral();
            if (this.dietPlatform.getLogCentral().useLogToGuideLaunch()) {
                connectLogCentral();
            }
            if (this.dietPlatform.useTestTool()) {
                launchTestTool();
            }
        }
        launchMasterAgents();
        launchMa_dags();
        launchLocalAgents();
        launchServerDaemons();
        endTime = new java.util.Date();
        timeDiff = (endTime.getTime() - startTime.getTime()) / 1000;
        consoleCtrl.printOutput("* DIET launch done at " + endTime.toString() +
                " [time= " + timeDiff + " sec]");
        consoleCtrl.printOutput("* StorageResource used =" + resourcePlatform.getUsedStorageResources().size());
        consoleCtrl.printOutput("* ComputeResource used =" + resourcePlatform.getUsedComputeResources().size());
        if (this.dietPlatform.useLogCentral()) {            
            if (this.dietPlatform.getLogCentral().useLogToGuideLaunch()
                    && this.dietPlatform.getLogCentral().logCentralConnected()){                
                this.logCommCtrl.disconnect();
                consoleCtrl.printOutput("* Disconnect from LogCentral");
            }
        }
        return true;
    }

    public void prepareScratch() {
        String runLabel = null;
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if (runCfg.isLocalScratchReady()) {
            consoleCtrl.printOutput("Local scratch " +
                    runCfg.getLocalScratch() + " already ready.", 2);
            return;
        }

        // Create physical scratch space and set runCfg variables
        // runLabel, localScratch, and scratchReady
        launcher.createLocalScratch();
    }

    public boolean launchOmniNames() {
        OmniNames omni = this.dietPlatform.getOmniNames();
        launchService(omni);
        if (omni.getLaunchInfo().getLaunchState() !=
                goDiet.Defaults.LAUNCH_STATE_RUNNING) {
            consoleCtrl.printError("OmniNames launch failed. " +
                    "All others will fail.", 0);
            return false;
        }
        return true;
    }

    public void launchLogCentral() {
        Elements logger = this.dietPlatform.getLogCentral();
        launchService(logger);
    }

    public void connectLogCentral() {
        LogCentral logger = this.dietPlatform.getLogCentral();

        if (logger.logCentralConnected()) {
            consoleCtrl.printError("* Error: log central already connected.", 1);
            return;
        }
        if (logger.getLaunchInfo().getLaunchState() !=
                goDiet.Defaults.LAUNCH_STATE_RUNNING) {
            logger.setLogCentralConnected(false);
            return;
        }

        OmniNames omni = this.dietPlatform.getOmniNames();
        if (logCommCtrl.connectLogService(omni) == true) {
            consoleCtrl.printOutput("* Connected to Log Central.", 1);
            logger.getLaunchInfo().setLogState(
                    goDiet.Defaults.LOG_STATE_RUNNING);
            logger.setLogCentralConnected(true);
        } else {
            consoleCtrl.printError("* Error connecting to log central.", 1);
            logger.getLaunchInfo().setLogState(
                    goDiet.Defaults.LOG_STATE_CONFUSED);
            logger.setLogCentralConnected(false);
        }
    }

    public void launchTestTool() {
        Elements testTool = this.dietPlatform.getTestTool();
        launchService(testTool);
    }

    public void launchService(Elements service) {
        ComputeResource compRes = service.getComputeResource();
        launchElement(service, compRes);
    }

    public void launchMasterAgents() {
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        launchElements(mAgents);
    }

    public void launchMa_dags() {
        java.util.Vector mAgents = this.dietPlatform.getMa_dags();
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
        LaunchInfo parentLI = null;
        boolean didLaunch = false;
        for (int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            ComputeResource compRes = currElement.getComputeResource();
            didLaunch = launchElement(currElement, compRes);
        }
    }

    private boolean launchElement(Elements element,
            ComputeResource compRes) {
        //boolean userCont = true;

        if (checkLaunchReady(element, compRes) == false) {
            return false;
        }
        /*if(runConfig.debugLevel >= 3){
        userCont = waitUserReady(element);
        }
        if(userCont){*/

        /*** LAUNCH */
        if (stageFileBefore) {
            launcher.launchElement2(element, dietPlatform.useLogCentral());
        } else {
            launcher.launchElement(element, dietPlatform.useLogCentral());
        }

        waitAfterLaunch(element, compRes);

        return true;
    }

    /*** ERROR CHECKING FOR VALID LAUNCH CONDITIONS */
    private boolean checkLaunchReady(Elements element,
            ComputeResource compRes) {
        if (element == null) {
            consoleCtrl.printError("Can not launch null element.");
            return false;
        }
        if (compRes == null) {
            consoleCtrl.printError("Can not launch on null resource.");
            return false;
        }
        if ((element.getLaunchInfo() != null) &&
                (element.getLaunchInfo().getLaunchState() ==
                goDiet.Defaults.LAUNCH_STATE_RUNNING)) {
            consoleCtrl.printError("Element " + element.getName() +
                    " is already running.  Launch request ignored.", 0);
            return false;
        }

        if (!(element instanceof OmniNames)) {
            // No launch if omniNames is not already running
            // [unless we're currently launching omniNames!]
            if ((this.dietPlatform.getOmniNames()).getLaunchInfo().getLaunchState() !=
                    goDiet.Defaults.LAUNCH_STATE_RUNNING) {
                consoleCtrl.printError("OmniNames is not running. " +
                        " Launch for " + element.getName() + " refused.");
                return false;
            }

            // No launch if user wants log feedback to guide launch progress
            // and log central is not correctly connected
            if (!(element instanceof LogCentral) &&
                    (this.dietPlatform.useLogCentral()) &&
                    (this.dietPlatform.getLogCentral().useLogToGuideLaunch())) {
                if (!(this.dietPlatform.getLogCentral().logCentralConnected())) {
                    consoleCtrl.printError("LogCentral is not connected. " +
                            " Launch for " + element.getName() + " refused.");
                    return false;
                }
            }
        }

        // For elements with parent in hierarchy, check on run status of parent
        LaunchInfo parentLI = null;
        Agents parent = null;
        if (element instanceof goDiet.Model.LocalAgent) {
            parent = ((LocalAgent) element).getParent();
            parentLI = parent.getLaunchInfo();
        } else if (element instanceof goDiet.Model.ServerDaemon) {
            parent = ((ServerDaemon) element).getParent();
            parentLI = parent.getLaunchInfo();
        }
        if ((element instanceof goDiet.Model.LocalAgent) ||
                (element instanceof goDiet.Model.ServerDaemon)) {
            if (parentLI.getLaunchState() !=
                    goDiet.Defaults.LAUNCH_STATE_RUNNING) {
                consoleCtrl.printError("Can not launch " + element.getName() +
                        " because parent " + parent.getName() + " is not running.", 1);
                return false;
            }            
            if (this.dietPlatform.useLogCentral() &&
                    this.dietPlatform.getLogCentral().useLogToGuideLaunch() &&
                    this.dietPlatform.getLogCentral().logCentralConnected() &&
                    parent.getElementCfg().getOption("useLogService").getValue().equals("1") &&
                    (parentLI.getLogState() != goDiet.Defaults.LOG_STATE_RUNNING))                    
                    {                
                consoleCtrl.printError("Can not launch " + element.getName() +
                        " because parent " + parent.getName() +
                        " did not register with log.", 1);
                
                return false;
            }
        }
        return true;
    }

    /*** WAIT FOR PROPER LAUNCH BEFORE RETURNING */
    private void waitAfterLaunch(Elements element,
            ComputeResource compRes) {
        // the case of Ma_dag should be remove when the Ma_dag will use the LogService
        if (element instanceof goDiet.Model.Services /*|| element instanceof goDiet.Model.Ma_dag*/) { 
            consoleCtrl.printOutput(
                    "Waiting for " + WAITING_TIME_FOR_SERVICE/1000
                    + " seconds after service launch", 1);
            try {
                Thread.sleep(WAITING_TIME_FOR_SERVICE);
            } catch (InterruptedException x) {
                consoleCtrl.printError("Launch Service: Unexpected sleep " +
                        "interruption.", 0);
            }
        } else if (this.dietPlatform.useLogCentral() &&
                element.getElementCfg().getOption("useLogService").getValue().equals("1")) {
            consoleCtrl.printOutput(
                    "Waiting on log service feedback", 1);
            try {
                synchronized (this) {
                    this.waitingOn = element;
                    this.wait(WAITING_TIME_FOR_ELEMENT_LOG_CONNEXION);
                }
            } catch (InterruptedException x) {
                consoleCtrl.printError("LaunchPlatform: Unexpected wait " +
                        "interruption.", 0);
            }
            if (element.getLaunchInfo().getLogState() ==
                    goDiet.Defaults.LOG_STATE_RUNNING) {
                consoleCtrl.printOutput("Element " + element.getName() +
                        " registered with log.", 2);
            } else {
                consoleCtrl.printOutput("Element " + element.getName() +
                        " did not register with log before deadline.", 1);
            // TODO: any special launch handling required here?
            }
        } else if (element instanceof goDiet.Model.Agents) {
            consoleCtrl.printOutput(
                    "Waiting for " + WAITING_TIME_FOR_ELEMENT/1000
                    + " seconds after launch without log service feedback", 1);
            try {
                Thread.sleep(WAITING_TIME_FOR_ELEMENT);
            } catch (InterruptedException x) {
                consoleCtrl.printError("Launch Element: Unexpected sleep " +
                        "interruption.", 0);
            }
        }
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
        stopWatcher();
        stopServerDaemons();
        stopLocalAgents();
        stopMa_dags();
        stopMasterAgents();

        if (this.dietPlatform.getLogCentral() != null) {
            if (this.dietPlatform.getTestTool() != null) {
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

    public void stopMa_dags() {
        java.util.Vector ma_dags = this.dietPlatform.getMa_dags();
        stopElements(ma_dags);
    }

    public void stopOmniNames() {
        Elements omni = this.dietPlatform.getOmniNames();
        stopService(omni);
    }

    public void stopLogCentral() {
        Elements logger = this.dietPlatform.getLogCentral();
        stopService(logger);
        ((LogCentral) this.dietPlatform.getLogCentral()).setLogCentralConnected(false);
    }

    public void stopTestTool() {
        Elements testTool = this.dietPlatform.getTestTool();
        stopService(testTool);
    }

    public void stopService(Elements service) {
        ComputeResource compRes = service.getComputeResource();
        if (stopElement(service, compRes)) {
            // If stop command was run, sleep afterwards for cleanup time
            try {
                Thread.sleep(500);
            } catch (InterruptedException x) {
                System.err.println("StopService: Unexpected sleep " +
                        "interruption.  Exiting.");
                System.exit(1);
            }
        }
    }

    public void stopElements(java.util.Vector elements) {
        Elements currElement = null;
        String hostRef = null;
        for (int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            ComputeResource compRes = currElement.getComputeResource();
            if (stopElement(currElement, compRes)) {
                // If stop command was run, sleep afterwards for cleanup time
                try {
                    Thread.sleep(100);
                } catch (InterruptedException x) {
                    consoleCtrl.printError("StopElements: Unexpected sleep " +
                            "interruption. Exiting.", 0);
                }
            }
        }
    }

    private boolean stopElement(Elements element,
            ComputeResource compRes) {
        if (element == null) {
            consoleCtrl.printError("StopElement: Can not run stop on null element.");
            return false;
        }
        if (element.getLaunchInfo() == null) {
            consoleCtrl.printError("Element " + element.getName() + " is not " +
                    "running. Ignoring stop command.");
            return false;
        }
        if ((element.getLaunchInfo().getLaunchState() !=
                goDiet.Defaults.LAUNCH_STATE_RUNNING) &&
                (element.getLaunchInfo().getLaunchState() !=
                goDiet.Defaults.LAUNCH_STATE_CONFUSED)) {
            consoleCtrl.printError("Element " + element.getName() + " is not " +
                    "running. Ignoring stop command.");
            return false;
        }
        launcher.stopElement(element);
        return true;
    }

    private void createAllCfgFiles() {
        consoleCtrl.printOutput("* create all Cfg Files", 0);
        OmniNames omni = this.dietPlatform.getOmniNames();
        Elements logger = this.dietPlatform.getLogCentral();
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        java.util.Vector ma_dags = this.dietPlatform.getMa_dags();
        java.util.Vector lAgents = this.dietPlatform.getLocalAgents();
        java.util.Vector seds = this.dietPlatform.getServerDaemons();

        createCfgFile((Elements) omni);
        if (this.dietPlatform.useLogCentral()) {
            createCfgFile((Elements) logger);
        }
        Iterator it = null;
        for (it = mAgents.iterator(); it.hasNext();) {
            createCfgFile((Elements) it.next());
        }
        for (it = ma_dags.iterator(); it.hasNext();) {
            createCfgFile((Elements) it.next());
        }
        for (it = lAgents.iterator(); it.hasNext();) {
            createCfgFile((Elements) it.next());
        }
        for (it = seds.iterator(); it.hasNext();) {
            createCfgFile((Elements) it.next());
        }

        createClientCfgFile(mAgents, ma_dags);
    //System.exit(1);
    }

    private void createClientCfgFile(java.util.Vector mAgents,
            java.util.Vector ma_dags) {
        RunConfig runCfg = consoleCtrl.getRunConfig();
        File cfgFile = new File(runCfg.getLocalScratch(),
                    "client.cfg");
        try {
            cfgFile.createNewFile();
            consoleCtrl.printOutput("Writing config file client.cfg", 1);
            FileWriter out = new FileWriter(cfgFile);

            out.write("# Available MA: ");
            boolean first = true;
            String MAc = "";
            for (Iterator it = mAgents.iterator(); it.hasNext();) {
                MasterAgent MA = (MasterAgent)it.next();
                out.write(MA.getName() + " ");
                if (first) {
                    first = false;
                    MAc = MA.getName();
                }
            }
            out.write("\n");
            out.write("MAName = " + MAc + "\n\n");

            first = true;
            String line = "";
            String MADc = "";
            for (Iterator it = ma_dags.iterator(); it.hasNext();) {
                Ma_dag MAD = (Ma_dag)it.next();
                line += MAD.getName() + " ";
                if (first) {
                    first = false;
                    MADc = MAD.getName();
                }
            }
            if (! first) {
                out.write("# Available MA DAG: " + line + "\n");
                out.write("MADAGNAME = " + MADc);
            }

            out.close();
        } catch (IOException x) {
            consoleCtrl.printError("Failed to write " + cfgFile.getPath());
        }
    }

    private void createCfgFile(Elements element) {
        try {
            launcher.createCfgFile(element, dietPlatform.useLogCentral());
        } catch (IOException x) {
            consoleCtrl.printError("Exception writing cfg file for " +
                    element.getName(), 0);
            consoleCtrl.printError("Exception: " + x + "\nExiting.", 1);
            element.getLaunchInfo().setLaunchState(
                    goDiet.Defaults.LAUNCH_STATE_CONFUSED);
            System.exit(1);     /// TODO: Add error handling and don't exit
        }
    }

    private void stageAllCfgFiles() {
        consoleCtrl.printOutput("* stage all Cfg Files", 0);
        for (Iterator itStRes = resourcePlatform.getUsedStorageResources().iterator(); itStRes.hasNext();) {
            StorageResource stRes = (StorageResource) itStRes.next();
            consoleCtrl.printOutput("Used storageResource =" + stRes.getName(), 3);
            launcher.stageAllFile(stRes);
        }
    }

    private String getStorageResourceServer(Elements el) {
        return el.getComputeResource().getCollection().getStorageResource().getAccessMethod("scp").getServer();
    }

    private StorageResource getStorageResource(Elements el) {
        return el.getComputeResource().getCollection().getStorageResource();
    }


    public void startWatcher() {
        if (this.watchdog == null || this.watcher == null) {
            this.watchdog = new java.util.Timer();
            this.watcher = new Watcher();
            RunConfig runCfg = consoleCtrl.getRunConfig();
            this.watchdog.schedule(watcher, runCfg.getWatcherPeriod(),
                                   runCfg.getWatcherPeriod());
        }
    }

    public void stopWatcher() {
        if (this.watchdog != null || this.watcher != null) {
            this.watchdog.cancel();
            this.watcher.cancel();
            this.watchdog = null;
            this.watcher = null;
        }
    }

    public Vector checkPlatform() {
    	consoleCtrl.printOutput("## BEGIN CHECK");
        Vector checks = new Vector();
        checks.add(checkOmniNames());
        checks.addAll(checkMasterAgents());
        checks.addAll(checkMa_dags());
        checks.addAll(checkLocalAgents());
        checks.addAll(checkServerDaemons());
        return checks;
    }

    private Vector checkRelaunchElements(java.util.Vector elements,
                                         String eltType) {
        Vector checks = new Vector();
        Vector elementsDown = new Vector();
        Vector elementsReconnect = new Vector();
        Properties checkProperties = null;
        Elements currElement = null;
        for (int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            ComputeResource compRes = currElement.getComputeResource();
            //checkElement(currElement, compRes, eltType);

            checkProperties = checkElement2(currElement, compRes, eltType);
            checks.add(checkProperties);

            /* Do we need to relaunch this element? */
            if (checkProperties == null ||
                checkProperties.getProperty("state") != STATE_OK) {
        	consoleCtrl.printOutput("## An element is down");
                currElement.getLaunchInfo().setLaunchState(goDiet.Defaults.LAUNCH_STATE_FAILED);
                elementsDown.add(currElement);

                    if (currElement instanceof Agents) {
                        elementsReconnect.addAll(((Agents) currElement).getChildren());
                    }
            }
        }

        /* Relaunch elements which are down */
        if (! elementsDown.isEmpty()) {
            consoleCtrl.printOutput("# Relaunching killed elements.");
            launchElements(elementsDown);
        }
        
        return elementsReconnect;
    }

    private boolean reconnectElement(Elements element) {
        OmniNames omni = this.dietPlatform.getOmniNames();
        Properties props = new Properties();
        props.put("org.omg.CORBA.ORBInitialHost", omni.getContact());
        props.put("org.omg.CORBA.ORBInitialPort", omni.getPort());
        props.put("org.omg.CORBA.ORBInitRef",
                  "NameService=corbaname::" + omni.getContact() + ":" + omni.getPort());

        boolean reconnected = false;
        int nbRetry = 0;

        org.omg.CORBA.ORB orb = null;

        try {
            orb = org.omg.CORBA.ORB.init(new String[0], props);
        } catch (org.omg.CORBA.INITIALIZE ex) {
            consoleCtrl.printError("ORB initialization problem ("
                    + ex.getMessage() + ")");
            return false;
        }

        while (!reconnected && nbRetry < MAX_RECONNECT_TRY) {
            try {
                if (nbRetry > 0) {
                    consoleCtrl.printError("Waiting "
                            + WAITING_TIME_FOR_RECONNECT/1000 + " second"
                            + " before trying to reconnect.");
                    try {
                        Thread.sleep(WAITING_TIME_FOR_RECONNECT);
                    } catch (InterruptedException x) {
                        consoleCtrl.printError("Reconnect: Unexpected sleep interruption.", 0);
                    }
                }

                org.omg.CORBA.Object rootPOARef =
                        orb.resolve_initial_references("RootPOA");
                POA rootpoa = POAHelper.narrow(rootPOARef);
                rootpoa.the_POAManager().activate();

                org.omg.CORBA.Object ncObjRef =
                        orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(ncObjRef);


                NameComponent nc1 = null;
                if (element instanceof ServerDaemon) {
                    nc1 = new NameComponent("dietSeD", "");
                } else {
                    nc1 = new NameComponent("dietAgent", "");
                }
                NameComponent nc2 = new NameComponent(element.getName(), "");
                NameComponent path[] = {nc1, nc2};
                consoleCtrl.printOutput("Reconnecting " + element.getName());

                org.omg.CORBA.Object objref = ncRef.resolve(path);

                if (element instanceof ServerDaemon) {
                    goDiet.diet.corba.SeD SeD = null;
                    SeD = goDiet.diet.corba.SeDHelper.narrow(objref);
                    SeD.bindParent(((ServerDaemon) element).getParent().getName());
                } else {
                    goDiet.diet.corba.LocalAgent LA;
                    LA = goDiet.diet.corba.LocalAgentHelper.narrow(objref);
                    LA.bindParent(((LocalAgent) element).getParent().getName());
                }

                reconnected = true;
            } catch (org.omg.CORBA.UserException ex2) {
                consoleCtrl.printError("Connection to element " + element.getName()
                        + ": Cannot find the servant.", 0);
                nbRetry ++;
            } catch (Exception e) {
                consoleCtrl.printError("Connection to element " + element.getName()
                        + " (" + e.getMessage()
                        + "). Cannot find the servant.", 0);
                nbRetry ++;
            }
        }

        return reconnected;
    }

    private void reconnectElements(Vector elements) {
        Elements currElement = null;
        for (int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            if (currElement instanceof ServerDaemon ||
                currElement instanceof LocalAgent)
                reconnectElement(currElement);
        }
 
    }

    public void checkRelaunchPlatform(boolean force) {
        if (! force && checkingPlatform) {
            return;
        }
        checkingPlatform = true;
    	consoleCtrl.printOutput("## BEGIN CHECK & RELAUNCH");
        Vector elementsReconnect = new Vector();
        checkRelaunchOmniNames();
        elementsReconnect.addAll(checkRelaunchMasterAgents());
        elementsReconnect.addAll(checkRelaunchMa_dags());
        elementsReconnect.addAll(checkRelaunchLocalAgents());
        elementsReconnect.addAll(checkRelaunchServerDaemons());

        /* Reconnect elements whose parent was down */
        if (! elementsReconnect.isEmpty()) {
            consoleCtrl.printOutput("Waiting for " + WAITING_TIME_FOR_SERVICE/1000
                    + " seconds after service launch", 1);
            try {
                Thread.sleep(WAITING_TIME_FOR_SERVICE);
            } catch (InterruptedException x) {
                consoleCtrl.printError("Launch Service: Unexpected sleep interruption.", 0);
            }

            consoleCtrl.printOutput("# Reconnecting hierarchy.");
            reconnectElements(elementsReconnect);
        }
        checkingPlatform = false;
    }


    private Properties checkOmniNames() {
        OmniNames omni = this.dietPlatform.getOmniNames();
        Properties props = new Properties();
        props.put("org.omg.CORBA.ORBInitialHost", omni.getContact());
        props.put("org.omg.CORBA.ORBInitialPort", omni.getPort());
        props.put("org.omg.CORBA.ORBInitRef",
                  "NameService=corbaname::" + omni.getContact() + ":" + omni.getPort());

        Properties checkProperties = new Properties();

        checkProperties.setProperty("type", omni.getName());
        checkProperties.setProperty("hostname", omni.getComputeResource().getAccessMethod("ssh").getServer());
        checkProperties.setProperty("name", omni.getComputeResource().getName());
        checkProperties.setProperty("pid", "N/A");
        checkProperties.setProperty("state", STATE_OK);

        try {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(new String[0], props);

            try {
                org.omg.CORBA.Object rootPOARef =
                        orb.resolve_initial_references("RootPOA");
                POA rootpoa = POAHelper.narrow(rootPOARef);
                rootpoa.the_POAManager().activate();

                org.omg.CORBA.Object ncObjRef =
                        orb.resolve_initial_references("NameService");
                NamingContextExt ncRef = NamingContextExtHelper.narrow(ncObjRef);
            } catch (Exception e) {
                consoleCtrl.printError("Connection to NameService failed ("
                        + e.getMessage() + ")", 0);
                checkProperties.setProperty("state", STATE_DOWN);
            }
        } catch (org.omg.CORBA.INITIALIZE ex) {
            consoleCtrl.printError("ORB initialization problem ("
                    + ex.getMessage() + ")");
            checkProperties.setProperty("state", STATE_DOWN);
        }

        consoleCtrl.printOutput("# STATEOF "
                + omni.getName() + "\t"
                + checkProperties.getProperty("state") + "\t"
                + checkProperties.getProperty("pid") + "\t"
                + checkProperties.getProperty("name"));

        return checkProperties;
    }

    private void checkRelaunchOmniNames() {
        if (checkOmniNames().getProperty("state") != STATE_OK) {
            Vector elements = new Vector();
            this.dietPlatform.getOmniNames().getLaunchInfo().setLaunchState(
                    goDiet.Defaults.LAUNCH_STATE_FAILED);
            this.dietPlatform.getOmniNames().setBackupRestart(true);
            elements.add(this.dietPlatform.getOmniNames());
            launchElements(elements);
            this.dietPlatform.getOmniNames().setBackupRestart(false);
        }
    }

    private Vector checkMasterAgents() {
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        return checkElements(mAgents, MA_IOR);
    }

    private Vector checkRelaunchMasterAgents() {
        java.util.Vector mAgents = this.dietPlatform.getMasterAgents();
        return checkRelaunchElements(mAgents, MA_IOR);
    }
    
    private Vector checkMa_dags() {        
           java.util.Vector madgas = this.dietPlatform.getMa_dags();
        return checkElements(madgas, MADAG_IOR);
    }

    private Vector checkRelaunchMa_dags() {
           java.util.Vector madgas = this.dietPlatform.getMa_dags();
        return checkRelaunchElements(madgas, MADAG_IOR);
    }

    private Vector checkLocalAgents() {
        java.util.Vector lAgents = this.dietPlatform.getLocalAgents();
        return checkElements(lAgents, LA_IOR);
    }

    private Vector checkRelaunchLocalAgents() {
        java.util.Vector lAgents = this.dietPlatform.getLocalAgents();
        return checkRelaunchElements(lAgents, LA_IOR);
    }

    private Vector checkServerDaemons() {
        java.util.Vector seds = this.dietPlatform.getServerDaemons();
        return checkElements(seds, SED_IOR);
    }

    private Vector checkRelaunchServerDaemons() {
        java.util.Vector seds = this.dietPlatform.getServerDaemons();
        return checkRelaunchElements(seds, SED_IOR);
    }

    private Vector checkElements(java.util.Vector elements,
            String eltType) {
        Vector checks = new Vector();
        Elements currElement = null;                
        for (int i = 0; i < elements.size(); i++) {
            currElement = (Elements) elements.elementAt(i);
            ComputeResource compRes = currElement.getComputeResource();
            //checkElement(currElement, compRes, eltType);
            checks.add(checkElement2(currElement, compRes, eltType));
        }
        return checks;
    }    
 private boolean checkElement(Elements element,
				   ComputeResource compRes,
				   String eltType) {

	//	System.out.println("cheking element " + element.getName() + " [" + eltType + "]");
        ComputeCollection coll = element.getComputeResource().getCollection();
        StorageResource storeRes = coll.getStorageResource();
	String [] cmd = {"/usr/bin/ssh",
			 storeRes.getAccessMethod("scp").getLogin() + "@" +
			 storeRes.getAccessMethod("scp").getServer(),
			 "cat " +
			 storeRes.getScratchBase() + "/" +
			 element.getName() + ".out"};
	//	System.out.println("======" + cmd[0] + " " + cmd[1] + " " + cmd[2]);
	try {
            Process p = Runtime.getRuntime().exec(cmd);
	    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = "";
	    String ior = "";
	    String state = "UNKNOWN";
            while ((line=in.readLine())!=null) {
		//		System.out.println("@" + line);
		if (line.startsWith("## "+ eltType)) {
		    ior = line.split(" ")[2];
		    state = checkIOR(ior, eltType);
		}
	    }
	    consoleCtrl.printOutput("# STATEOF " +
			       element.getName() + " " +
			       state);

            in.close();

	    /*
            while ((line=error.readLine())!=null) {
		System.out.println("@@@" + line);
	    }
	    */

        } catch (IOException x) {
            consoleCtrl.printOutput("# STATEOF " +
			       element.getName() + " " +
			       "UNKNOWN");
            consoleCtrl.printError("stageWithScp failed.",0);
        }

        return true;
    }

    private Properties checkElement2(Elements element,
            ComputeResource compRes,
            String eltType) {
        Properties checkProperties = null;
        try {
            ComputeCollection coll = null;
            coll = element.getComputeResource().getCollection();
            StorageResource storeRes = null;
            storeRes = coll.getStorageResource();

            String[] cmd = {"/usr/bin/ssh",
                storeRes.getAccessMethod("scp").getLogin() + "@" +
                storeRes.getAccessMethod("scp").getServer(),
                "cat " +
                storeRes.getScratchBase() + "/" +
                element.getName() + ".out"
            };
            //	System.out.println("======" + cmd[0] + " " + cmd[1] + " " + cmd[2]);
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = "";
                String ior = "";
                Properties props = new Properties();
                props.put("org.omg.CORBA.ORBInitialHost", dietPlatform.getOmniNames().getContact());
                props.put("org.omg.CORBA.ORBInitialPort", "" + dietPlatform.getOmniNames().getPort());
                String [] emptyArgs = {""};
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(emptyArgs, props);
                //org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init((String[]) null, props);
                while ((line = in.readLine()) != null) {
                    // consoleCtrl.printOutput("@" + line);
                    if (line.startsWith("## " + eltType)) {
                        ior = line.split(" ")[2];
                        checkProperties = checkIOR2(ior, eltType, orb);
                    }
                }
            
                if (checkProperties != null) {
                    checkProperties.setProperty("type", element.getName());
                    checkProperties.setProperty("hostname", element.getComputeResource().getAccessMethod("ssh").getServer());
                    checkProperties.setProperty("name", element.getComputeResource().getName());
                    consoleCtrl.printOutput("# STATEOF " +
                        element.getName() + "\t" +
                        checkProperties.getProperty("state")+"\t"+
                        checkProperties.getProperty("pid")+"\t"+
                        checkProperties.getProperty("name"));
                } else {
                    /* if checkProperties is still null, then no line was found in *.out */
                    in.close();
                    error.close();

                    /* Try to get results in *.err file, and show last 5 lines */
                    cmd[2] = "cat " + storeRes.getScratchBase() + "/" +
                            element.getName() + ".err";

                    p = Runtime.getRuntime().exec(cmd);
                    in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                    String[] err = {"","","","",""};

                    int i = 0;
                    boolean nStr = true;
                    while ((line = in.readLine()) != null) {
                        err[i%5] = line;
                        if (!line.equals(""))
                            nStr = false;
                    }

                    consoleCtrl.printOutput("# STATEOF " + element.getName()
                           + " " + "UNKNOWN");
                    if (! nStr) {
                        consoleCtrl.printOutput("Last lines in '" + element.getName() + ".err':");
                        for (i = 0; i < 5; ++ i)
                            if (!err[i].equals(""))
                                consoleCtrl.printOutput(err[i]);
                    }
                }
                in.close();
                error.close();
            /*
            while ((line=error.readLine())!=null) {
            System.out.println("@@@" + line);
            }
            */

            } catch (IOException x) {
                consoleCtrl.printOutput("# STATEOF " +
                        element.getName() + " " +
                        "UNKNOWN");
                consoleCtrl.printError("stageWithScp failed.", 0);
            }
        } catch (Exception x) {
            consoleCtrl.printOutput("### Error: " + x.getMessage());
        }

        return checkProperties;
    }

    public String checkIOR(String ior, String eltType) {
        String[] args = null;
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

        String state = "UNKNOWN";
        if (eltType.equals(MA_IOR)) {
            goDiet.diet.corba.MasterAgent ma = null;
            ma = goDiet.diet.corba.MasterAgentHelper.narrow(orb.string_to_object(ior));
            try {
                ma.ping();
                return STATE_OK;
            } catch (Exception e) {
                return STATE_DOWN;
            }
        }
        if (eltType.equals(LA_IOR)) {
            goDiet.diet.corba.LocalAgent la = null;
            la = goDiet.diet.corba.LocalAgentHelper.narrow(orb.string_to_object(ior));
            try {
                la.ping();
                return STATE_OK;
            } catch (Exception e) {
                return STATE_DOWN;
            }
        }
        if (eltType.equals(SED_IOR)) {
            goDiet.diet.corba.SeD sed = null;
            sed = goDiet.diet.corba.SeDHelper.narrow(orb.string_to_object(ior));
            try {
                sed.ping();
                return STATE_OK;
            } catch (Exception e) {
                return STATE_DOWN;
            }

        }
        return state;
    }

    public Properties checkIOR2(String ior, String elType, org.omg.CORBA.ORB orb) {
        String state = "UNKNOWN";        
        int ping = -1;

        /* Awful hack to remove stacktrace printout everytime a ping() fails */

        /*PrintStream err = System.err;
        PrintStream out = System.out;
        System.setErr(null);
        System.setOut(null);
         */
        
        if (elType.equals(MA_IOR)) {
            goDiet.diet.corba.MasterAgent ma = null;
            ma = goDiet.diet.corba.MasterAgentHelper.narrow(orb.string_to_object(ior));
            try {
                ping = ma.ping();
                state = STATE_OK;
            } catch (Exception e) {
                state = STATE_DOWN;
            }
        }
        if (elType.equals(MADAG_IOR)) {
            goDiet.diet.corba.MaDag madag = null;
            madag = goDiet.diet.corba.MaDagHelper.narrow(orb.string_to_object(ior));
            try {
                ping = madag.ping();
                state = STATE_OK;
            } catch (Exception e) {
                state = STATE_DOWN;
            }
        }
        if (elType.equals(LA_IOR)) {
            goDiet.diet.corba.LocalAgent la = null;
            la = goDiet.diet.corba.LocalAgentHelper.narrow(orb.string_to_object(ior));
            try {                
                ping = la.ping();
                state = STATE_OK;
            } catch (Exception e) {
                state = STATE_DOWN;
            }
        }
        if (elType.equals(SED_IOR)) {
            goDiet.diet.corba.SeD sed = null;
            sed = goDiet.diet.corba.SeDHelper.narrow(orb.string_to_object(ior));
            try {
                ping = sed.ping();
                state = STATE_OK;
            } catch (Exception e) {
                state = STATE_DOWN;
            }
        }

        /*System.setErr(err);
        System.setOut(out);*/
    
        Properties prop = new Properties();
        prop.setProperty("state", state);
        prop.setProperty("pid", ""+ping);           
        return prop;
    }



    private class Watcher extends java.util.TimerTask {
        public void run(){
            consoleCtrl.printError("# Watcher checking on the Platform", 1);
            checkRelaunchPlatform(false);
        }
    }
}
