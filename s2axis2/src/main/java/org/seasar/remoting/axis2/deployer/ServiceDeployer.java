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

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.DeployFailedException;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.deployment.S2XmlBasedServiceBuilder;
import org.seasar.remoting.axis2.deployment.ServiceBuilder;

/**
 * S2コンテナで管理されているコンポーネントを、Axisにデプロイします。<br>
 * このクラスでは、コンポーネント定義をもとに、<code>AxisService</code>を生成します。<br>
 * 生成されたサービスは、デプロイ時に指定される<code>ConfigurationContext</code>が保持する<code>AxisConfiguration</code>に登録されます。<br>
 * そうすることで、<code>AxisServlet</code>がサービスをデプロイします。
 * 
 * @author takanori
 */
public class ServiceDeployer implements ItemDeployer {

    private ServiceBuilder           serviceBuilder         = null;

    private S2XmlBasedServiceBuilder xmlBasedServiceBuilder = new S2XmlBasedServiceBuilder();

    private static final Logger      logger                 = Logger.getLogger(ServiceDeployer.class);

    /**
     * デフォルトのコンストラクタです。
     */
    public ServiceDeployer() {}

    /**
     * {@inheritDoc}
     */
    public void deploy(ConfigurationContext configCtx,
                       ComponentDef componentDef,
                       MetaDef metaDef) {

        List serviceList = createService(configCtx, componentDef, metaDef);

        for (int i = 0; serviceList != null && i < serviceList.size(); i++) {

            AxisService service = (AxisService) serviceList.get(i);

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
     * @return <code>AxisService</code>のリスト
     */
    protected List createService(ConfigurationContext configCtx,
                                 ComponentDef componentDef,
                                 MetaDef metaDef) {

        Object metaData = metaDef.getValue();

        List serviceList;
        if (metaData == null) {
            AxisService service = this.serviceBuilder.populateService(
                    configCtx, componentDef);

            serviceList = new ArrayList();
            serviceList.add(service);
        } else if (metaData instanceof ServiceDef) {
            ServiceDef serviceDef = (ServiceDef) metaData;
            AxisService service = this.serviceBuilder.populateService(
                    configCtx, componentDef, serviceDef);

            serviceList = new ArrayList();
            serviceList.add(service);
        } else if (metaData instanceof String && componentDef == null) {
            String servicexmlPath = (String) metaData;
            serviceList = this.xmlBasedServiceBuilder.populateService(
                    configCtx, servicexmlPath);
        } else {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { componentDef.getComponentName() });
        }

        return serviceList;
    }

    public ServiceBuilder getServiceBuilder() {
        return this.serviceBuilder;
    }

    public void setServiceBuilder(ServiceBuilder serviceBuilder) {
        this.serviceBuilder = serviceBuilder;
    }

}
