/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis2.examples.jaxws.ex02;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.seasar.remoting.axis2.examples.jaxws.ex02.client.Calc;
import org.seasar.remoting.axis2.examples.jaxws.ex02.client.CalcService;

import junit.framework.TestCase;

/**
 * @author takanori
 */
public class CalcServiceTest extends TestCase {

    private URL    url;

    private String targetNamespace;

    private String portName;

    public CalcServiceTest(String name) {
        super(name);
    }

    public void setUp() throws MalformedURLException {
        this.targetNamespace = "http://ex02.jaxws.examples.axis2.remoting.seasar.org/";
        this.portName = "CalcService";
        this.url = new URL("http://localhost:8080/s2axis2-examples/services/"
                + this.portName + "?wsdl");
    }

    public void testAdd() throws MalformedURLException {

        // wsgenでオプションを指定しない場合、serivceNmae,portNameが変わるため、
        // Axis2のデフォルトの命名規則で明示的に指定。
        CalcService service = new CalcService(this.url, new QName(
                this.targetNamespace, this.portName));
        Calc calc = service.getPort(new QName(targetNamespace, this.portName
                + "HttpSoap11Endpoint"), Calc.class);

        int actual = calc.add(1, 2);

        assertEquals(3, actual);
    }
}
