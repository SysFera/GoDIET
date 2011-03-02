//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.02 at 11:52:36 AM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The root level of diet infrastructure description.
 * 			
 * 
 * <p>Java class for dietInfrastructure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dietInfrastructure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="masterAgent" type="{}masterAgent" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dietInfrastructure", propOrder = {
    "masterAgent"
})
public class DietInfrastructure {

    @XmlElement(required = true)
    protected List<MasterAgent> masterAgent;

    /**
     * Gets the value of the masterAgent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the masterAgent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMasterAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MasterAgent }
     * 
     * 
     */
    public List<MasterAgent> getMasterAgent() {
        if (masterAgent == null) {
            masterAgent = new ArrayList<MasterAgent>();
        }
        return this.masterAgent;
    }

}
