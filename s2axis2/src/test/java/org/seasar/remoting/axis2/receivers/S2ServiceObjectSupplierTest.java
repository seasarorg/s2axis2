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

    protected void setUp() throws Exception {
        super.setUp();
        include("S2ServiceObjectSupplierTest.dicon");
    }

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
