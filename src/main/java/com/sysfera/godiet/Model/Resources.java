/*@GODIET_LICENSE*/
/*
 * Resources.java
 *
 * Created on 20 avril 2004
 */

package com.sysfera.godiet.Model;
import java.util.Iterator;
import java.util.Vector;
/**
 *
 * @author  hdail
 */
public  abstract class Resources extends java.util.Observable {
    private String name = null;
    private java.util.Vector accessMethods;
    
    /*List of Elements that use this StorageResource*/
    private Vector elementsList;
    
    private boolean isUsed = false;
    
    /* Constructor for Resources.  Once a Resource is created,
       the name can not be changed. */
    public Resources(String name) {
        this.name = name;
        this.accessMethods = new java.util.Vector();
        this.elementsList = new Vector();
    }
    
    public void addAccessMethod(AccessMethod accessMethod){
        AccessMethod access = null;
        if( this.getAccessMethod(accessMethod.getType()) != null){
            System.err.println("Resource " + this.name + ": Access method " +
                    accessMethod.getType() + "already exists. Addition refused.");
            return;
        }
        this.accessMethods.add(accessMethod);
    }
    
    public String getName() {return this.name;}
    
    public int getAccessMethodCount(){
        return accessMethods.size();
    }
    
    public AccessMethod getAccessMethod(String type){
        AccessMethod access = null;
        for( int i = 0; i < accessMethods.size(); i++) {
            access = (AccessMethod)accessMethods.elementAt(i);
            if(type.equals(access.getType())) {
                return access;
            }
        }
        return null;
    }
    
    public void addElement(Elements el){
        if (el!=null){
            this.elementsList.add(el);
            this.isUsed=true;
        }
    }
    
    public Vector getElementList(){return this.elementsList;}
    
    public int getElementsListCount(){return this.elementsList.size();}
    
    public Elements getElements(String name){
        Elements found = null;
        for (Iterator it = elementsList.iterator(); it.hasNext();){
            Elements el = (Elements)it.next();
            if (name.equals(el.getName()))
                found = el;
            break;
        }
        return found;
    }
    
    public boolean isUsed(){return isUsed;}
}