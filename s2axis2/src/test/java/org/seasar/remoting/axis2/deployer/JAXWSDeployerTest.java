/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-test-jaxws.dicon");

        this.configCtx = new ConfigurationContext(new AxisConfiguration());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDeploy() throws AxisFault {
        ComponentDef componentDef = this.container.getComponentDef(JAXWSSample1.class);
        this.deployer.deploy(this.configCtx, componentDef, null);

        AxisService service = this.configCtx.getAxisConfiguration().getService(
                "JAXWSSample1Service");
        assertNotNull(service);
        assertTrue(service.isWsdlFound());
    }

}
