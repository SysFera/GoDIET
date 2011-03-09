/*@GODIET_LICENSE*/
package com.sysfera.godiet.utils.corba;

import com.sysfera.godiet.utils.corba.generated.LogCentralToolPOA;
import com.sysfera.godiet.utils.corba.generated.ToolMsgReceiver;
import com.sysfera.godiet.utils.corba.generated.filter_t;

public class LogCentralToolImpl extends LogCentralToolPOA {
    public LogCentralToolImpl() {
        this._initialize_inheritance_tree();
    }

    public void test() {
        throw new UnsupportedOperationException();
    }
    
    public short connectTool(org.omg.CORBA.StringHolder toolName, ToolMsgReceiver msgReceiver) {
        throw new UnsupportedOperationException();
    }
    
    public short disconnectTool(String toolName) {
        throw new UnsupportedOperationException();
    }
    
    public String[] getDefinedTags() {
        throw new UnsupportedOperationException();
    }
    
    public String[] getDefinedComponents() {
        throw new UnsupportedOperationException();
    }
    
    public short addFilter(String toolName, filter_t filter) {
        throw new UnsupportedOperationException();
    }
    
    public short removeFilter(String toolName, String filterName) {
        throw new UnsupportedOperationException();
    }
    
    public short flushAllFilters(String toolName) {
        throw new UnsupportedOperationException();
    }
    
    public void _initialize_inheritance_tree() {//GEN-BEGIN:M_void__initialize_inheritance_tree_
        // Do not edit! This is a method which is necessary for using delegation.
    }//GEN-END:M_void__initialize_inheritance_tree_
    
    
}
