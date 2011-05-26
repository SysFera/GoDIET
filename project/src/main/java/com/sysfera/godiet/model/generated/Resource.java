//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.26 at 03:30:55 PM CEST 
//


package com.sysfera.godiet.model.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Represent a physical resource on which you can run
 * 				DietAgent
 * 
 * <p>Java class for resource complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ssh" type="{http://www.sysfera.com}ssh" maxOccurs="unbounded"/>
 *         &lt;element name="env" type="{http://www.sysfera.com}env" minOccurs="0"/>
 *         &lt;element name="endPoint" type="{http://www.sysfera.com}endPoint" minOccurs="0"/>
 *         &lt;element name="scratch" type="{http://www.sysfera.com}scratch"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource", propOrder = {
    "ssh",
    "env",
    "endPoint",
    "scratch"
})
@XmlSeeAlso({
    Node.class
})
public abstract class Resource {

    @XmlElement(required = true)
    protected List<Ssh> ssh;
    protected Env env;
    protected EndPoint endPoint;
    @XmlElement(required = true)
    protected Scratch scratch;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the ssh property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ssh property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSsh().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ssh }
     * 
     * 
     */
    public List<Ssh> getSsh() {
        if (ssh == null) {
            ssh = new ArrayList<Ssh>();
        }
        return this.ssh;
    }

    /**
     * Gets the value of the env property.
     * 
     * @return
     *     possible object is
     *     {@link Env }
     *     
     */
    public Env getEnv() {
        return env;
    }

    /**
     * Sets the value of the env property.
     * 
     * @param value
     *     allowed object is
     *     {@link Env }
     *     
     */
    public void setEnv(Env value) {
        this.env = value;
    }

    /**
     * Gets the value of the endPoint property.
     * 
     * @return
     *     possible object is
     *     {@link EndPoint }
     *     
     */
    public EndPoint getEndPoint() {
        return endPoint;
    }

    /**
     * Sets the value of the endPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndPoint }
     *     
     */
    public void setEndPoint(EndPoint value) {
        this.endPoint = value;
    }

    /**
     * Gets the value of the scratch property.
     * 
     * @return
     *     possible object is
     *     {@link Scratch }
     *     
     */
    public Scratch getScratch() {
        return scratch;
    }

    /**
     * Sets the value of the scratch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scratch }
     *     
     */
    public void setScratch(Scratch value) {
        this.scratch = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
