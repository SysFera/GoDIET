/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goDiet.Utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author rbolze
 */
public class OptionChecker {

    /* 
     * Helper class, this is use only here
     */
    private class ValidOption{        
        public Vector elements = new Vector();
        public String option_name=null;
        public ValidOption(String n){
            this.option_name=n;
        }
        public boolean equals(Object o){            
            if (o instanceof ValidOption)
                return this.option_name.equals(((ValidOption)o).option_name);
            return false;
        }
    }
    
    private URL checkFile = getClass().getResource("/Utils/cfg_options.properties");
    public Vector valid_options = new Vector();
    
    public OptionChecker(){
        loadCheckFile();
    }
    private void loadCheckFile() {                
        java.io.BufferedReader bReader;
        try {
            InputStream is = checkFile.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            bReader = new java.io.BufferedReader(isr, 2 * 1024 * 1024);
            int currentNbLine = 0;
            String line;
            while ((line = bReader.readLine()) != null) {
                currentNbLine++;
                if (line.equals("")){continue;}
                if (line.substring(0,1).equals("#")) {continue;}
                StringTokenizer strTok = new StringTokenizer(line);
                ValidOption valid_opt = new ValidOption(strTok.nextToken());                                
                while(strTok.hasMoreTokens()){
                    valid_opt.elements.add(strTok.nextToken());
                }
                valid_options.add(valid_opt);
                //System.out.println("line :" + currentNbLine + " " + line);
            }
            isr.close();
            is.close();            
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public String check(String element_type,String option_name){
        //System.out.println(this.getClass().getName()+ " check "+element_type+" "+option_name);
        String message = "";
        ValidOption o = new ValidOption(option_name);        
        if (this.valid_options.contains(o)){
            int i = valid_options.indexOf(o);
            ValidOption option = (ValidOption)valid_options.get(i);
            for (Iterator it = option.elements.iterator();it.hasNext();){
                String el= it.next().toString();
                //System.out.println(option.option_name+" "+el);
            }
            if (!option.elements.contains(element_type)){
                message+="WARNING : '"+element_type+"' does not accept '"+option_name+"'";
            }
        }else{
            message+="WARNING : you try to add an unknown option :'"+option_name+"' to '"+element_type+"'";
        }
        if(!message.equals("")){
            StringTokenizer strToken = new StringTokenizer(checkFile.getFile(),"!");
            strToken.nextToken();            
            message+="\n\tCheck file :"+strToken.nextToken();
        }
        return message;
    }
    public static String getObjectClass(String class_name){
        StringTokenizer strTok = new StringTokenizer(class_name,".");
        int nb= strTok.countTokens();
        String name ="";
        while(strTok.hasMoreTokens()){
            name=strTok.nextToken();
        }
        return name;
    }
}
