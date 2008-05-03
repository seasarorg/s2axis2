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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.mock.SampleService;
import org.seasar.remoting.axis2.mock.ServiceMock;
import org.seasar.remoting.axis2.mock.impl.ServiceMockImpl;
import org.seasar.remoting.axis2.mock.impl.ServiceMockImpl2;

public class ComponentBasedServiceBuilderImplTest extends S2TestCase {

    private static final String              TARGET_NAMESPACE = "http://test/axis2";

    private static final String              SCHEMA_NAMESPACE = "http://test/xsd";

    private static List<String>              interfaceMethods = new ArrayList<String>();

    private static List<String>              implMethods      = new ArrayList<String>();
    static {
        interfaceMethods.add("method1");
        interfaceMethods.add("method2");
        interfaceMethods.add("method3");

        implMethods.add("method1");
        implMethods.add("method2");
        implMethods.add("method3");
        implMethods.add("setParam1");
        implMethods.add("setParam2");
    }

    private S2Container                      container;

    private ComponentBasedServiceBuilderImpl builder;

    private ServiceDef                       serviceDef;

    private ConfigurationContext             configCtx;

    @Override
    protected void setUp() throws Exception {
        include("s2axis2-test.dicon");

        this.serviceDef = new ServiceDef();
        this.serviceDef.setServiceType(ServiceMock.class);
        this.serviceDef.setTargetNamespace(TARGET_NAMESPACE);
        this.serviceDef.setSchemaNamespace(SCHEMA_NAMESPACE);

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    @Override
    protected void tearDown() throws Exception {}

    public void testPopulateService_implClass() {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");
        AxisService service = this.builder.populateService(this.configCtx,
                componentDef);

        List<String> opeList = extractOpNames(service);

        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getTargetNamespace());
        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getSchemaTargetNamespace());
        boolean equals = equalsList(interfaceMethods, opeList);
        assertTrue(equals);
    }

    public void testPopulateService_serviceType() {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");

        this.serviceDef.setTargetNamespace(null);
        this.serviceDef.setSchemaNamespace(null);

        AxisService service = this.builder.populateService(this.configCtx,
                componentDef, this.serviceDef);
        List<String> opeList = extractOpNames(service);

        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getTargetNamespace());
        assertEquals("http://mock.axis2.remoting.seasar.org",
                service.getSchemaTargetNamespace());
        boolean equals = equalsList(interfaceMethods, opeList);
        assertTrue(equals);
    }

    public void testPopulateService_ns() {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");

        AxisService service = this.builder.populateService(this.configCtx,
                componentDef, this.serviceDef);
        List<String> opeList = extractOpNames(service);

        assertEquals(TARGET_NAMESPACE, service.getTargetNamespace());
        assertEquals(SCHEMA_NAMESPACE, service.getSchemaTargetNamespace());
        boolean equals = equalsList(interfaceMethods, opeList);
        assertTrue(equals);
    }

    public void testPopulateService_withServieDef() {
        ComponentDef componentDef = this.container.getComponentDef("ServiceDefTest");
        MetaDef metaDef = componentDef.getMetaDef("axis-service");

        AxisService service = this.builder.populateService(this.configCtx,
                componentDef, (ServiceDef)metaDef.getValue());
        List opeList = extractOpNames(service);

        assertEquals("http://examples", service.getTargetNamespace());
        assertEquals("http://examples/xsd", service.getSchemaTargetNamespace());
        assertEquals(0, opeList.size());
    }

    public void testGetServiceType_interface1() {
        Class expected = ServiceMock.class;
        Class actual = this.builder.getServiceType(ServiceMockImpl.class);

        assertEquals(expected, actual);
    }

    public void testGetServiceType_interface2() {
        Class expected = ServiceMockImpl2.class;
        Class actual = this.builder.getServiceType(ServiceMockImpl2.class);

        assertEquals(expected, actual);
    }

    public void testGetServiceType_notImpl() {
        Class expected = SampleService.class;
        Class actual = this.builder.getServiceType(SampleService.class);

        assertEquals(expected, actual);
    }

    public void testCreateExcludeOperations_success() {
        List<String> expected = new ArrayList<String>();
        expected.add("setParam1");
        expected.add("setParam2");

        List<String> actual = this.builder.createExcludeOperations(
                ServiceMockImpl.class, ServiceMock.class);

        boolean equals = equalsList(expected, actual);
        assertTrue(equals);

    }

    public void testCreateExcludeOperations_invalidServiceType() {

        try {
            List<String> actual = this.builder.createExcludeOperations(
                    ServiceMockImpl.class, String.class);
            fail(actual.toString());
        } catch (Exception ex) {
            assertTrue(ex.getMessage(), true);
        }

    }

    public void testCreateExcludeOperations_typeNull() {

        List<String> actual = this.builder.createExcludeOperations(
                ServiceMockImpl.class, null);

        assertEquals(0, actual.size());

    }

    public void testCreateSchemaNamespace_success() {
        String expected = "http://mock.axis2.remoting.seasar.org";
        String actual = this.builder.createSchemaNamespace(ServiceMock.class,
                ServiceMock.class.getClassLoader());

        assertEquals(expected, actual);
    }

    public void setBuilder(ComponentBasedServiceBuilderImpl builder) {
        this.builder = builder;
    }

    private List<String> extractOpNames(AxisService service) {
        Iterator ite = service.getOperations();
        List<String> opeList = new ArrayList<String>();
        while (ite.hasNext()) {
            AxisOperation ope = (AxisOperation)ite.next();
            opeList.add(ope.getName().getLocalPart());
        }

        return opeList;
    }

    private boolean equalsList(List<String> list1, List<String> list2) {
        boolean equals = list1.containsAll(list2) && list2.containsAll(list1);

        return equals;
    }

}
