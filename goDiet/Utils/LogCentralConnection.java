/*@GODIET_LICENSE*/
/*
 * LogCentralConnection.java
 *
 * Created on 8 juin 2004, 08:19
 */

package goDiet.Utils;

import goDiet.Controller.LogCentralCommController;
import goDiet.Controller.ConsoleController;

import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.text.SimpleDateFormat;

/**
 *
 * @author  rbolze
 */
public class LogCentralConnection {
    private LogCentralCommController commController;
    private ConsoleController consoleCtrl;
    private String name;
    private java.util.Properties props;
    private  goDiet.Utils.CORBA.filter_t filter;
    private  goDiet.Utils.CORBA.LogCentralTool LCTref;
    
    /** Creates a new instance of LogCentralConnection */
    public LogCentralConnection(String name,String host,String port,
            LogCentralCommController commController, 
            ConsoleController consoleCtrl){
        this.commController = commController;
        this.consoleCtrl = consoleCtrl;
        this.name=name;
        this.props = new java.util.Properties();        
        this.props.put("org.omg.CORBA.ORBInitialHost", host);
        this.props.put("org.omg.CORBA.ORBInitialPort", port);
        filter = new goDiet.Utils.CORBA.filter_t();
        filter.filterName = "allFilter";
        filter.tagList = new String[1];
        // Filter everything.  Still get all static and IN/OUT msgs
        // because LogCentral does not allow filtering on those
        filter.tagList[0]= "IN"; 
        filter.componentList = new String[1];
        filter.componentList[0] = "*";    
    }
    
    public void recieveMsg(goDiet.Utils.CORBA.log_msg_t msg){
        commController.queueMsg(msg);
    }
    public boolean connect(){        
        org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(new String[0], props);
        try {
            org.omg.CORBA.Object ncObjRef =
                orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(ncObjRef);
            
            org.omg.CORBA.Object rootPOARef =
            orb.resolve_initial_references("RootPOA");
            POA rootpoa = POAHelper.narrow(rootPOARef);
            rootpoa.the_POAManager().activate();
            
            NameComponent nc1 = new NameComponent("LogService","");
            NameComponent nc2 = new NameComponent("LogTool", "");
            NameComponent path[] = {nc1, nc2};
               
            org.omg.CORBA.Object objref = ncRef.resolve(path);
            LCTref = goDiet.Utils.CORBA.LogCentralToolHelper.narrow(objref);
            consoleCtrl.printOutput("LCTref="+LCTref, 2);
            goDiet.Utils.CORBA.ToolMsgReceiverImpl TMRimpl = 
                new goDiet.Utils.CORBA.ToolMsgReceiverImpl(this);
            StringHolder s = new StringHolder(name);
            LCTref.connectTool(s,TMRimpl._this(orb));
            LCTref.addFilter(name,filter);
        } catch(org.omg.CORBA.UserException ex2) {
            consoleCtrl.printError("LogCentralConnection: " + 
                "Cannot find the servant.", 0);
            return false;
        } catch (Exception e){
            consoleCtrl.printError("LogCentralConnection: \n" + e.getMessage()+
                "\nCannot find the servant.", 0);
            return false;
        }
        return true;
    }
     public void disconnect(){
        LCTref.disconnectTool(name);
    }
}
