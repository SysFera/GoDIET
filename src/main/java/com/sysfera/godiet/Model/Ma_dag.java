/*@GODIET_LICENSE*/
/*
 * MasterAgent.java
 *
 * Created on 13 avril 2004, 15:24
 */

package com.sysfera.godiet.Model;

/**
 *
 * @author  rbolze
 */
public class Ma_dag extends Agents {    
    private Agents parent;
    private String parameters = null;
    /** Creates a new instance of MasterAgent */
    public Ma_dag(String name, ComputeResource compRes, String binary
            ,Agents parent,Domain domain){
        super(name, compRes, binary,domain);
        this.parent=parent;
        this.getElementCfg().addOption(new Option("agentType", "DIET_MA_DAG"));
        this.getElementCfg().addOption(new Option("parentName", parent.getName()));
    }
    
    public Agents getParent(){
        return this.parent;
    }
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }    
    public String getParameters() {
        return this.parameters;
    }
}
