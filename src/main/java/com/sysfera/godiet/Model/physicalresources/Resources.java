/*@GODIET_LICENSE*/
/*
 * Resources.java
 *
 * Created on 20 avril 2004
 */

package com.sysfera.godiet.Model.physicalresources;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sysfera.godiet.Model.AccessMethod;
import com.sysfera.godiet.Model.Domain;
import com.sysfera.godiet.Model.Elements;
/**
 *
 * @author  hdail
 */
public  abstract class Resources extends java.util.Observable {
    private String name = null;
    private List<AccessMethod> accessMethods;
    
    /*List of Elements that use this StorageResource*/
    private List<Elements> elementsList;
    
    private boolean isUsed = false;
	private Domain domain;
    
    /* Constructor for Resources.  Once a Resource is created,
       the name can not be changed. */
    public Resources(String name,Domain domain) {
        this.name = name;
        this.accessMethods = new ArrayList<AccessMethod>();
        this.elementsList = new ArrayList<Elements>();
        this.domain = domain;
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
            access = (AccessMethod)accessMethods.get(i);
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
    
    public List<Elements> getElementList(){return this.elementsList;}
    
    
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


	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}
}
