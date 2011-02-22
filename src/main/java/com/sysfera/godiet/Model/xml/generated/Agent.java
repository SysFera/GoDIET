//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.22 at 02:28:34 PM CET 
//


package com.sysfera.godiet.Model.xml.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for agent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="config" type="{}config"/>
 *         &lt;element name="cfg_options" type="{}cfg_options" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="stats" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agent", propOrder = {
    "config",
    "cfgOptions"
})
@XmlSeeAlso({
    MasterAgent.class,
    MaDag.class,
    LocalAgent.class,
    Sed.class
})
public abstract class Agent {

    @XmlElement(required = true)
    protected Config config;
    @XmlElement(name = "cfg_options")
    protected CfgOptions cfgOptions;
    @XmlAttribute
    protected String label;
    @XmlAttribute
    protected Boolean stats;

    /**
     * Gets the value of the config property.
     * 
     * @return
     *     possible object is
     *     {@link Config }
     *     
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets the value of the config property.
     * 
     * @param value
     *     allowed object is
     *     {@link Config }
     *     
     */
    public void setConfig(Config value) {
        this.config = value;
    }

    /**
     * Gets the value of the cfgOptions property.
     * 
     * @return
     *     possible object is
     *     {@link CfgOptions }
     *     
     */
    public CfgOptions getCfgOptions() {
        return cfgOptions;
    }

    /**
     * Sets the value of the cfgOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfgOptions }
     *     
     */
    public void setCfgOptions(CfgOptions value) {
        this.cfgOptions = value;
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
     * Gets the value of the stats property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStats() {
        if (stats == null) {
            return false;
        } else {
            return stats;
        }
    }

    /**
     * Sets the value of the stats property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStats(Boolean value) {
        this.stats = value;
    }

}
