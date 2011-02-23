/*@GODIET_LICENSE*/
/*
 * Agents.java
 *
 * Created on 13 avril 2004, 14:54
 */

package com.sysfera.godiet.Model.deprecated;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;

/**
 *
 * @author  rbolze
 */
public class Agents extends DietElements {   
    private List<Elements> children;
    
    /** Creates a new instance of Agents */
    public Agents(String name,ComputeResource compRes,String binary,Domain domain){
        super(name, compRes, binary,domain);
        this.children= new ArrayList<Elements>();
    }
    
    public void addChild(Elements newChild){
        this.children.add(newChild);
    }


    public List<Elements> getChildren(){
        return this.children;
    }
}
