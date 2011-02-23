/*@GODIET_LICENSE*/
/*
 * Services.java
 *
 * Created on 13 avril 2004, 14:44
 */

package com.sysfera.godiet.Model.deprecated;

import java.io.FileWriter;
import java.io.IOException;

import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;

/**
 *
 * @author  hdail
 */
public  class Services extends Elements {   
    public Services(String name, ComputeResource compRes, String binary,Domain domain) {
        super(name, compRes, binary,domain);
    }
    public void writeCfgFile(FileWriter out) throws IOException{
        // do nothing but it has to be as it extend Elements
    }
}
