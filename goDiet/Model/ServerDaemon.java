/*
 * ServerDaemon.java
 *
 * Created on 13 avril 2004, 15:10
 */

package goDiet.Model;

/**
 *
 * @author  rbolze
 */
public class ServerDaemon extends Elements {
    /** Config-related items.  These should never change while SeD is running */
    private Agents parent;
    private String parameters = null;
    
    /** Creates a new instance of ServerDaemon */
    public ServerDaemon(String name, ComputeResource compRes,
                        String binary, Agents parent){
        super(name,compRes,binary);
        this.parent=parent;
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
