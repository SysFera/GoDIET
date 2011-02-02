/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 *
 * @author rbolze
 */
public class EnvVar {
    private String name;
    private String value;
    public EnvVar(){}
    public EnvVar(String n,String v){
        this.name=n;
        this.value=v;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
//    public boolean equals(Object o){
//        if (o instanceof EnvVar){
//            return name.equals(((EnvVar)o).getName());
//        }
//        return false;
//    }
}
