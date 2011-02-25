/*@GODIET_LICENSE*/
/*
 * MasterAgent.java
 *
 * Created on 13 avril 2004, 15:24
 */

package com.sysfera.godiet.Model.deprecated;

import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;

/**
 *
 * @author  rbolze
 */
public class MasterAgent extends Agents {
    //private List Neighbor;    // needed only for multi-MA
    /** Creates a new instance of MasterAgent */
    public MasterAgent(String name, ComputeResource compRes, String binary,Domain domain){
        super(name, compRes, binary,domain);
        this.getElementCfg().addOption(new Option("agentType", "DIET_MASTER_AGENT"));
    }      
}