//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.25 at 11:19:27 AM CEST 
//


package com.sysfera.godiet.model.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cluster complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cluster">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="computingNode" type="{http://www.sysfera.com}node" maxOccurs="unbounded"/>
 *         &lt;element name="fronted" type="{http://www.sysfera.com}fronted" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cardinality" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="login" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cluster", propOrder = {
    "computingNode",
    "fronted"
})
public class Cluster {

    @XmlElement(required = true)
    protected List<Node> computingNode;
    @XmlElement(required = true)
    protected List<Fronted> fronted;
    @XmlAttribute(required = true)
    protected int cardinality;
    @XmlAttribute(required = true)
    protected String label;
    @XmlAttribute
    protected String login;

    /**
     * Gets the value of the computingNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the computingNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComputingNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Node }
     * 
     * 
     */
    public List<Node> getComputingNode() {
        if (computingNode == null) {
            computingNode = new ArrayList<Node>();
        }
        return this.computingNode;
    }

    /**
     * Gets the value of the fronted property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fronted property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFronted().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Fronted }
     * 
     * 
     */
    public List<Fronted> getFronted() {
        if (fronted == null) {
            fronted = new ArrayList<Fronted>();
        }
        return this.fronted;
    }

    /**
     * Gets the value of the cardinality property.
     * 
     */
    public int getCardinality() {
        return cardinality;
    }

    /**
     * Sets the value of the cardinality property.
     * 
     */
    public void setCardinality(int value) {
        this.cardinality = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

}
