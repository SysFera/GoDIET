/*@GODIET_LICENSE*/
/*
 * DietPlatformController.java
 *
 * Created on 13 avril 2004, 14:50
 */

package com.sysfera.godiet.Controller;

import com.sysfera.godiet.Events.AddElementsEvent;
import com.sysfera.godiet.Events.AddServiceEvent;
import com.sysfera.godiet.Events.StatusInfosEvent;
import com.sysfera.godiet.Model.Agents;
import com.sysfera.godiet.Model.ComputeCollection;
import com.sysfera.godiet.Model.ComputeResource;
import com.sysfera.godiet.Model.DietPlatform;
import com.sysfera.godiet.Model.Domain;
import com.sysfera.godiet.Model.Elements;
import com.sysfera.godiet.Model.Gateway;
import com.sysfera.godiet.Model.LocalAgent;
import com.sysfera.godiet.Model.LogCentral;
import com.sysfera.godiet.Model.Ma_dag;
import com.sysfera.godiet.Model.MasterAgent;
import com.sysfera.godiet.Model.OmniNames;
import com.sysfera.godiet.Model.ResourcePlatform;
import com.sysfera.godiet.Model.ServerDaemon;
import com.sysfera.godiet.Model.Services;
import com.sysfera.godiet.Model.StorageResource;

/**
 *
 * @author  hdail
 */
public class DietPlatformController implements java.util.Observer {
    //private RunConfig         runConfig;
    
    private com.sysfera.godiet.Model.DietPlatform      dietPlatform;
    private ResourcePlatform  resourcePlatform;
    private ConsoleController consoleCtrl;
    
    public DietPlatformController(ConsoleController consoleController){
        this.consoleCtrl    = consoleController;
        //runConfig           = new RunConfig();
        dietPlatform        = new DietPlatform(consoleCtrl);
        resourcePlatform    = new ResourcePlatform();
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
            consoleCtrl.printOutput("New Element : "+((Elements)e.getSource()).getName(),2);
            consoleCtrl.printOutput("Elements Cfg: "+((Elements)e.getSource()).getElementCfg(),3);
        }else if (e instanceof AddServiceEvent){
            consoleCtrl.printOutput("New service : " + e.getSource(),1);            
        }else if (e instanceof StatusInfosEvent){
            consoleCtrl.printOutput("Infos :" + e.getSource(),0);
        }
    }

    
    
    /* Interfaces for building the diet platform model */
    public void addOmniNames(OmniNames omni){
        this.dietPlatform.addOmniNames(omni);
        omni.addObserver(this);
    }
    public void addLogCentral(LogCentral logger){
        this.dietPlatform.addLogCentral(logger);
        logger.addObserver(this);
    }
    public void addDomain(Domain domain)
    {
    	this.dietPlatform.addDomain(domain);
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

	public void addGateway(Gateway gateway) {
		
		
	}
}