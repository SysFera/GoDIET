/*
 * GoDIET.java
 *
 * Created on April 25, 2004, 12:40 PM
 */

package goDiet;

import goDiet.Controller.*;

import java.io.*;
import java.util.*;

/**
 *
 * @author  hdail
 */
public class GoDIET {
    
    public GoDIET() {
    }
    
    protected static final String USAGE = 
        "USAGE: GoDiet [--launch] <file.xml>";
    protected static final String HELP = 
        "The following commands are available:\n" +
        "   launch:     launch entire DIET platform\n" +
        "   stop:       kill entire DIET platform using kill pid\n" +
//        "   kill:       kill entire DIET platform using kill -9 pid\n" +
        "   status:     print run status of each DIET component\n" +
        "   history:    print history of commands executed\n" +
        "   help:       print this message\n" +
        "   exit:       exit GoDIET, do not change running platform.";    
    
    public static void main(String[] args) {
        boolean intShell = true;
        String xmlFile = "";
        int i;
        java.util.Date startTime, endTime;
        double timeDiff;
        
        if( (args.length < 1) || (args.length > 2) ) {
            System.err.println(USAGE);
            System.exit(1);
        }
        
        /** Parse Arguments */
        xmlFile = args[args.length - 1];    // last always XML
        for(i = 0; i < args.length; i++){
            if(args[i].compareTo("--launch") == 0){
                intShell = false;
            } 
        }
        
        System.out.println("*** GoDIET.");
   
        /** Prepare GoDIET for usage */
        DietPlatformController mainController = 
                new DietPlatformController();

        System.out.println("\n* Trying to parse input XML file " + xmlFile);
        mainController.parseXmlFile(xmlFile);
        
        if(!intShell){
            // If not interactive, just launch platform and exit
            System.out.println("* Launching DIET platform.");
            mainController.launchPlatform();
            System.out.println("GoDIET finished.");
        } else {
            BufferedReader stdin = new BufferedReader(
                new InputStreamReader(System.in));
            String command;
            java.util.Vector history = new java.util.Vector();
            while(true){
                System.out.print("GoDIET> ");
                command = "";
                try {
                    command = stdin.readLine();
                } catch(Exception x) {
                    break;
                }
                if(command == null)
                    break;
                command = command.trim();
                if(command.equals(""))
                    continue;
                history.addElement(command);
                if(command.equals("history")){
                    for(i = 0; i < history.size(); i++){
                        System.out.println(i + " " + 
                            (String)history.elementAt(i));
                    }
                } else if(command.equals("launch")){
                    startTime = new Date();
                    System.out.println("* Launching DIET platform at " + startTime.toString());
                    mainController.launchPlatform();
                    endTime = new Date();
                    timeDiff = (endTime.getTime() - startTime.getTime())/1000;
                    System.out.println("* DIET launch done at " + endTime.toString() + 
                       " [time= " + timeDiff + " sec]");       
                } else if(command.equals("stop")){
                    startTime = new Date();
                    System.out.println("* Stopping DIET platform at " + startTime.toString());
                    mainController.stopPlatform();
                    endTime = new Date();
                    timeDiff = (endTime.getTime() - startTime.getTime())/1000;
                    System.out.println("* DIET platform stopped at " + endTime.toString() +
                        "[time= " + timeDiff + " sec]");
                    System.out.println("\n* Exiting GoDIET. Bye.");
                    break; // exit.  Change once we handle re-launching better
               /* } else if(command.equals("kill")){
                    System.out.println("* Killing DIET platform.");
                    // TODO*/
                } else if(command.equals("status")){
                    mainController.printPlatformStatus();
                } else if(command.equals("exit")){
                    System.out.println("* Exiting GoDIET. Bye.");
                    break;
                } else {
                    System.out.println(HELP);
                }
            }
        }
    }  // End of GoDIET main(). 
}
