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

import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.deployment.XmlBasedServiceBuilder;

/**
 * Axis2のservice.xmlファイルに従って、サービスをデプロイします。<br>
 * 生成されたサービスは、デプロイ時に指定される<code>ConfigurationContext</code>が保持する<code>AxisConfiguration</code>に登録されます。<br>
 * そのようにすることで、<code>AxisServlet</code>がサービスをデプロイします。
 * 
 * @author takanori
 */
public class ServiceXmlDeployer implements ItemDeployer {

    private XmlBasedServiceBuilder xmlBasedServiceBuilder;

    private static final Logger    logger = Logger.getLogger(ServiceComponentDeployer.class);

    /**
     * デフォルトのコンストラクタです。
     */
    public ServiceXmlDeployer() {}

    /**
     * {@inheritDoc}
     */
    public void deploy(ConfigurationContext configCtx,
                       ComponentDef componentDef,
                       MetaDef metaDef) {

        Object metaData = metaDef.getValue();

        List serviceList;
        if (metaData instanceof String) {
            String servicexmlPath = (String) metaData;
            serviceList = createService(configCtx, servicexmlPath);
        } else {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { componentDef.getComponentName() });
        }

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
     * @param servicexmlPath service.xml のパス
     * @return <code>AxisService</code>のリスト
     */
    protected List createService(ConfigurationContext configCtx,
                                 String servicexmlPath) {

        List serviceList = this.xmlBasedServiceBuilder.populateService(
                configCtx, servicexmlPath);

        return serviceList;
    }

    public XmlBasedServiceBuilder getXmlBasedServiceBuilder() {
        return this.xmlBasedServiceBuilder;
    }

    public void setXmlBasedServiceBuilder(XmlBasedServiceBuilder xmlBasedServiceBuilder) {
        this.xmlBasedServiceBuilder = xmlBasedServiceBuilder;
    }

}
