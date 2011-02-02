/*@GODIET_LICENSE*/
/*
 * Services.java
 *
 * Created on 13 avril 2004, 14:44
 */

package Model;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author  hdail
 */
public  class Services extends Elements {   
    public Services(String name, ComputeResource compRes, String binary) {
        super(name, compRes, binary);
    }
    public void writeCfgFile(FileWriter out) throws IOException{
        // do nothing but it has to be as it extend Elements
    }
}
