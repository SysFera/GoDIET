/*@GODIET_LICENSE*/
/*
 * OmniNames.java
 *
 * Created on 26 May 2004, 14:44
 */

package goDiet.Model;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author  hdail
 */
public class LogCentral extends Services {
    // Does user want guided launch?
    private boolean useLogToGuideLaunch = false;
    
    // Did we connect successfully?  Will never be true unless useLogToGuideLaunch
    // is also true.
    private boolean logCentralConnected = false;

    public LogCentral(String name, ComputeResource compRes, String binary) {
        super(name, compRes, binary);
        this.useLogToGuideLaunch = true;
    }
    
    public LogCentral(String name, ComputeResource compRes, String binary, 
            boolean useLogToGuideLaunch) {
        super(name, compRes, binary);
        this.useLogToGuideLaunch = useLogToGuideLaunch;
    }
    public void writeCfgFile(FileWriter out) throws IOException{
        out.write("[General]\n\n");
        out.write("[DynamicTagList]\n");
        out.write("[StaticTagList]\n");
        out.write("ADD_SERVICE\n");
        out.write("MADAG_SCHEDULER\n");
        out.write("[UniqueTagList]\n");
        out.write("[VolatileTagList]\n");
        out.write("ASK_FOR_SED\n");
        out.write("SED_CHOSEN\n");
        out.write("BEGIN_SOLVE\n");
        out.write("END_SOLVE\n");
        out.write("DATA_STORE\n");
        out.write("DATA_RELEASE\n");
        out.write("DATA_TRANSFER_BEGIN\n");
        out.write("DATA_TRANSFER_END\n");
        out.write("MEM\n");
        out.write("LOAD\n");
        out.write("LATENCY\n");
        out.write("BANDWIDTH\n");
    }
    public boolean useLogToGuideLaunch(){
        return this.useLogToGuideLaunch;
    }
    public void setUseLogToGuideLaunch(boolean flag){
        this.useLogToGuideLaunch = flag;
    }
    public boolean logCentralConnected(){
        return this.logCentralConnected;
    }
    public void setLogCentralConnected(boolean flag){
        this.logCentralConnected = flag;
    }
}
