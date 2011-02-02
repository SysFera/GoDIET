/*@GODIET_LICENSE*/
package com.sysfera.godiet.Utils.corba;

import com.sysfera.godiet.Utils.LogCentralConnection;
import com.sysfera.godiet.Utils.corba.generated.ToolMsgReceiverHelper;
import com.sysfera.godiet.Utils.corba.generated.ToolMsgReceiverPOA;
import com.sysfera.godiet.Utils.corba.generated.log_msg_t;

public class ToolMsgReceiverImpl extends ToolMsgReceiverPOA {
    LogCentralConnection logCentralConnection;
    public ToolMsgReceiverImpl(LogCentralConnection logConnect) {
        this.logCentralConnection = logConnect;
        this._initialize_inheritance_tree();
    }
    
    public void sendMsg(log_msg_t[] msgBuf) {
        for (int i = 0 ; i < msgBuf.length ; i++) {
            //sendMsg(msgBuf[i]);
            //System.out.println("SendMsg");
            logCentralConnection.receiveMsg(msgBuf[i]);
        }
        //throw new UnsupportedOperationException();
    }
    
    public void _initialize_inheritance_tree() {//GEN-BEGIN:M_void__initialize_inheritance_tree_
        // Do not edit! This is a method which is necessary for using delegation.
    }//GEN-END:M_void__initialize_inheritance_tree_
    
    
}
