/*
 * AddElementsEvent.java
 *
 * Created on 13 avril 2004, 15:01
 */

package goDiet.Events;

/**
 *
 * @author  rbolze
 */
public class AddElementsEvent extends java.awt.AWTEvent {
    
    /** Creates a new instance of AddElementsEvent */
    public AddElementsEvent(goDiet.Model.Elements source) {
        super(source, 110);
    } 
}
