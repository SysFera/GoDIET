/*
 * Resources.java
 *
 * Created on 20 avril 2004
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class Resources extends java.util.Observable {
    private String name = null;
    private java.util.Vector accessMethods;

    /* Constructor for Resources.  Once a Resource is created,
       the name can not be changed. */
    public Resources(String name) {
        this.name = name;
        this.accessMethods = new java.util.Vector();
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
    
}
