/*
 * LocalAgent.java
 *
 * Created on 13 avril 2004, 15:24
 */

package goDiet.Model;

/**
 *
 * @author  rbolze
 */
public class LocalAgent extends Agents {
    
    private Agents parent;
    
    /** Creates a new instance of LocalAgent */
    public LocalAgent(String name,String hostName,String binary, Agents parent) {
        super(name, hostName, binary);
        this.parent=parent;
    }
    
    public Agents getParent(){
        return this.parent;
    }
    
}
