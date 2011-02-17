/*@GODIET_LICENSE*/
/*
 * LocalAgent.java
 *
 * Created on 13 avril 2004, 15:24
 */

package com.sysfera.godiet.Model;

import com.sysfera.godiet.Model.physicalresources.ComputeResource;

/**
 *
 * @author  rbolze
 */
public class LocalAgent extends Agents {
    private Agents parent;
    
    /** Creates a new instance of LocalAgent */
    public LocalAgent(String name, ComputeResource compRes, 
                      String binary, Agents parent,Domain domain) {
        super(name, compRes, binary,domain);
        this.parent=parent;
        this.getElementCfg().addOption(new Option("agentType", "DIET_LOCAL_AGENT"));
        this.getElementCfg().addOption(new Option("parentName", parent.getName()));
    }
    
    public Agents getParent(){
        return this.parent;
    }
    
}
