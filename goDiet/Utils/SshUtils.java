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
    
    /** Creates a new instance of SshUtils */
    public SshUtils() {
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
        if(runConfig.debugLevel >= 2){
          System.out.println("Running: " + command);
        }
        try {
            runtime.exec(command);
        }
        catch (IOException x) {
            System.out.println("stageFile failed.");
        }
        storeRes.setScratchReady(true);
        storeRes.setRunLabel(runLabel);
    }
    
    // TODO: move ssh functionality to sshUtils & incorporate Elagi usage
    public void runWithSsh(Elements element,ComputeResource compRes,
            RunConfig runConfig) {
        String className = element.getClass().getName();
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
            can use "omniNames -start port" command. */
        if(element.getName().compareTo("OmniNames") == 0){
            String omniRemove = "/bin/rm -f " + scratch + "/omninames-*.log ";
            omniRemove += scratch + "/omninames-*.bak ";
            
            String[] commandOmni = {"/usr/bin/ssh", 
                            access.getLogin() + "@" + access.getServer(), 
                            omniRemove};
            for(i = 0; (i < commandOmni.length) && (runConfig.debugLevel >= 2); i++){
                System.out.println("Command element " + i + " is " + commandOmni[i]);
            }

            try {
                Runtime.getRuntime().exec(commandOmni);
            }
            catch (IOException x) {
                System.err.println("runElement failed to remove omni log file.");
            } 
        }
        
        /** Build remote command for launching the job */
        String remoteCommand = "/bin/sh -c \"";
        // Set PATH.  Used to find binaries (unless user provides full path)
        if(compRes.getEnvPath() != null) {
            remoteCommand += "export PATH=" + compRes.getEnvPath() + ":\\$PATH ; ";
        }
        // set LD_LIBRARY_PATH.  Needed by omniNames & servers
        if(compRes.getEnvLdLibraryPath() != null){
                remoteCommand += "export LD_LIBRARY_PATH=" + compRes.getEnvLdLibraryPath() + " ; ";
        }
        // Set OMNINAMES_LOGDIR.  Needed by omniNames.
        if(className.compareTo("goDiet.Model.Elements") == 0){
            remoteCommand += "export OMNINAMES_LOGDIR=" + scratch + " ; ";
        }
        // Set OMNIORB_CONFIG.  Needed by omniNames & all diet components.
        remoteCommand += "export OMNIORB_CONFIG=" + scratch + "/omniORB4.cfg ; ";
        // Get into correct directory. Needed by LogCentral and testTool.
        remoteCommand += "cd " + scratch + " ; ";
        // Provide resiliency to the return from ssh with nohup.  Give binary.
        remoteCommand += "nohup " + element.getBinary() + " ";
        // Provide config file name with full path.  Needed by agents and seds.
        if(className.compareTo("goDiet.Model.Elements") != 0){
            remoteCommand += scratch + "/" + element.getCfgFileName() + " ";
        }
        // Provide command line parameters. Needed by SeDs only.
        if( (className.compareTo("goDiet.Model.ServerDaemon") == 0) &&
            (((ServerDaemon)element).isParametersSet())){
            remoteCommand += ((ServerDaemon)element).getParameters() + " ";
        }
        // Give -start parameter to omniNames.
        if(element.getName().compareTo("OmniNames") == 0){
            remoteCommand += "-start ";
            if(element.isPortSet()){
                remoteCommand += element.getPort() + " ";
            }
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
        remoteCommand += "&\"";
        
        String[] command = {"/usr/bin/ssh", 
                            access.getLogin() + "@" + access.getServer(), 
                            remoteCommand};
        for(i = 0; (i < command.length) && (runConfig.debugLevel >= 2); i++){
            System.out.println("Command element " + i + " is " + command[i]);
        }
    
        try {
            Runtime.getRuntime().exec(command);
        }
        catch (IOException x) {
            System.err.println("runElement failed to run the task " + 
                element.getName() + ".");
        }
    }
}
