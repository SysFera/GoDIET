/*@GODIET_LICENSE*/
/*
 * ComputeAccess.java
 *
 * Created on April 20, 2004, 11:06 AM
 */

package com.sysfera.godiet.Model.deprecated;

/**
 *
 * @author  hdail
 */
public class AccessMethod {
    private String type = null;
    private String server = null;
    private String login = null;
   
    private Integer port = 22;
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
        if (login == null || login.equals(""))
            this.login = this.discoverLogin();
        else
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
        String loginTmp = System.getProperty("user.name");
        if (loginTmp == null || loginTmp.equals(""))
                loginTmp = System.getenv("USER");
        return loginTmp;
    }

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

}
