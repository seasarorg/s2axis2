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
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.DeployFailedException;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.deployment.ComponentBasedServiceBuilder;

/**
 * S2コンテナで管理されているコンポーネントを、Axisにデプロイします。<br>
 * このクラスでは、コンポーネント定義をもとに、<code>AxisService</code>を生成します。<br>
 * 生成されたサービスは、デプロイ時に指定される<code>ConfigurationContext</code>が保持する<code>AxisConfiguration</code>に登録されます。<br>
 * そのようにすることで、<code>AxisServlet</code>がサービスをデプロイします。
 * 
 * @author takanori
 */
public class ServiceComponentDeployer implements ItemDeployer {

    private ComponentBasedServiceBuilder componentBasedServiceBuilder;

    private static final Logger          logger = Logger.getLogger(ServiceComponentDeployer.class);

    /**
     * デフォルトのコンストラクタです。
     */
    public ServiceComponentDeployer() {}

    /**
     * {@inheritDoc}
     */
    public void deploy(ConfigurationContext configCtx,
                       ComponentDef componentDef,
                       MetaDef metaDef) {

        AxisService service = createService(configCtx, componentDef, metaDef);
        if (service != null) {
            try {
                configCtx.getAxisConfiguration().addService(service);

                if (logger.isDebugEnabled()) {
                    logger.log("DAXS0001", new Object[] { service.getName() });
                }

            } catch (AxisFault ex) {
                throw new DeployFailedException("EAXS0002",
                        new Object[] { service.getName() }, ex);
            }
        }
    }

    /**
     * サービスを生成います。
     * 
     * @param configCtx Axisの設定情報
     * @param componentDef コンポーネント定義
     * @param metaDef メタデータ
     * @return 生成された<code>AxisService</code>
     */
    protected AxisService createService(ConfigurationContext configCtx,
                                        ComponentDef componentDef,
                                        MetaDef metaDef) {

        Object metaData = metaDef.getValue();

        AxisService service;
        if (metaData == null) {
            service = this.componentBasedServiceBuilder.populateService(
                    configCtx, componentDef);

        } else if (metaData instanceof ServiceDef) {
            ServiceDef serviceDef = (ServiceDef) metaData;
            service = this.componentBasedServiceBuilder.populateService(
                    configCtx, componentDef, serviceDef);

        } else {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { componentDef.getComponentName() });
        }

        return service;
    }

    public ComponentBasedServiceBuilder getComponentBasedServiceBuilder() {
        return this.componentBasedServiceBuilder;
    }

    public void setComponentBasedServiceBuilder(ComponentBasedServiceBuilder serviceBuilder) {
        this.componentBasedServiceBuilder = serviceBuilder;
    }

}
