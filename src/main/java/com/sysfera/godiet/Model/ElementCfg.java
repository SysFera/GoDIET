/*@GODIET_LICENSE*
/*
 * 
 */

package com.sysfera.godiet.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This Class is use in order to generate the config_file.cfg
 * of the Element
 * @author rbolze
 */
public class ElementCfg {    
    private String cfgFileName;
    private List options;
    
    public ElementCfg(String cfgFileName){        
        this.cfgFileName=cfgFileName;
        this.options = new ArrayList();        
    }
    public String addOption(Option o){
        String message = "";
        if (options.contains(o)){
            message+="WARNING : you have redefined one option :\n";            
            int index = options.indexOf(o);
            Option old = (Option)options.get(index);
            message+="WARNING : "+old.getName() +" = "+old.getValue()+"\n";
            message+="WARNING : replaced by :\n";
            message+="WARNING : "+o.getName() +" = "+o.getValue();
            options.remove(index);
            options.add(index,o);
        }else{
            this.options.add(o);
        }
        return message;
    }

    public String setCfgFileName(String cfgFileName) {
        return this.cfgFileName=cfgFileName;
    }
    
    public String getCfgFileName() {
        return cfgFileName;
    }
    
    public List getOptions() {
        return options;
    }
    public Option getOption(String name){        
        Option o = new Option();
        o.setName(name);
        if (!options.isEmpty()){
            int index = options.indexOf(o);            
            o =(Option)options.get(index);
        }
        return o;
    }
    public String toString(){
        String out = this.cfgFileName+"\n";
        for (Iterator it = this.options.iterator();it.hasNext();){
            out+=(Option)it.next()+"\n";
        }
        return out;
    }
}
