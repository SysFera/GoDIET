//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.23 at 03:33:47 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * An LA transmits requests and information
 *   between MAs and servers (sed).
 * 
 * <p>Java class for localAgent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="localAgent">
 *   &lt;complexContent>
 *     &lt;extension base="{}agent">
 *       &lt;sequence>
 *         &lt;element name="localagent" type="{}localAgent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sed" type="{}sed" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localAgent", propOrder = {
    "localagent",
    "sed"
})
public class LocalAgent
    extends Agent
{

    protected List<LocalAgent> localagent;
    protected List<Sed> sed;

    /**
     * Gets the value of the localagent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localagent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalagent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalAgent }
     * 
     * 
     */
    public List<LocalAgent> getLocalAgent() {
        if (localagent == null) {
            localagent = new ArrayList<LocalAgent>();
        }
        return this.localagent;
    }

    /**
     * Gets the value of the sed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSed().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sed }
     * 
     * 
     */
    public List<Sed> getSed() {
        if (sed == null) {
            sed = new ArrayList<Sed>();
        }
        return this.sed;
    }

}