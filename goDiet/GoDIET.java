/*
 * GoDIET.java
 *
 * Created on April 25, 2004, 12:40 PM
 */

package goDiet;

import goDiet.Controller.*;
import goDiet.Interface.GoDIETFrame;

import java.io.*;
import java.util.*;

/**
 *
 * @author  rbolze
 */
public class GoDIET {
    protected static final String USAGE =
    "USAGE: GoDiet [--launch] <file.xml>";
    
    public GoDIET() {
    }
    
    public static void main(String[] args) {
        boolean launchMode = false;
        boolean shellMode= true; //default
        boolean interfaceMode = false;
        String xmlFile = "";
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
        //System.out.println("shellMode :" + shellMode);
        //System.out.println("interfaceMode :" + interfaceMode);
        //System.out.println("launchMode :"+ launchMode);
        if(launchMode){
            // FIXME : maybe some problems after changes ..
            DietPlatformController mainController =
            new DietPlatformController();
            mainController.parseXmlFile(xmlFile);
            // If not interactive, just launch platform and exit
            System.out.println("* Launching DIET platform.");
            mainController.launchPlatform();
            System.out.println("GoDIET finished.");
        }else if (interfaceMode){
            new GoDIETFrame().show();
        }else if (shellMode){
            ConsoleController consoleController =
            new ConsoleController();
            consoleController.loadXmlFile(xmlFile);
            BufferedReader stdin = new BufferedReader(
            new InputStreamReader(System.in));
            String command;
            while(true){
                System.out.print("GoDIET> ");
                command = "";
                try {
                    command = stdin.readLine();
                    consoleController.doCommand(command);
                    //System.out.println(consoleControler.procedCommand(command));
                    //System.out.println(command);
                } catch(Exception x) {
                    //System.out.println("Exception: "+x.toString());
                    //break;
                }
            }
        }
    }  // End of GoDIET main().
}
