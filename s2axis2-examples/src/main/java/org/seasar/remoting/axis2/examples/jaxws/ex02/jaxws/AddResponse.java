
package org.seasar.remoting.axis2.examples.jaxws.ex02.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "addResponse", namespace = "http://ex02.jaxws.examples.axis2.remoting.seasar.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addResponse", namespace = "http://ex02.jaxws.examples.axis2.remoting.seasar.org/")
public class AddResponse {

    @XmlElement(name = "return", namespace = "")
    private int _return;

    /**
     * 
     * @return
     *     returns int
     */
    public int get_return() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void set_return(int _return) {
        this._return = _return;
    }

}
