//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.13 at 02:10:11 PM CEST 
//


package sicd.alma.gen.users;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * A specific user's block.
 * 
 * <p>Java class for user_block complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="user_block">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="block_type" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;>string255Length">
 *                 &lt;attribute name="desc" type="{}string4000Length" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="block_description">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;>string255Length">
 *                 &lt;attribute name="desc" type="{}string4000Length" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="block_status" type="{}string255Length" minOccurs="0"/>
 *         &lt;element name="block_note" type="{}string2000Length" minOccurs="0"/>
 *         &lt;element name="created_by" type="{}string255Length" minOccurs="0"/>
 *         &lt;element name="created_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="expiry_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="segment_type" type="{}string10Length" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user_block", propOrder = {

})
public class UserBlock {

    @XmlElement(name = "block_type")
    protected UserBlock.BlockType blockType;
    @XmlElement(name = "block_description", required = true)
    protected UserBlock.BlockDescription blockDescription;
    @XmlElement(name = "block_status")
    protected String blockStatus;
    @XmlElement(name = "block_note")
    protected String blockNote;
    @XmlElement(name = "created_by")
    protected String createdBy;
    @XmlElement(name = "created_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDate;
    @XmlElement(name = "expiry_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expiryDate;
    @XmlAttribute(name = "segment_type")
    protected String segmentType;

    /**
     * Gets the value of the blockType property.
     * 
     * @return
     *     possible object is
     *     {@link UserBlock.BlockType }
     *     
     */
    public UserBlock.BlockType getBlockType() {
        return blockType;
    }

    /**
     * Sets the value of the blockType property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserBlock.BlockType }
     *     
     */
    public void setBlockType(UserBlock.BlockType value) {
        this.blockType = value;
    }

    /**
     * Gets the value of the blockDescription property.
     * 
     * @return
     *     possible object is
     *     {@link UserBlock.BlockDescription }
     *     
     */
    public UserBlock.BlockDescription getBlockDescription() {
        return blockDescription;
    }

    /**
     * Sets the value of the blockDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserBlock.BlockDescription }
     *     
     */
    public void setBlockDescription(UserBlock.BlockDescription value) {
        this.blockDescription = value;
    }

    /**
     * Gets the value of the blockStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlockStatus() {
        return blockStatus;
    }

    /**
     * Sets the value of the blockStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlockStatus(String value) {
        this.blockStatus = value;
    }

    /**
     * Gets the value of the blockNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlockNote() {
        return blockNote;
    }

    /**
     * Sets the value of the blockNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlockNote(String value) {
        this.blockNote = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the createdDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the value of the createdDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedDate(XMLGregorianCalendar value) {
        this.createdDate = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiryDate(XMLGregorianCalendar value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the segmentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegmentType() {
        return segmentType;
    }

    /**
     * Sets the value of the segmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegmentType(String value) {
        this.segmentType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;>string255Length">
     *       &lt;attribute name="desc" type="{}string4000Length" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class BlockDescription {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "desc")
        protected String desc;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the desc property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Sets the value of the desc property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDesc(String value) {
            this.desc = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;>string255Length">
     *       &lt;attribute name="desc" type="{}string4000Length" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class BlockType {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "desc")
        protected String desc;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the desc property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Sets the value of the desc property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDesc(String value) {
            this.desc = value;
        }

    }

}
