/*@GODIET_LICENSE*/
/*
 * LaunchRequest.java
 *
 * Created on 26 June 2004, 15:01
 */

package com.sysfera.godiet.Events;

/**
 *
 * @author  rbolze
 */
public class LaunchCheckRequest extends java.awt.AWTEvent {
    private String launchAndRequest;
    
    /** Creates a new instance of AddElementsEvent */
    public LaunchCheckRequest(java.lang.Object source, String launchAndRequest) {
        super(source, 110);
        this.launchAndRequest = launchAndRequest;
    }
    
    public String getLaunchAndRequest(){
        return this.launchAndRequest;
    }   
}
