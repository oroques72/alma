//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.13 at 02:10:11 PM CEST 
//


package sicd.alma.gen.users;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of the user's library notices.
 * 		On SIS synch this field will not be replaced if it was updated manually (or if empty in the incoming user record).
 * 		For external users in PUT action: this field will not be replaced if it was updated manually (or if empty in the incoming user record), unless 'override' parameter is sent with the field's name.
 * 		
 * 
 * <p>Java class for library_notices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="library_notices">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="library_notice" type="{}library_notice" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "library_notices", propOrder = {
    "libraryNotice"
})
public class LibraryNotices {

    @XmlElement(name = "library_notice")
    protected List<LibraryNotice> libraryNotice;

    /**
     * Gets the value of the libraryNotice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryNotice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryNotice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryNotice }
     * 
     * 
     */
    public List<LibraryNotice> getLibraryNotice() {
        if (libraryNotice == null) {
            libraryNotice = new ArrayList<LibraryNotice>();
        }
        return this.libraryNotice;
    }

}