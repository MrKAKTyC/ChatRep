//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.29 at 09:13:58 PM EET 
//


package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conversation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conversation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="friend" type="String" minOccurs="0"/>
 *         &lt;element name="msgs" type="{}message" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "conversation")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conversation", propOrder = {
    "friend",
    "msgs"
})
public class Conversation {

    public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}

	protected String friend;
    @XmlElement(nillable = true)
    protected List<Message> msgs;

    /**
     * Gets the value of the friend property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFriend() {
        return friend;
    }

    /**
     * Sets the value of the friend property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFriend(String value) {
        this.friend = value;
    }

    /**
     * Gets the value of the msgs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msgs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMsgs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Message }
     * 
     * 
     */
    public List<Message> getMsgs() {
        if (msgs == null) {
            msgs = new ArrayList<Message>();
        }
        return this.msgs;
    }

}
