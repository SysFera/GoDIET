/*@GODIET_LICENSE*/
/*
 * AddElementsEvent.java
 *
 * Created on 13 avril 2004, 15:01
 */

package Events;

/**
 *
 * @author  rbolze
 */
public class DeployStateChange extends java.awt.AWTEvent {
    private int newState = -1;
    
    /** Creates a new instance of AddElementsEvent */
    public DeployStateChange(java.lang.Object source, int state) {
        super(source, 110);
        this.newState = state;
    }
    
    public int getNewState(){
        return this.newState;
    }   
}
