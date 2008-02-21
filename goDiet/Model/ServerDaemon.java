/*@GODIET_LICENSE*/
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
public class ServerDaemon extends DietElements {
    /** Config-related items.  These should never change while SeD is running */
    private Agents parent;
    private String parameters = null;
    private int maxConcJobs;
    private boolean useConcJobLimit;    
    /** Creates a new instance of ServerDaemon */
    public ServerDaemon(String name, ComputeResource compRes,
                        String binary, Agents parent){
        super(name,compRes,binary);
        this.parent=parent;
        maxConcJobs = 1;
        useConcJobLimit = false;
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
    
    public void enableConcurrentJobLimit(int maxConcJobs){
        this.useConcJobLimit = true;
        if (maxConcJobs > 0){
            this.maxConcJobs = maxConcJobs;
        } else {
            this.maxConcJobs = 1;
        }
    }
    public void disableConcurrentJobLimit(){
        this.useConcJobLimit = false;
    }

    public int getMaxConcurrentJobLimit() {
        return this.maxConcJobs;
    }
    public boolean isConcurrentJobLimitEnabled(){
        return this.useConcJobLimit;
    }


        
}
