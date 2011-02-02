/*@GODIET_LICENSE*/
/*
 * LogStateChange.java
 *
 * Created on 27 June 2004, 15:01
 */

package com.sysfera.godiet.Events;

/**
 *
 * @author  hdail
 */
public class LogStateChange extends java.awt.AWTEvent {
    private int newState = -1;
    private String elementName = null;
    private String elementType = null;
    
    /** Creates a new instance of LogStateChange event */
    public LogStateChange(java.lang.Object source, String elementName, 
            String elementType, int state) {
        super(source, 110);
        this.elementName = elementName;
        this.elementType = elementType; // MA, LA, or SeD
        this.newState = state;
    }
    
    public String getElementName(){
        return this.elementName;
    }
    
    public String getElementType(){
        return this.elementType;
    }
    
    public int getNewState(){
        return this.newState;
    }
}
