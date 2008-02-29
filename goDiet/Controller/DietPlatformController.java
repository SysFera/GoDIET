/*@GODIET_LICENSE*/
/*
 * DietPlatformController.java
 *
 * Created on 13 avril 2004, 14:50
 */

package goDiet.Controller;

import goDiet.Utils.*;
import goDiet.Model.*;
import goDiet.Events.*;
import goDiet.Defaults;

/**
 *
 * @author  hdail
 */
public class DietPlatformController implements java.util.Observer {
    //private goDiet.Model.RunConfig         runConfig;
    
    private goDiet.Model.DietPlatform      dietPlatform;
    private goDiet.Model.ResourcePlatform  resourcePlatform;
    private ConsoleController consoleCtrl;
    
    public DietPlatformController(ConsoleController consoleController){
        this.consoleCtrl    = consoleController;
        //runConfig           = new goDiet.Model.RunConfig();
        dietPlatform        = new goDiet.Model.DietPlatform(consoleCtrl);
        resourcePlatform    = new goDiet.Model.ResourcePlatform();
        dietPlatform.addObserver(this);
    }
    
    public DietPlatform getDietPlatform(){
        return this.dietPlatform;
    }
    
    public ResourcePlatform getResourcePlatform(){
        return this.resourcePlatform;
    }
  
    public void update(java.util.Observable observable, Object obj) {
        java.awt.AWTEvent e = (java.awt.AWTEvent)obj;
        if ( e instanceof AddElementsEvent){
            ((Elements)e.getSource()).addObserver(this);
            consoleCtrl.printOutput("New Elements : "+((Elements)e.getSource()).getName(),2);
        }else if (e instanceof AddServiceEvent){
            consoleCtrl.printOutput("New service : " + e.getSource(),1);
        }else if (e instanceof StatusInfosEvent){
            consoleCtrl.printOutput("Infos :" + e.getSource(),0);
        }
    }

    /*public void addRunConfig(RunConfig runCfg){
        this.runConfig = runCfg;
    }
    public RunConfig getRunConfig(){
        return this.runConfig;
    }*/
    
    /* Interfaces for building the diet platform model */
    public void addOmniNames(OmniNames omni){
        this.dietPlatform.addOmniNames(omni);
        omni.addObserver(this);
    }
    public void addLogCentral(LogCentral logger){
        this.dietPlatform.addLogCentral(logger);
        logger.addObserver(this);
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
    public void addMa_dag(Ma_dag newMa_dag){
        this.dietPlatform.addMa_dag(newMa_dag);
        newMa_dag.addObserver(this);
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
    
    public void addComputeCollection(ComputeCollection compColl){
        this.resourcePlatform.addComputeCollection(compColl);
        //compColl.addObserver(this);
    }
    public ComputeCollection getComputeCollection(String label){
        return this.resourcePlatform.getComputeCollection(label);
    }
    
    public ComputeResource getComputeResource(String label){
        return this.resourcePlatform.getComputeResource(label);
    }
    
    public void addStorageResource(StorageResource storRes){
        this.resourcePlatform.addStorageResource(storRes);
        storRes.addObserver(this);
    }
    public StorageResource getStorageResource(String resourceLabel){
        return this.resourcePlatform.getStorageResource(resourceLabel);
    }      
    
    public void printPlatformStatus(){
        this.dietPlatform.printStatus();
    }
}