/*@GODIET_LICENSE*/
/*
 * Agents.java
 *
 * Created on 13 avril 2004, 14:54
 */

package goDiet.Model;

/**
 *
 * @author  rbolze
 */
public class Agents extends DietElements {   
    private java.util.Vector children;
    
    /** Creates a new instance of Agents */
    public Agents(String name,ComputeResource compRes,String binary){
        super(name, compRes, binary);
        this.children= new java.util.Vector();
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

    public java.util.Vector getChildren(){
        return this.children;
    }
}
