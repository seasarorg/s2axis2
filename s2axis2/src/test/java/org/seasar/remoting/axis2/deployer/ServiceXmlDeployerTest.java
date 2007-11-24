package org.seasar.remoting.axis2.deployer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.MetaDefImpl;

public class ServiceXmlDeployerTest extends S2TestCase {

    ServiceXmlDeployer   deployer;

    ConfigurationContext configCtx;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test-servicexml.dicon");

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeploy_service() throws AxisFault {
        MetaDef metaDef = new MetaDefImpl("axis-deploy",
                "org/seasar/remoting/axis2/deployment/SampleService01.xml");
        this.deployer.deploy(this.configCtx, null, metaDef);

        AxisService service = this.configCtx.getAxisConfiguration().getService(
                "ServiceSample");
        assertNotNull(service);
        assertTrue(service.isWsdlFound());
    }

    public void testDeploy_serviceGroup() throws AxisFault {
        MetaDef metaDef = new MetaDefImpl("axis-deploy",
                "org/seasar/remoting/axis2/deployment/SampleServiceGroup01.xml");
        this.deployer.deploy(this.configCtx, null, metaDef);

        AxisService service1 = this.configCtx.getAxisConfiguration().getService(
                "ServiceSample1");
        assertNotNull(service1);
        assertTrue(service1.isWsdlFound());

        AxisService service2 = this.configCtx.getAxisConfiguration().getService(
                "ServiceSample2");
        assertNotNull(service2);
        assertTrue(service2.isWsdlFound());
    }

    public void testDeploy_nullMetaDef() {
        try {
            this.deployer.deploy(this.configCtx, null, null);

            fail();
        } catch (DeployFailedException ex) {
            System.err.println(ex.getMessage());
            assertEquals("EAXS0002", ex.getMessageCode());
        }
    }

    public void testDeploy_invalidMetaDef() {
        MetaDef metaDef = new MetaDefImpl("axis-deploy", Integer.valueOf(0));

        try {
            this.deployer.deploy(this.configCtx, null, metaDef);

            fail();
        } catch (DeployFailedException ex) {
            System.err.println(ex.getMessage());
            assertEquals("EAXS0002", ex.getMessageCode());
        }
    }

}