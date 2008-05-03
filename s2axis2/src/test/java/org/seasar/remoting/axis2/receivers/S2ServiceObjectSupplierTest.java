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
package org.seasar.remoting.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.ServiceObjectSupplier;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.mock.SampleService;
import org.seasar.remoting.axis2.mock.ServiceMock;

public class S2ServiceObjectSupplierTest extends S2TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("S2ServiceObjectSupplierTest.dicon");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetServiceObject_success() throws AxisFault {
        Class serviceClass = SampleService.class;

        AxisService service = new AxisService();
        service.setClassLoader(Thread.currentThread().getContextClassLoader());
        service.addParameter(new Parameter(Constants.SERVICE_CLASS,
                serviceClass.getName()));

        ServiceObjectSupplier supplier = new S2ServiceObjectSupplier();
        Object obj = supplier.getServiceObject(service);

        assertTrue(obj instanceof SampleService);
    }

    public void testGetServiceObject_nullParam() throws AxisFault {

        AxisService service = new AxisService();
        service.setClassLoader(Thread.currentThread().getContextClassLoader());

        ServiceObjectSupplier supplier = new S2ServiceObjectSupplier();
        try {
            supplier.getServiceObject(service);

            fail();
        } catch (AxisFault ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testGetServiceObject_invalidClass() throws AxisFault {
        // diconに登録していないクラス
        Class serviceClass = ServiceMock.class;

        AxisService service = new AxisService();
        service.setName("TestService");
        service.setClassLoader(Thread.currentThread().getContextClassLoader());
        service.addParameter(new Parameter(Constants.SERVICE_CLASS,
                serviceClass.getName()));

        ServiceObjectSupplier supplier = new S2ServiceObjectSupplier();
        try {
            supplier.getServiceObject(service);

            fail();
        } catch (AxisFault ex) {
            System.err.println(ex.getMessage());
        }
    }
}
