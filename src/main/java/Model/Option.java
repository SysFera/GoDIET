/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package goDiet.Model;

/**
 *
 * @author rbolze
 */
public class Option {
    private String name="";
    private String value="";
    public Option(){}
    public Option(String n,String v){
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
    public boolean equals(Object o){
        if (o instanceof Option){
            return name.equals(((Option)o).getName());
        }
        return false;
    }
    public String toString(){
        return name+" = "+value;
    }
}
