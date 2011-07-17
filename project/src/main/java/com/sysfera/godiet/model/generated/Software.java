//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.25 at 11:19:27 AM CEST 
//

package com.sysfera.godiet.model.generated;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.14 at 04:02:37 PM CEST 
//

/**
 * Abstract representation of Diet agent.
 * 
 * 
 * <p>
 * Java class for software complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="software">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="config" type="{http://www.sysfera.com}config"/>
 *         &lt;element name="file" type="{http://www.sysfera.com}softwareFile" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="binary" type="{http://www.sysfera.com}binary" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="stats" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "software", propOrder = { "config", "file", "binary" })
@XmlSeeAlso({ LocalAgent.class, MaDag.class, MasterAgent.class,
		OmniNames.class, Sed.class, Forwarder.class })
public abstract class Software {
	@XmlTransient
	protected Software parent;

	public Software getParent() {
		return parent;
	}

	public void setParent(Software parent) {
		this.parent = parent;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		if (parent instanceof Software)
			this.parent = (Software) parent;
	}

	@XmlElement(required = true)
	protected Config config;
	protected List<SoftwareFile> file;
	protected Binary binary;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;
	@XmlAttribute
	protected Boolean stats;

	/**
	 * Gets the value of the config property.
	 * 
	 * @return possible object is {@link Config }
	 * 
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * Sets the value of the config property.
	 * 
	 * @param value
	 *            allowed object is {@link Config }
	 * 
	 */
	public void setConfig(Config value) {
		this.config = value;
	}

	/**
	 * Gets the value of the file property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the file property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFile().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SoftwareFile }
	 * 
	 * 
	 */
	public List<SoftwareFile> getFile() {
		if (file == null) {
			file = new ArrayList<SoftwareFile>();
		}
		return this.file;
	}

	/**
	 * Gets the value of the binary property.
	 * 
	 * @return possible object is {@link Binary }
	 * 
	 */
	public Binary getBinary() {
		return binary;
	}

	/**
	 * Sets the value of the binary property.
	 * 
	 * @param value
	 *            allowed object is {@link Binary }
	 * 
	 */
	public void setBinary(Binary value) {
		this.binary = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the stats property.
	 * 
	 * @return possible object is {@link Boolean }
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
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setStats(Boolean value) {
		this.stats = value;
	}

}
