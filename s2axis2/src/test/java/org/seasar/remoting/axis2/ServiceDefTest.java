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
package org.seasar.remoting.axis2;

import java.util.Map;

import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.mock.ServiceMock;

public class ServiceDefTest extends S2TestCase {

    private S2Container container;

    protected void setUp() throws Exception {
        include("s2axis2-test.dicon");
    }

    protected void tearDown() throws Exception {}

    public void testServiceDef_readDicon() {
        ComponentDef componentDef = container.getComponentDef("ServiceDefTest");
        MetaDef metaDef = componentDef.getMetaDef("axis-service");

        ServiceDef serviceDef = (ServiceDef)metaDef.getValue();

        assertEquals(ServiceMock.class, serviceDef.getServiceType());
        assertEquals("http://examples", serviceDef.getTargetNamespace());
        assertEquals("http://examples/xsd", serviceDef.getSchemaNamespace());

        assertTrue(serviceDef.getExcludeOperations().contains("method1"));
        assertTrue(serviceDef.getExcludeOperations().contains("method2"));

        Map receivers = serviceDef.getMessageReceivers();
        Class clazz;
        clazz = (Class)receivers.get("http://www.w3.org/2004/08/wsdl/in-out");
        assertTrue(RPCMessageReceiver.class.isAssignableFrom(clazz));
        clazz = (Class)receivers.get("http://www.w3.org/2004/08/wsdl/in-only");
        assertTrue(RPCInOnlyMessageReceiver.class.isAssignableFrom(clazz));
    }

    public void setContainer(S2Container container) {
        this.container = container;
    }

}
