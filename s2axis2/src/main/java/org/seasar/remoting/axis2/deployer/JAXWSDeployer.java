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
package org.seasar.remoting.axis2.deployer;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.remoting.axis2.deployment.JAXWSServiceBuilder;

/**
 * 
 * @author takanori
 */
public class JAXWSDeployer extends AxisServiceDeployer {

    private JAXWSServiceBuilder jaxwsServiceBuilder;

    /**
     * デフォルトのコンストラクタです。
     */
    public JAXWSDeployer() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(ConfigurationContext configCtx,
                       ComponentDef componentDef,
                       MetaDef metaDef) {

        AxisService axisService = this.jaxwsServiceBuilder.populateService(
                configCtx, componentDef);
        deployAxisService(configCtx.getAxisConfiguration(), axisService);
    }

    public void setJaxwsServiceBuilder(JAXWSServiceBuilder jaxwsServiceBuilder) {
        this.jaxwsServiceBuilder = jaxwsServiceBuilder;
    }

}
