/*@GODIET_LICENSE*/
/*
 * LaunchRequest.java
 *
 * Created on 26 June 2004, 15:01
 */

package goDiet.Events;

/**
 *
 * @author  rbolze
 */
public class LaunchRequest extends java.awt.AWTEvent {
    private String launchRequest;
    
    /** Creates a new instance of AddElementsEvent */
    public LaunchRequest(java.lang.Object source, String launchRequest) {
        super(source, 110);
        this.launchRequest = launchRequest;
    }
    
    public String getLaunchRequest(){
        return this.launchRequest;
    }   
}
