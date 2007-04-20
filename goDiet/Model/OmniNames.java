/*@GODIET_LICENSE*/
/*
 * OmniNames.java
 *
 * Created on 26 May 2004, 14:44
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class OmniNames extends Services {
    private String contact = null;          
    private int port = -1;
    // giopMaxMsgSize This value defines the ORB-wide limit on the size of GIOP message
    private long giopMaxMsgSize = 1073741824; // giopMaxMsgSize in octet default = 1Go 
    
    public OmniNames(String name, ComputeResource compRes, String binary,
                     String contact) {
        super(name, compRes, binary);
        this.contact = contact;
        this.port = 2809;
    }
    public OmniNames(String name, ComputeResource compRes, String binary,
                     String contact, int port) {
        super(name, compRes, binary);
        this.contact = contact;
        this.port = port;
    }
    
    public String getContact(){
        return this.contact;
    }
    public int getPort(){
        return this.port;
    }
    
    public long getGiopMaxMsgSize(){return giopMaxMsgSize;}
    
    public void setGiopMaxMsgSize(long newVal){giopMaxMsgSize=newVal;}
}
