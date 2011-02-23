/*@GODIET_LICENSE*/
/*
 * ServerDaemon.java
 *
 * Created on 13 avril 2004, 15:10
 */

package com.sysfera.godiet.Model.deprecated;

import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;

/**
 *
 * @author  rbolze
 */
public class ServerDaemon extends DietElements {
    /** Config-related items.  These should never change while SeD is running */
    private Agents parent;
    private String parameters = null;
    /** Creates a new instance of ServerDaemon */
    public ServerDaemon(String name, ComputeResource compRes,
                        String binary, Agents parent,Domain domain){
        super(name,compRes,binary,domain);
        this.parent=parent;
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
