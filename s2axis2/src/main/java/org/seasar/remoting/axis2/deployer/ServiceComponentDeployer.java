/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
public class ServiceComponentDeployer extends AxisServiceDeployer {

    private ComponentBasedServiceBuilder componentBasedServiceBuilder;

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

        AxisService axisService = createService(configCtx, componentDef,
                metaDef);
        deployAxisService(configCtx.getAxisConfiguration(), axisService);
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

        Object metaData;
        if (metaDef != null) {
            metaData = metaDef.getValue();
        } else {
            metaData = null;
        }

        AxisService service;
        if (metaData == null) {
            service = this.componentBasedServiceBuilder.populateService(
                    configCtx, componentDef);

        } else if (metaData instanceof ServiceDef) {
            ServiceDef serviceDef = (ServiceDef)metaData;
            service = this.componentBasedServiceBuilder.populateService(
                    configCtx, componentDef, serviceDef);

        } else {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { componentDef.getComponentName() });
        }

        return service;
    }

    public void setComponentBasedServiceBuilder(ComponentBasedServiceBuilder serviceBuilder) {
        this.componentBasedServiceBuilder = serviceBuilder;
    }

}
