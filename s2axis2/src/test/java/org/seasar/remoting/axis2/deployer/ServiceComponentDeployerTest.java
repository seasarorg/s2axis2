package org.seasar.remoting.axis2.deployer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.MetaDefImpl;
import org.seasar.remoting.axis2.ServiceDef;

public class ServiceComponentDeployerTest extends S2TestCase {

    S2Container              container;

    ServiceComponentDeployer deployer;

    ConfigurationContext     configCtx;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test.dicon");

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeploy_noServiceDef() throws AxisFault {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");
        this.deployer.deploy(this.configCtx, componentDef, null);

        AxisService service = this.configCtx.getAxisConfiguration().getService(
                "ServiceMock");
        assertNotNull(service);
        assertTrue(service.isWsdlFound());
    }

    public void testDeploy_hasServiceDef() throws AxisFault {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");
        MetaDef metaDef = new MetaDefImpl("axis-service", new ServiceDef());

        this.deployer.deploy(this.configCtx, componentDef, metaDef);

        AxisService service = this.configCtx.getAxisConfiguration().getService(
                "ServiceMock");
        assertNotNull(service);
        assertTrue(service.isWsdlFound());
    }

    public void testDeploy_invalidMetaDef() {
        ComponentDef componentDef = this.container.getComponentDef("ServiceMock");
        MetaDef metaDef = new MetaDefImpl("axis-service", new String());

        try {
            this.deployer.deploy(this.configCtx, componentDef, metaDef);
            
            fail();
        } catch (DeployFailedException ex) {
            System.err.println(ex.getMessage());
            assertEquals("EAXS0002", ex.getMessageCode());
        }
    }

}
