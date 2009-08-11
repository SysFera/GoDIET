/*@GODIET_LICENSE*/
/*
 * GoDIET.java
 *
 * Created on April 25, 2004, 12:40 PM
 */

package goDiet;

import goDiet.Controller.*;
import goDiet.Interface.GoDIETFrame;
import goDiet.Events.DeployStateChange;

import java.io.*;
import java.util.*;
import jline.*;

/**
 *
 * @author  hdail
 */
public class GoDIET implements java.util.Observer {
    protected static final String USAGE =
            "USAGE: GoDiet [--launch|--launch_check] <file.xml>\n" +
            "       GoDiet --interface";
    private int deployState = Defaults.DEPLOY_NONE;
    
    public GoDIET() {
    }
    
    public void update(Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
        int newState;
        if ( e instanceof DeployStateChange){
            newState = ((DeployStateChange)e).getNewState();
            //System.out.println("godiet Changing state to : " +
              //  goDiet.Defaults.getDeployStateString(newState));
            this.deployState = newState;
            if(newState != goDiet.Defaults.DEPLOY_LAUNCHING &&
               newState != goDiet.Defaults.DEPLOY_STOPPING){
               synchronized(this){
                  notifyAll();
               } 
            }
        }
    }

    public static void main(String[] args) {
        GoDIET goDiet = new GoDIET();
        boolean launchMode = false;
        boolean shellMode= true; //default
        boolean interfaceMode = false;
        String xmlFile = "";
        boolean giveUserCtrl = true;
        boolean check_mode = false;
        int i;
        //System.out.println("args.length :"+args.length);
        if (args.length<1 || args.length>2){
            System.out.println(USAGE);
            System.exit(1);
        }
        /** Parse Arguments */
        
        if (args.length == 2){
            if(args[0].compareTo("--launch") == 0){
                shellMode = false;
                interfaceMode=false;
                launchMode =true;
                xmlFile = args[args.length - 1];
            }   
            if(args[0].compareTo("--launch_check") == 0){
                shellMode = false;
                interfaceMode=false;
                launchMode =true;
                check_mode = true;
                xmlFile = args[args.length - 1];
            } 
        }
        if (args.length==1){
            if (args[0].compareTo("--interface") == 0){
                shellMode = false;
                interfaceMode=true;
                launchMode =false;
            }else{
                xmlFile = args[args.length - 1];
            }
        }
        
        /** Prepare GoDIET for usage */
        if(launchMode){
            // If not interactive, just launch platform and exit
            ConsoleController consoleController = new ConsoleController(goDiet);
            consoleController.loadXmlFile(xmlFile);            
            consoleController.printOutput("* Launching DIET platform.");
            if(check_mode) consoleController.doCommand("launch_check");
            else consoleController.doCommand("launch");
            consoleController.printOutput("GoDIET finished.");
        } else if (interfaceMode){
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GoDIETFrame().setVisible(true);
            }
            });
            
        } else if (shellMode){
            ConsoleController consoleController = new ConsoleController(goDiet);
            consoleController.loadXmlFile(xmlFile);
            BufferedReader stdin = new BufferedReader(
                new InputStreamReader(System.in));

            try {
                ConsoleReader reader = new ConsoleReader();
                reader.setBellEnabled(false);

                List completors = new LinkedList();
                completors.add(new SimpleCompletor(new String[] { "launch", "launch_check",
                    "relaunch", "stop", "status", "history", "help", "check",
                    "stop_check", "exit"}));
                reader.addCompletor(new ArgumentCompletor(completors));

                String command;
                while(true){
                    System.out.print("GoDIET> ");
                    command = "";
                    try {
                        //command = stdin.readLine();
                        command = reader.readLine();
                        giveUserCtrl = consoleController.doCommand(command);
                    } catch(Exception x) {
                        consoleController.printOutput("Exception: " + x.toString());
                        //break;
                    }
                    if(giveUserCtrl == false){
                        try {
                            synchronized(goDiet){
                                goDiet.wait();
                            }
                        }
                        catch (InterruptedException x) {
                            System.out.println("Unexpected sleep interruption.");
                        }
                    }
                }           // End while(true)
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }               // End Shell mode context
    }                   // End main{}
}                       // End GoDIET
