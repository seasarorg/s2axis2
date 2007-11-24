package org.seasar.remoting.axis2.deployer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.mock.impl.JAXWSSample1;

public class JAXWSDeployerTest extends S2TestCase {

    S2Container          container;

    JAXWSDeployer        deployer;

    ConfigurationContext configCtx;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test-jaxws.dicon");

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeploy() throws AxisFault {
        ComponentDef componentDef = this.container.getComponentDef(JAXWSSample1.class);
        this.deployer.deploy(this.configCtx, componentDef, null);

        AxisService service = this.configCtx.getAxisConfiguration().getService(
                "JAXWSSample1Service.JAXWSSample1Port");
        assertNotNull(service);
        assertTrue(service.isWsdlFound());
    }

}