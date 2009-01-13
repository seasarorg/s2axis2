/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.deployment;

import java.util.Map;
import java.util.Set;

import org.apache.axis2.description.AxisService;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.deployer.DeployFailedException;

public class AbstractServiceBuilderTest extends S2TestCase {

    AbstractServiceBuilder serviceBuilder = new ServiceBuilderMock();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateWsdlService() {
        AxisService actual = this.serviceBuilder.createWsdlService("WSDLServiceTest");

        assertNotNull(actual);
        assertEquals("WSDLServiceTest", actual.getName());
        assertEquals("http://mock.axis2.remoting.seasar.org",
                actual.getTargetNamespace());
        assertTrue(actual.isCustomWsdl());
    }

    public void testCreateWsdlService_null() {
        try {
            AxisService actual = this.serviceBuilder.createWsdlService(null);

            fail(actual.getName());
        } catch (DeployFailedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testCreateWsdlService_invalidService() {
        try {
            AxisService actual = this.serviceBuilder.createWsdlService("dummy");

            fail(actual.getName());
        } catch (DeployFailedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testCreateWsdlServicesMap() {
        Map<String, AxisService> actual = this.serviceBuilder.createWsdlServiceMap();

        assertEquals(2, actual.size());

        Set<String> keySet = actual.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        for (int index = 0; index < keyArray.length; index++) {
            AxisService service = actual.get(keyArray[index]);

            assertEquals(keyArray[index], service.getName());
            assertTrue(service.isCustomWsdl());
        }
    }

    private static class ServiceBuilderMock extends AbstractServiceBuilder {
        public ServiceBuilderMock() {}
    }

}
