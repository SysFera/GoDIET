/*
 * LaunchInfo.java
 *
 * Created on May 19, 2004, 11:00 AM
 */

package goDiet.Model;

/**
 *
 * @author  hdail
 */
public class LaunchInfo {
    public String[] commandArray = null;
    public String launchStdOut = null;
    public String launchStdErr = null;
    public int pid = -1;
    public boolean running = false;

    /** Creates a new instance of RunConfig */
    public LaunchInfo() {

    }
    
}
