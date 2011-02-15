/*@GODIET_LICENSE*/
/*
 * Agents.java
 *
 * Created on 13 avril 2004, 14:54
 */

package com.sysfera.godiet.Model;

import java.util.ArrayList;
import java.util.List;

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
        //setChanged();
        //notifyObservers(new Events.AddElementsEvent(newChild));
        //clearChanged();
    }
/*    public void setName(String name){
        super.setName(name);
        this.elConfig.addOption(new Option("name", name));
    }
 */

    public List<Elements> getChildren(){
        return this.children;
    }
}
