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
    //private java.util.Vector services;
    private String parameters;
    private boolean haveParameters = false;
    
    private Agents parent;
    
    /** Creates a new instance of ServerDaemon */
    public ServerDaemon(String name,String hostName,
                        String binary,Agents parent) {
        super(name, hostName, binary);
        this.parent=parent;
        //services = new java.util.Vector();
    }
    
    public void setParameters(String parameters) {
        this.parameters = parameters;
        this.haveParameters = true;
    }
        
    public boolean isParametersSet() {return this.haveParameters;}
    public String getParameters() {return this.parameters;}
    
    /*public void addService(String newService){
        services.add(newService);
        setChanged();
        notifyObservers(new Events.AddServiceEvent(newService));
        clearChanged();
    }*/
    
    public Agents getParent(){
        return this.parent;
    }
}
