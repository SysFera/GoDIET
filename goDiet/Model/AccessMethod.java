/*@GODIET_LICENSE*/
/*
 * ComputeAccess.java
 *
 * Created on April 20, 2004, 11:06 AM
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class AccessMethod {
    private String type = null;
    private String server = null;
    private String login = null;
    
    /** Creates a new instance of ComputeAccess */
    public AccessMethod(String type, String server) {
        this.type = type;
        this.server = server;
        this.login = this.discoverLogin();
    }
    
    /** Creates a new instance of ComputeAccess */
    public AccessMethod(String type, String server, String login) {
        this.type = type;
        this.server = server;
        this.login = login;
    }
    
    public String getServer(){
        return this.server;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getLogin(){
        return this.login;
    }
    
    /* Use environment variable retrieval to find out current login name */
    private String discoverLogin() {
        return System.getProperty("user.name");
    }
    
    public static void main(String args[]) {
        AccessMethod newAccess = new AccessMethod("ssh", "localhost");
        String myLogin = newAccess.getLogin();
        System.out.println("ComputeAccess unit test found user " + myLogin + ".");
    }
}
