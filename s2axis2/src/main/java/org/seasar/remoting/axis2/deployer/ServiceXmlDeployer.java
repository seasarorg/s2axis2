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

import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.remoting.axis2.deployment.XmlBasedServiceBuilder;

/**
 * Axis2のservices.xmlファイルに従って、サービスをデプロイします。<br>
 * 生成されたサービスは、デプロイ時に指定される<code>ConfigurationContext</code>が保持する<code>AxisConfiguration</code>に登録されます。<br>
 * そのようにすることで、<code>AxisServlet</code>がサービスをデプロイします。
 * 
 * @author takanori
 */
public class ServiceXmlDeployer extends AxisServiceDeployer {

    private XmlBasedServiceBuilder xmlBasedServiceBuilder;

    /**
     * デフォルトのコンストラクタです。
     */
    public ServiceXmlDeployer() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void deploy(ConfigurationContext configCtx,
                       ComponentDef componentDef,
                       MetaDef metaDef) {

        Object metaData;
        if (metaDef != null) {
            metaData = metaDef.getValue();
        } else {
            metaData = null;
        }

        List serviceList;
        if (metaData instanceof String) {
            String servicexmlPath = (String)metaData;
            serviceList = createService(configCtx, servicexmlPath);
        } else {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { metaData });
        }

        for (int i = 0; serviceList != null && i < serviceList.size(); i++) {

            AxisService axisService = (AxisService)serviceList.get(i);
            deployAxisService(configCtx.getAxisConfiguration(), axisService);
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

    public void setXmlBasedServiceBuilder(XmlBasedServiceBuilder xmlBasedServiceBuilder) {
        this.xmlBasedServiceBuilder = xmlBasedServiceBuilder;
    }

}
