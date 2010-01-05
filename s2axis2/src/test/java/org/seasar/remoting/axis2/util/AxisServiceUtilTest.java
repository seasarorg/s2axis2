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

import java.io.File;

import org.apache.axis2.Constants;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.mock.impl.ServiceMockImpl;

public class AxisServiceUtilTest extends S2TestCase {

    private S2Container container;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-util-test.dicon");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetAxisScope_singleton() {
        ComponentDef componentDef = this.container.getComponentDef("singletionComponent");
        String axisScope = AxisServiceUtil.getAxisScope(componentDef.getInstanceDef());

        assertEquals(Constants.SCOPE_APPLICATION, axisScope);
    }

    public void testGetAxisScope_session() {
        ComponentDef componentDef = this.container.getComponentDef("sessionComponent");
        String axisScope = AxisServiceUtil.getAxisScope(componentDef.getInstanceDef());

        assertEquals(Constants.SCOPE_TRANSPORT_SESSION, axisScope);
    }

    public void testGetAxisScope_request() {
        ComponentDef componentDef = this.container.getComponentDef("requestComponent");
        String axisScope = AxisServiceUtil.getAxisScope(componentDef.getInstanceDef());

        assertEquals(Constants.SCOPE_REQUEST, axisScope);
    }

    public void testGetAxisScope_prototype() {
        ComponentDef componentDef = this.container.getComponentDef("prototypeComponent");
        String axisScope = AxisServiceUtil.getAxisScope(componentDef.getInstanceDef());

        assertEquals(Constants.SCOPE_REQUEST, axisScope);
    }

    public void testGetWSDLResource_fromServiceName() {
        File wsdlFile = AxisServiceUtil.getWSDLResource("ServiceMockFromMetaInf");
        assertNotNull(wsdlFile);
        assertTrue(wsdlFile.exists());
    }

    public void testGetWSDLResource_serviceName_null() {
        File wsdlFile = AxisServiceUtil.getWSDLResource(null);
        assertNull(wsdlFile);
    }

    public void testGetWSDLResource_fromServiceClass() {
        File wsdlFile = AxisServiceUtil.getWSDLResource("ServiceMockFromClass",
                ServiceMockImpl.class.getName());
        assertNotNull(wsdlFile);
        assertTrue(wsdlFile.exists());
    }

    public void testGetWSDLResource_fromMetaInf() {
        File wsdlFile = AxisServiceUtil.getWSDLResource(
                "ServiceMockFromMetaInf", ServiceMockImpl.class.getName());
        assertNotNull(wsdlFile);
        assertTrue(wsdlFile.exists());
    }

    public void testGetWSDLResource_notExist() {
        File wsdlFile = AxisServiceUtil.getWSDLResource("ServiceMockDummy",
                ServiceMockImpl.class.getName());
        assertNull(wsdlFile);
    }

}
