/*@GODIET_LICENSE*/
package goDiet.Utils.CORBA;

public class ToolMsgReceiverImpl extends ToolMsgReceiverPOA {
    goDiet.Utils.LogCentralConnection logCentralConnection;
    public ToolMsgReceiverImpl(goDiet.Utils.LogCentralConnection logConnect) {
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
