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
package org.seasar.remoting.axis2.deployment;

import java.util.HashMap;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.ServiceGroupBuilder;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.seasar.remoting.axis2.deployer.DeployFailedException;

/**
 * <code>service.xml</code>の情報から、<code>AxisService</code>を構築します。<br>
 * このクラスで指定される<code>OMElement</code>は、 <code>serviceGroup</code>タグで開始されている必要があります。
 * 
 * @author takanori
 */
public class S2ServiceGroupBuilder {

    private ConfigurationContext configCtx;

    /**
     * デフォルトコンストラクタです。
     * 
     * @param configCtx Axisの設定情報
     */
    public S2ServiceGroupBuilder(ConfigurationContext configCtx) {
        this.configCtx = configCtx;
    }

    /**
     * <code>AxisService</code>を生成します。
     * 
     * @param rootElement <code>service.xml</code>のルート要素
     * @return 生成された<code>AxisService</code>のリスト
     */
    public List<AxisService> populateService(OMElement rootElement) {
        AxisServiceGroup serviceGroup = new AxisServiceGroup();
        ClassLoader loader = this.configCtx.getAxisConfiguration().getServiceClassLoader();
        serviceGroup.setServiceGroupClassLoader(loader);

        ServiceGroupBuilder serviceGroupBuilder = new ServiceGroupBuilder(
                rootElement, new HashMap(), this.configCtx);

        List<AxisService> serviceList;
        try {
            serviceList = serviceGroupBuilder.populateServiceGroup(serviceGroup);
        } catch (DeploymentException ex) {
            String arg = rootElement.getText().replaceAll("\n", "");
            throw new DeployFailedException("EAXS0003", new Object[] { arg },
                    ex);
        }

        for (int i = 0; serviceList != null && i < serviceList.size(); i++) {
            AxisService service = (AxisService)serviceList.get(i);
            service.setWsdlFound(true);
        }

        return serviceList;
    }
}
