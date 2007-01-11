/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.DeployFailedException;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.deployment.ServiceBuilder;

/**
 * 
 * @author takanori
 */
public class ServiceDeployer implements ItemDeployer {

    private AxisDeployer        deployer       = null;

    private ServiceBuilder      serviceBuilder = null;

    private static final Logger logger         = Logger.getLogger(ServiceDeployer.class);

    public ServiceDeployer() {}

    public void deploy(ComponentDef componentDef, MetaDef metaDef) {

        AxisService service = createService(componentDef, metaDef);
        try {
            this.deployer.getAxisConfig().addService(service);

            if (logger.isDebugEnabled()) {
                logger.log("DAXS0001", new Object[] { service.getName() });
            }
        }
        catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0002",
                                            new Object[] { service.getName() },
                                            ex);
        }

    }

    protected AxisService createService(ComponentDef componentDef,
            MetaDef metaDef) {

        Object metaData = metaDef.getValue();

        AxisConfiguration axisConfig = this.deployer.getAxisConfig();
        AxisService service;

        if (metaData == null) {
            service = this.serviceBuilder.populateService(axisConfig,
                                                          componentDef);
        }
        else if (metaData instanceof ServiceDef) {
            ServiceDef serviceDef = (ServiceDef) metaData;
            service = this.serviceBuilder.populateService(axisConfig,
                                                          componentDef,
                                                          serviceDef);
        }
        else {
            throw new DeployFailedException("EAXS0002",
                                            new Object[] { componentDef.getComponentName() });
        }

        return service;
    }

    public AxisDeployer getDeployer() {
        return this.deployer;
    }

    public void setDeployer(AxisDeployer deployer) {
        this.deployer = deployer;
    }

    public ServiceBuilder getServiceBuilder() {
        return this.serviceBuilder;
    }

    public void setServiceBuilder(ServiceBuilder serviceBuilder) {
        this.serviceBuilder = serviceBuilder;
    }

}
