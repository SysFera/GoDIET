/*
 * GoDIET.java
 *
 * Created on April 25, 2004, 12:40 PM
 */

package goDiet;

import goDiet.Controller.*;

/**
 *
 * @author  hdail
 */
public class GoDIET {
    
    public GoDIET() {
    }
    
    protected static final String USAGE = 
        "USAGE: GoDiet <file.xml>\n";  
    
    public static void main(String[] args) {       
        System.out.println("\n*** Running GoDIET.");

        if( args.length != 1 ) {
            System.err.println(USAGE);
            System.exit(1);
        }
        
        DietPlatformController mainController = 
                new DietPlatformController();

        System.out.println("\n* Parsing input XML file.");
        String xmlFile = args[0];
        mainController.parseXmlFile(xmlFile);
        
        System.out.println("\n* Launching DIET platform.");
        mainController.launchPlatform();
        
        System.out.println("GoDIET finished.");
    }
    
}
