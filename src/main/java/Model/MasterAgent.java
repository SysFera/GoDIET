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
public class MasterAgent extends Agents {
    //private java.util.Vector Neighbor;    // needed only for multi-MA
    /** Creates a new instance of MasterAgent */
    public MasterAgent(String name, ComputeResource compRes, String binary){
        super(name, compRes, binary);
        this.getElementCfg().addOption(new Option("agentType", "DIET_MASTER_AGENT"));
    }      
}
