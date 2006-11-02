/*@GODIET_LICENSE*/
/*
 * MasterAgent.java
 *
 * Created on 13 avril 2004, 15:24
 */

package goDiet.Model;

/**
 *
 * @author  rbolze
 */
public class Ma_dag extends Agents {    
    private Agents parent;
    
    /** Creates a new instance of MasterAgent */
    public Ma_dag(String name, ComputeResource compRes, String binary
            ,Agents parent){
        super(name, compRes, binary);
        this.parent=parent;
    }
    
    public Agents getParent(){
        return this.parent;
    }
}
