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
package org.seasar.remoting.axis2.deployment.impl;

import java.util.List;

import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.deployment.XmlBasedServiceBuilder;
import org.seasar.remoting.axis2.receivers.S2ServiceObjectSupplier;

public class XmlBasedServiceBuilderImplTest extends S2TestCase {

    XmlBasedServiceBuilder builder;

    ConfigurationContext   configCtx;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test-servicesxml.dicon");

        this.configCtx = createConfigurationContext();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPopulateService_service1() {
        List serviceList = builder.populateService(this.configCtx,
                "org/seasar/remoting/axis2/deployment/SampleService01.xml");
        assertEquals(1, serviceList.size());

        AxisService service = (AxisService)serviceList.get(0);
        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getTargetNamespace());
        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getSchemaTargetNamespace());
        assertTrue(service.isWsdlFound());
    }

    public void testPopulateService_service2() {
        List serviceList = builder.populateService(this.configCtx,
                "org/seasar/remoting/axis2/deployment/SampleService02.xml");
        assertEquals(1, serviceList.size());

        AxisService service = (AxisService)serviceList.get(0);
        assertEquals(Constants.SCOPE_TRANSPORT_SESSION, service.getScope());
        assertEquals(RPCMessageReceiver.class, service.getMessageReceiver(
                WSDL2Constants.MEP_URI_IN_OUT).getClass());
        assertEquals(
                RPCInOnlyMessageReceiver.class,
                service.getMessageReceiver(WSDL2Constants.MEP_URI_IN_ONLY).getClass());
        assertEquals(S2ServiceObjectSupplier.class.getName(),
                service.getParameterValue(Constants.SERVICE_OBJECT_SUPPLIER));
    }

    public void testPopulateService_fileNotExist() {
        try {
            builder.populateService(this.configCtx,
                    "org/seasar/remoting/axis2/deployment/Dummy.xml");
            fail("not throw Exception");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testPopulateService_serviceGroup1() {
        List serviceList = builder.populateService(this.configCtx,
                "org/seasar/remoting/axis2/deployment/SampleServiceGroup01.xml");
        assertEquals(2, serviceList.size());

        AxisService service1 = (AxisService)serviceList.get(0);
        assertEquals("ServiceSample1", service1.getName());
        assertTrue(service1.isWsdlFound());

        AxisService service2 = (AxisService)serviceList.get(1);
        assertEquals("ServiceSample2", service2.getName());
        assertTrue(service2.isWsdlFound());
    }

    private ConfigurationContext createConfigurationContext() {
        ConfigurationContext configCtx = new ConfigurationContext(
                new AxisConfiguration());

        return configCtx;
    }

}
