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
package org.seasar.remoting.axis2.deployment.impl;

import java.util.ArrayList;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.jaxws.description.DescriptionFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.remoting.axis2.deployer.DeployFailedException;
import org.seasar.remoting.axis2.deployment.AbstractServiceBuilder;
import org.seasar.remoting.axis2.deployment.JAXWSServiceBuilder;

/**
 * JAX-WSによるサービスの構築を行うクラスです。
 * 
 * @author takanori
 */
public class JAXWSServiceBuilderImpl extends AbstractServiceBuilder implements
        JAXWSServiceBuilder {

    private static final Logger logger = Logger.getLogger(JAXWSServiceBuilderImpl.class);

    /**
     * デフォルトのコンストラクタ。
     */
    public JAXWSServiceBuilderImpl() {}

    /**
     * {@inheritDoc}
     */
    public AxisService populateService(ConfigurationContext configCtx,
                                       ComponentDef componentDef) {

        Class serviceClass = componentDef.getComponentClass();
        AxisService service = DescriptionFactory.createAxisService(serviceClass);

        buildMessageBuilder(configCtx.getAxisConfiguration());
        buildServiceParameter(service);

        // MessageReceiver
        addMessageReceiver(service, createMessageReceiverDefAtJAXWS());

        try {
            Utils.fillAxisService(service, configCtx.getAxisConfiguration(),
                    new ArrayList(), new ArrayList());
        } catch (Exception ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { serviceClass }, ex);
        }

        // FIXME
        // Axis2 1.4 & JDK1.6より前の環境で、JAX-WSのWSDLを生成しようとすると、
        // ClassNotFoundExceptionが発生する。
        // それを防ぐための一時対策。
        // https://issues.apache.org/jira/browse/AXIS2-3693
        Class clazz = null;
        try {
            clazz = ClassUtil.forName("com.sun.tools.internal.ws.spi.WSToolsObjectFactory");
        } catch (Exception ex) {
            // ignore
        }

        if (clazz == null) {
            try {
                clazz = ClassUtil.forName("com.sun.tools.ws.spi.WSToolsObjectFactory");
            } catch (Exception ex) {
                // ignore
            }
        }

        if (clazz == null) {
            Parameter param = service.getParameter("WSDLSupplier");
            if (param != null) {
                try {
                    service.removeParameter(param);
                } catch (Exception ex) {
                    logger.warn(
                            "It's faild that WSDLSupplier is remove from JAXWSService.",
                            ex);
                }
            }
        }

        return service;
    }
}
