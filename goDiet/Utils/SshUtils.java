/*
 * SshUtils.java
 *
 * Created on May 10, 2004, 1:59 PM
 */

package goDiet.Utils;

import goDiet.Model.*;

import java.io.*;

/**
 *
 * @author  hdail
 */
public class SshUtils {
    private goDiet.Controller.DietPlatformController mainController;
    /** Creates a new instance of SshUtils */
    public SshUtils(goDiet.Controller.DietPlatformController dietPlatformController) {
        this.mainController=dietPlatformController;
    }
    
    public void stageWithScp(String localBase, String runLabel,
    String filename,StorageResource storeRes,
    RunConfig runConfig) {
        AccessMethod access = storeRes.getAccessMethod("scp");
        
        String command = new String("/usr/bin/scp ");
        
        // If remote scratch not yet available, create it and stage file by
        // recursive copy.  Else, copy just the file.
        if(storeRes.isScratchReady() == false){
            if(runConfig.useUniqueDirs){
                // scp -r localScratchBase/runLabel remoteScratchBase/
                command += "-r " + localBase + "/" + runLabel + " "; // source
            } else {
                // scp localScratchBase/* remoteScratchBase/
                command += localBase + "/" + filename + " "; // source
                command += localBase + "/omniORB4.cfg "; // omniORB
            }
            command += access.getLogin() + "@" + access.getServer() + ":";
            command += storeRes.getScratchBase();
        } else {
            // format: /usr/bin/scp filename login@host:remoteFile
            if(runConfig.useUniqueDirs){
                command += localBase + "/" + runLabel + "/" + filename + " ";
            } else {
                command += localBase + "/" + filename + " ";
            }
            command += access.getLogin() + "@" + access.getServer() + ":";
            if(runConfig.useUniqueDirs){
                command += storeRes.getScratchBase() + "/" + runLabel;
            } else {
                command += storeRes.getScratchBase();
            }
        }
        java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
        /*if(runConfig.debugLevel >= 2){
            System.out.println("Running: " + command);
        }*/
        mainController.printToConsole("Running: " + command,2);
        try {
            runtime.exec(command);
        }
        catch (IOException x) {
            //System.out.println("stageFile failed.");
            mainController.printToConsole("stageFile failed.",0);
        }
        if(!storeRes.isScratchReady()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException x){
                System.err.println("ScratchPrep: Unexpected sleep " +
                "interruption. Exiting.");
                System.exit(1);
            }
            storeRes.setScratchReady(true);
        }
        storeRes.setRunLabel(runLabel);
    }
    
    // TODO: incorporate Elagi usage
    public void runWithSsh(Elements element, RunConfig runConfig, 
                           FileWriter killBackup) {
        ComputeResource compRes = element.getComputeResource();
        StorageResource storage = compRes.getStorageResource();
        String scratch;
        int i;
        if(runConfig.useUniqueDirs){
            scratch = storage.getScratchBase() + "/" + storage.getRunLabel();
        } else {
            scratch = storage.getScratchBase();
        }
        
        AccessMethod access = compRes.getAccessMethod("ssh");
        if(access == null){
            System.err.println("runElement: compRes does not have ssh access " +
            "method. Ignoring launch request");
            return;
        }
        
        /** If element is omniNames, need to ensure old log file is deleted so
         * can use "omniNames -start port" command. */
        if( (element instanceof goDiet.Model.OmniNames) &&
        (!runConfig.useUniqueDirs)){
            String omniRemove = "/bin/sh -c \" /bin/rm -f " + scratch +
            "/omninames-*.log ";
            omniRemove += scratch + "/omninames-*.bak \" ";
            
            String[] commandOmni = {"/usr/bin/ssh",
            access.getLogin() + "@" + access.getServer(),
            omniRemove};
            /*for(i = 0; (i < commandOmni.length) && (runConfig.debugLevel >= 2); i++){
                System.out.println("Command element " + i + " is " + commandOmni[i]);
            }*/            
            for(i = 0;i < commandOmni.length; i++){
                mainController.printToConsole("Command element " + i + " is " + commandOmni[i],2);
            }
            try {
                Runtime.getRuntime().exec(commandOmni);
            }
            catch (IOException x) {
                System.err.println("runElement failed to remove omni log file.");
                x.printStackTrace();
            }
        }
        
        /** Build remote command for launching the job */
        String remoteCommand = "";
        // Set PATH.  Used to find binaries (unless user provides full path)
        if(compRes.getEnvPath() != null) {
            remoteCommand += "export PATH=" + compRes.getEnvPath() + ":\\$PATH ; ";
        }
        // set LD_LIBRARY_PATH.  Needed by omniNames & servers
        if(compRes.getEnvLdLibraryPath() != null){
            remoteCommand += "export LD_LIBRARY_PATH=" + compRes.getEnvLdLibraryPath() + " ; ";
        }
        // Set OMNINAMES_LOGDIR.  Needed by omniNames.
        if(element instanceof goDiet.Model.OmniNames){
            remoteCommand += "export OMNINAMES_LOGDIR=" + scratch + " ; ";
        }
        // Set OMNIORB_CONFIG.  Needed by omniNames & all diet components.
        remoteCommand += "export OMNIORB_CONFIG=" + scratch + "/omniORB4.cfg ; ";
        if( (element instanceof goDiet.Model.MasterAgent) ||
        (element instanceof goDiet.Model.LocalAgent) ||
        (element instanceof goDiet.Model.ServerDaemon)){
            if( element.getUseDietStats()){
                remoteCommand += "export DIET_STAT_FILE_NAME=" + scratch +
                "/" + element.getName() + ".stats ; ";
            }
        }
        // Get into correct directory. Needed by LogCentral and testTool.
        remoteCommand += "cd " + scratch + " ; ";
        // Provide resiliency to the return from ssh with nohup.  Give binary.
        remoteCommand += "nohup " + element.getBinary() + " ";
        // Provide config file name with full path.  Needed by agents and seds.
        if( (element instanceof goDiet.Model.MasterAgent) ||
        (element instanceof goDiet.Model.LocalAgent) ||
        (element instanceof goDiet.Model.ServerDaemon)){
            remoteCommand += scratch + "/" + element.getCfgFileName() + " ";
        }
        // Provide command line parameters. Needed by SeDs only.
        if( (element instanceof goDiet.Model.ServerDaemon) &&
        (((ServerDaemon)element).isParametersSet())){
            remoteCommand += ((ServerDaemon)element).getParameters() + " ";
        }
        // Give -start parameter to omniNames.
        if(element instanceof goDiet.Model.OmniNames){
            remoteCommand += "-start " + ((OmniNames)element).getPort() + " ";
        }
        if(element.getName().compareTo("LogCentral") == 0){
            remoteCommand += "-config LogCentral.cfg ";
            if(compRes.getEndPointContact() != null){
                remoteCommand += "-ORBendPoint giop:tcp:" +
                compRes.getEndPointContact() + ":";
            } else if(compRes.getEndPointStartPort() > 0){
                remoteCommand += "-ORBendPoint giop:tcp::";
            }
            if(compRes.getEndPointStartPort() > 0){
                int port = compRes.allocateEndPointPort();
                if(port > 0){
                    remoteCommand += port;
                }
            }
            remoteCommand += " ";
        }
        // Redirect stdin/stdout/stderr so ssh can exit cleanly w/ live process
        remoteCommand += "< /dev/null ";
        if(!(runConfig.saveStdOut) && !(runConfig.saveStdErr)){
            remoteCommand += "> /dev/null 2>&1 ";
        } else {
            if(runConfig.saveStdOut){
                remoteCommand += "> " + element.getName() + ".out ";
            } else {
                remoteCommand += "> /dev/null ";
            }
            if(runConfig.saveStdErr){
                remoteCommand += "2> " + element.getName() + ".err ";
            } else {
                remoteCommand += "2> /dev/null ";
            }
        }
        // Background process and give correct quotes
        execSshGetPid(element, access, remoteCommand, runConfig, scratch);
        updateKillScript(element, access, scratch);
    }
    
    // input: command ; command ; command
    private void execSshGetPid(Elements element, AccessMethod access,
    String remoteCommand, RunConfig runConfig, String scratch ){
        String newCommand = null;
        LaunchInfo launchInfo = new LaunchInfo();
        
        newCommand = "( /bin/echo \"" + remoteCommand + "&\" ; ";
        newCommand += "/bin/echo '/bin/echo ${!}' ) | ";
        newCommand += "/usr/bin/ssh -q ";
        newCommand += access.getLogin() + "@" + access.getServer() + " ";
        newCommand += "\" tee " +
        scratch + "/" + element.getName() + ".launch ";
        newCommand += "| /bin/sh - \"";
        
        String[] commandArray = {"/bin/sh", "-c", newCommand};
        launchInfo.commandArray = commandArray;
        
        /*for(int i = 0; (i < commandArray.length) && (runConfig.debugLevel >= 2); i++){
            System.out.println("Command element " + i + " is " + commandArray[i]);
        }*/
        for(int i = 0; (i < commandArray.length); i++){
            mainController.printToConsole("Command element " + i + " is " + commandArray[i],2);
        }
        
        launchInfo.running = false;
        try {
            // Run the process
            Process p = Runtime.getRuntime().exec(commandArray);
            
            // Get output and error from launch
            BufferedReader brErr = new BufferedReader(
            new InputStreamReader(p.getErrorStream()));
            launchInfo.launchStdErr = brErr.readLine();
            BufferedReader brOut = new BufferedReader(
            new InputStreamReader(p.getInputStream()));
            launchInfo.launchStdOut = brOut.readLine();
        }
        catch (IOException x) {
            System.err.println("Launch of " + element.getName() +
            " failed with following exception.");
            x.printStackTrace();
            element.setLaunchInfo(launchInfo);
            return;
        }
        
        if(launchInfo.launchStdErr != null){
            System.err.println("Launch of " + element.getName() +
            " failed with stdErr " + launchInfo.launchStdErr);
        } else if(launchInfo.launchStdOut == null){
            System.err.println("Launch of " + element.getName() +
            " failed to return PID.");
        } else {
            try{
                launchInfo.pid = Integer.parseInt(launchInfo.launchStdOut);
                /*if(runConfig.debugLevel >= 2){
                    System.out.println("PID: " + launchInfo.pid);
                }*/
                 mainController.printToConsole("PID: " + launchInfo.pid,2);
                launchInfo.running = true;
            } catch(NumberFormatException x){
                System.err.println("Launch of " + element.getName() +
                " failed.");
                System.err.println("Could not parse PID in stdout: " +
                launchInfo.launchStdOut);
                launchInfo.pid = -1;
            }
        }
        element.setLaunchInfo(launchInfo);
        return;
    }
    
    public void stopWithSsh(Elements element,RunConfig runConfig) {
        ComputeResource compRes = element.getComputeResource();
        AccessMethod access = compRes.getAccessMethod("ssh");
        if(access == null){
            System.err.println("stopWithSsh: compRes does not have ssh access " +
            "method. Ignoring stop request");
            return;
        }
        
        String stopJob = "kill " + element.getLaunchInfo().pid;
        
        String[] commandStop = {"/usr/bin/ssh",
        access.getLogin() + "@" + access.getServer(),
        stopJob};
        /*for(int i = 0; (i < commandStop.length) && (runConfig.debugLevel >= 2); i++){
            System.out.println("Command element " + i + " is " + commandStop[i]);
        }*/
        for(int i = 0; (i < commandStop.length); i++){
            mainController.printToConsole("Command element " + i + " is " + commandStop[i],2);
        }
        try {
            Runtime.getRuntime().exec(commandStop);
            element.getLaunchInfo().running = false;
        }
        catch (IOException x) {
            System.err.println("stopElement triggered an exception.");
            x.printStackTrace();
        }
    }
    private void updateKillScript(Elements element, AccessMethod access, 
                                  String scratch){
        File killPlatformFile = new File(scratch, 
                                        "killPlatform.csh");
        try{
            FileWriter out = new FileWriter(killPlatformFile, true);
            out.write("# " + element.getName() + "\n");
            out.write("/usr/bin/ssh " + access.getLogin() + "@" + access.getServer()
                        + " kill -9 " + element.getLaunchInfo().pid + "\n");
            out.close();
        } catch (IOException x) {
            System.err.println("Failed to write " + element.getName() + 
                " to " + killPlatformFile.getPath());
        }
    }
}
