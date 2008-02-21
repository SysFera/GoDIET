/*@GODIET_LICENSE*/
/*
 * Launcher.java
 *
 * Created on April 19, 2004, 1:59 PM
 */

package goDiet.Utils;

import goDiet.Model.*;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author  hdail
 */
public class Launcher {
    private goDiet.Controller.ConsoleController consoleCtrl;
    private File killPlatformFile;
    
    /** Creates a new instance of Launcher */
    public Launcher(goDiet.Controller.ConsoleController consoleController) {
        this.consoleCtrl=consoleController;
    }
    
    public void createLocalScratch() {
        RunConfig runCfg = consoleCtrl.getRunConfig();
        File dirHdl;
        String runLabel = null;
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMMdd_HHmm");
        java.util.Date today = new Date();
        String dateString = formatter.format(today);
        
        if(runCfg.useUniqueDirs){
            runLabel = "run_" + dateString;
            dirHdl = new File(runCfg.getLocalScratchBase(),runLabel);
            if( runCfg.useUniqueDirs && dirHdl.exists() ) {
                int i = 0;
                do {
                    i++;
                    dirHdl = new File(runCfg.getLocalScratchBase(),
                            runLabel + "_r" + i);
                } while (dirHdl.exists());
                runLabel += "_r" + i;
            }
            dirHdl.mkdirs();
            runCfg.setLocalScratch(runCfg.getLocalScratchBase() + "/" + runLabel);
            runCfg.setRunLabel(runLabel);
        } else {
            dirHdl = new File(runCfg.getLocalScratchBase());
            dirHdl.mkdirs();
            runCfg.setLocalScratch(runCfg.getLocalScratchBase());
            runCfg.setRunLabel(null);
        }
        
        runCfg.setLocalScratchReady(true);
        consoleCtrl.printOutput("\nLocal scratch directory ready:\n\t" +
                runCfg.getLocalScratch(),1);
        
        // Initiate output file for pids to use as backup for failures
        killPlatformFile = new File(runCfg.getLocalScratch(),
                "killPlatform.csh");
        try {
            if(killPlatformFile.exists()){
                killPlatformFile.delete();
            }
            killPlatformFile.createNewFile();
        } catch (IOException x){
            consoleCtrl.printOutput("createLocalScratch: Could not create " +
                    killPlatformFile);
        }
    }
    
    /* launchElement is the primary method for launching components of the DIET
     * hierarchy.  This method performs the following actions:
     *      - check that element, compRes, & scratch base are non-null
     *      - create the config file locally
     *      - stage the config file to remote host
     *      - run the element on the remote host
     */
    public void launchElement(Elements element,
            boolean useLogService){
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if(element == null){
            consoleCtrl.printError("launchElement called with null element. " +
                    "Launch request ignored.", 1);
            return;
        }
        if(element.getComputeResource() == null){
            consoleCtrl.printError("LaunchElement called with null resource. " +
                    "Launch request ignored.");
            return;
        }
        if(runCfg.isLocalScratchReady() == false){
            consoleCtrl.printError("launchElement: Scratch space is not ready. " +
                    "Need to run createLocalScratch.");
            return;
        }
        
        consoleCtrl.printOutput("\n** Launching element " +
                element.getName() + " on " +
                element.getComputeResource().getName(),1);
        try {
            // LAUNCH STAGE 1: Write config file
            createCfgFile(element, useLogService);            
        } catch (IOException x) {
            consoleCtrl.printError("Exception writing cfg file for " +
                    element.getName(), 0);
            consoleCtrl.printError("Exception: " + x + "\nExiting.", 1);
            element.getLaunchInfo().setLaunchState(
                    goDiet.Defaults.LAUNCH_STATE_CONFUSED);
            System.exit(1);     /// TODO: Add error handling and don't exit
        }
        ComputeCollection coll = element.getComputeResource().getCollection();
        StorageResource storeRes = coll.getStorageResource();
        // LAUNCH STAGE 2: Stage config file
        stageFile(element.getCfgFileName(),storeRes);
        // LAUNCH STAGE 3: Launch element
        runElement(element);
    }
    /* launchElement2 is the second method for launching components of the DIET
     * hierarchy.  This method performs the following actions:
     *      - check that element, compRes, & scratch base are non-null
     *      - run the element on the remote host
     *      - don't need to create and stage cfg file, it suppose to be already done.
     */
    public void launchElement2(Elements element,
            boolean useLogService){
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if(element == null){
            consoleCtrl.printError("launchElement called with null element. " +
                    "Launch request ignored.", 1);
            return;
        }
        if(element.getComputeResource() == null){
            consoleCtrl.printError("LaunchElement called with null resource. " +
                    "Launch request ignored.");
            return;
        }
        consoleCtrl.printOutput("\n** Launching element " +
                element.getName() + " on " +
                element.getComputeResource().getName(),1);
        // LAUNCH STAGE 1: Launch element
        runElement(element);
    }
    // TODO: incorporate Elagi usage
    public void stageFile(String filename,StorageResource storeRes) {
        consoleCtrl.printOutput("Staging file " + filename + " to " +
                storeRes.getName(),1);
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.stageWithScp(filename,storeRes,consoleCtrl.getRunConfig());
    }
    // TODO: incorporate Elagi usage
    public void stageAllFile(StorageResource storeRes) {
        consoleCtrl.printOutput("Staging file to " +
                storeRes.getName(),1);
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.stageFilesWithScp(storeRes,consoleCtrl.getRunConfig());
    }
    // TODO: incorporate Elagi usage
    private void runElement(Elements element) {
        ComputeResource compRes = element.getComputeResource();
        StorageResource storage = compRes.getCollection().getStorageResource();
        consoleCtrl.printOutput("Executing element " + element.getName() +
                " on resource " + compRes.getName(),1);
        AccessMethod access = compRes.getAccessMethod("ssh");
        if(access == null){
            consoleCtrl.printError("runElement: compRes does not have " +
                    "ssh access method. Ignoring launch request.");
            return;
        }
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.runWithSsh(element,consoleCtrl.getRunConfig(),killPlatformFile);
    }
    
    public void stopElement(Elements element){
        consoleCtrl.printOutput("Trying to stop element " + element.getName(),1);
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.stopWithSsh(element,consoleCtrl.getRunConfig());
    }
    
    public void createCfgFile(Elements element,
            boolean useLogService) throws IOException {
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if (element != null){
            if( element.getName().compareTo("TestTool") == 0){
                return;
            }                                                
            File cfgFile = new File(runCfg.getLocalScratch(),
                    element.getCfgFileName());
            try {                                
                cfgFile.createNewFile();
                consoleCtrl.printOutput("Writing config file " +
                    element.getCfgFileName(),1);
                FileWriter out = new FileWriter(cfgFile); 
                element.writeCfgFile(out);                
                out.close();
            } catch (IOException x) {
                consoleCtrl.printError("Failed to write " + cfgFile.getPath());                
                throw x;
            }
        }        
    }
    
    private void writeCfgFileLogCentral(Elements element,FileWriter out) throws IOException {
          element.writeCfgFile(out);
//        out.write("[General]\n\n");
//        out.write("[DynamicTagList]\n");
//        out.write("[StaticTagList]\n");
//        out.write("ADD_SERVICE\n");
//        out.write("[UniqueTagList]\n");
//        out.write("[VolatileTagList]\n");
//        out.write("ASK_FOR_SED\n");
//        out.write("SED_CHOSEN\n");
//        out.write("BEGIN_SOLVE\n");
//        out.write("END_SOLVE\n");
//        out.write("DATA_STORE\n");
//        out.write("DATA_RELEASE\n");
//        out.write("DATA_TRANSFER_BEGIN\n");
//        out.write("DATA_TRANSFER_END\n");
//        out.write("MEM\n");
//        out.write("LOAD\n");
//        out.write("LATENCY\n");
//        out.write("BANDWIDTH\n");
    }
    
    private void writeCfgFileOmniNames(OmniNames omni,FileWriter out) throws IOException {
        omni.writeCfgFile(out);
        //out.write("InitRef = NameService=corbaname::" +
        //        omni.getContact() + ":" + omni.getPort() + "\n");
        //out.write("giopMaxMsgSize = "+omni.getGiopMaxMsgSize()+"\n");
        //out.write("supportBootstrapAgent = 1\n");
    }
    
    private void writeCfgFileDiet(Elements element,FileWriter out) throws IOException {        
        element.writeCfgFile(out);
        /*ComputeResource compRes = element.getComputeResource();
        if(element instanceof goDiet.Model.MasterAgent) {
            out.write("name = " + element.getName() + "\n");
            out.write("agentType = DIET_MASTER_AGENT\n");
            if ( ((MasterAgent)element).getInitRequestID() > 0 ){
                out.write("initRequestID = " + ((MasterAgent)element).getInitRequestID() + "\n");
            }
        } else if( element instanceof goDiet.Model.Ma_dag) {
            out.write("name = " + element.getName() + "\n");
            out.write("agentType = DIET_MA_DAG\n");
            Ma_dag mad = (Ma_dag)element;
            out.write("parentName = " + (mad.getParent()).getName() + "\n");
        } else if( element instanceof goDiet.Model.LocalAgent) {
            out.write("name = " + element.getName() + "\n");
            out.write("agentType = DIET_LOCAL_AGENT\n");
            LocalAgent agent = (LocalAgent)element;
            out.write("parentName = " + (agent.getParent()).getName() + "\n");
        } else if(element instanceof goDiet.Model.ServerDaemon){
            ServerDaemon sed = (ServerDaemon)element;
            out.write("parentName = " + (sed.getParent()).getName() + "\n");
            if(sed.isConcurrentJobLimitEnabled()) {
                out.write("useConcJobLimit = 1\n");
                out.write("maxConcJobs = " + sed.getMaxConcurrentJobLimit() +"\n");
            }
            if (!sed.getBatchName().equals("")){
                out.write("batchName = " + sed.getBatchName() +"\n");
            }
            if (!sed.getBatchQueue().equals("")){
                out.write("batchQueue = " + sed.getBatchQueue() +"\n");
            }
            if (!sed.getPathToNFS().equals("")){
                out.write("pathToNFS = " + sed.getPathToNFS() +"\n");
            }
            if (!sed.getPathToTmp().equals("")){
                out.write("pathToTmp = " + sed.getPathToTmp() +"\n");
            }
        }
        
        out.write("traceLevel = " + element.getTraceLevel() + "\n");
        
        int port = compRes.allocateAllowedPort();
        // port will be -1 if we don't need to use port, or if all ports
        // have been allocated (in which case we try without specifying port)
        if(port > 0){
            out.write("dietPort = " + port + "\n");
        }
        if(compRes.getEndPointContact() != null){
            out.write("dietHostname = " +
                    compRes.getEndPointContact() + "\n");
        }
        out.write("fastUse = 0\n"); // TODO: support config in xml
        out.write("ldapUse = 0\n"); // TODO: support config in xml
        out.write("nwsUse = 0\n");  // TODO: support config in xml
        if(useLogService){
            out.write("useLogService = 1\n");
        } else {
            out.write("useLogService = 0\n");
        }
        out.write("lsOutbuffersize = 0\n");
        out.write("lsFlushinterval = 10000\n");*/
    }
}
