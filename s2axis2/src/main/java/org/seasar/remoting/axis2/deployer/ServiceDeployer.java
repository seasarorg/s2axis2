/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.util.ArrayList;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.apache.axis2.wsdl.WSDLConstants;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.DeployFailedException;

/**
 * 
 * @author takanori
 * 
 */
public class ServiceDeployer implements ItemDeployer {

    private AxisDeployer        deployer = null;

    private static final Logger logger   = Logger.getLogger(ServiceDeployer.class);

    public ServiceDeployer(AxisDeployer deployer) {
        this.deployer = deployer;
    }

    public void deploy(ComponentDef componentDef, MetaDef metaDef) {

        AxisService service = createService(componentDef, metaDef);
        try {
            this.deployer.getAxisConfig().addService(service);

            if (logger.isDebugEnabled()) {
                logger.log("DAXS0003", new Object[] { service.getName() });
            }
        }
        catch (AxisFault ex) {
            throw new DeployFailedException(ex);
        }

    }

    protected AxisService createService(ComponentDef componentDef,
            MetaDef metaDef) {

        String className = componentDef.getComponentClass().getName();
        Parameter parameter = new Parameter(Constants.SERVICE_CLASS, className);
        AxisService service = new AxisService(componentDef.getComponentName());

        service.addMessageReceiver(WSDLConstants.MEP_URI_IN_OUT,
                                   new RPCMessageReceiver());
        service.addMessageReceiver(WSDLConstants.MEP_URI_IN_ONLY,
                                   new RPCInOnlyMessageReceiver());

        try {
            service.addParameter(parameter);
            service.setClassLoader(Thread.currentThread().getContextClassLoader());

            Utils.fillAxisService(service, this.deployer.getAxisConfig(), new ArrayList());
        }
        catch (AxisFault ex) {
            throw new DeployFailedException(ex);
        }
        catch (Exception ex) {
            throw new DeployFailedException(ex);
        }

        return service;
    }

}
