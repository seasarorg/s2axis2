
package org.seasar.remoting.axis2.examples.jaxws.ex02.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "add", namespace = "http://ex02.jaxws.examples.axis2.remoting.seasar.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "add", namespace = "http://ex02.jaxws.examples.axis2.remoting.seasar.org/", propOrder = {
    "value1",
    "value2"
})
public class Add {

    @XmlElement(name = "value1", namespace = "")
    private int value1;
    @XmlElement(name = "value2", namespace = "")
    private int value2;

    /**
     * 
     * @return
     *     returns int
     */
    public int getValue1() {
        return this.value1;
    }

    /**
     * 
     * @param value1
     *     the value for the value1 property
     */
    public void setValue1(int value1) {
        this.value1 = value1;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getValue2() {
        return this.value2;
    }

    /**
     * 
     * @param value2
     *     the value for the value2 property
     */
    public void setValue2(int value2) {
        this.value2 = value2;
    }

}
