/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
