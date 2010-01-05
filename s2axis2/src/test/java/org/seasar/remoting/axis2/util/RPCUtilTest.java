/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.util;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.remoting.axis2.mock.ServiceMock;

public class RPCUtilTest extends TestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.setUp();
    }

    public void testCreateOperationQName_success() throws Exception {

        QName expected = new QName("http://util.axis2.remoting.seasar.org",
                "testCreateOperationQName_success");

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(this.getClass());
        Method method = beanDesc.getMethod("testCreateOperationQName_success");
        QName actual = RPCUtil.createOperationQName(method);

        assertEquals(expected.getNamespaceURI(), actual.getNamespaceURI());
        assertEquals(expected.getLocalPart(), actual.getLocalPart());
        assertEquals(expected.getPrefix(), actual.getPrefix());
    }

    public void testCreateRequest_success1() throws Exception {

        OMElement expected = getOMElement("method1");

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(ServiceMock.class);
        Method method = beanDesc.getMethod("method1");
        OMElement actual = RPCUtil.createRequest(method, new Object[] {});

        assertEquals(expected.toString(), actual.toString());
    }

    public void testCreateRequest_success2() throws Exception {

        OMElement expected = getOMElement("method2");
        OMFactory fac = expected.getOMFactory();
        OMElement arg0OM = fac.createOMElement("arg0", fac.createOMNamespace(
                "", ""));
        arg0OM.setText("test");
        expected.addChild(arg0OM);

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(ServiceMock.class);
        Method method = beanDesc.getMethod("method2",
                new Class[] { String.class });
        OMElement actual = RPCUtil.createRequest(method,
                new Object[] { "test" });

        assertEquals(expected.toString(), actual.toString());
    }

    public void testCreateRequest_success3() throws Exception {

        OMElement expected = getOMElement("method3");
        OMFactory fac = expected.getOMFactory();
        OMElement arg0OM = fac.createOMElement("arg0", fac.createOMNamespace(
                "", ""));
        arg0OM.setText("test");
        expected.addChild(arg0OM);
        OMElement arg1OM = fac.createOMElement("arg1", fac.createOMNamespace(
                "", ""));
        arg1OM.setText("1");
        expected.addChild(arg1OM);

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(ServiceMock.class);
        Method method = beanDesc.getMethod("method3", new Class[] {
                String.class, Integer.class });
        OMElement actual = RPCUtil.createRequest(method, new Object[] { "test",
                Integer.valueOf(1) });

        assertEquals(expected.toString(), actual.toString());
    }

    private static OMElement getOMElement(String operationName) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(
                "http://mock.axis2.remoting.seasar.org", "");
        OMElement operationNameOM = fac.createOMElement(operationName, ns);

        return operationNameOM;
    }

}
