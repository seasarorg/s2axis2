package org.seasar.remoting.axis2.deployment.impl;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.deployment.JAXWSServiceBuilder;
import org.seasar.remoting.axis2.mock.impl.JAXWSSample1;

public class JAXWSServiceBuilderImplTest extends S2TestCase {

    S2Container          container;

    JAXWSServiceBuilder  builder;

    ConfigurationContext configCtx;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test-jaxws.dicon");

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPopulateService_noInterface() {
        ComponentDef componentDef = this.container.getComponentDef(JAXWSSample1.class);
        AxisService service = this.builder.populateService(this.configCtx,
                componentDef);

        assertEquals("JAXWSSample1Service.JAXWSSample1Port", service.getName());
        assertEquals("http://impl.mock.axis2.remoting.seasar.org/",
                service.getTargetNamespace());
        assertEquals("http://impl.mock.axis2.remoting.seasar.org",
                service.getSchematargetNamespace());
        assertNotNull(service.getOperationByAction("echo"));
    }

}
