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
package org.seasar.remoting.axis2.deployment;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.ServiceBuilder;
import org.apache.axis2.description.AxisService;
import org.seasar.remoting.axis2.deployer.DeployFailedException;

/**
 * <code>service.xml</code>の情報から、<code>AxisService</code>を構築します。<br>
 * このクラスで指定される<code>OMElement</code>は、 <code>service</code>タグで開始されている必要があります。
 * 
 * @author takanori
 */
public class S2ServiceBuilder {

    private ConfigurationContext configCtx;

    /**
     * デフォルトコンストラクタです。
     * 
     * @param configCtx Axisの設定情報
     */
    public S2ServiceBuilder(ConfigurationContext configCtx) {
        this.configCtx = configCtx;
    }

    /**
     * <code>AxisService</code>を生成します。
     * 
     * @param rootElement <code>service.xml</code>のルート要素
     * @return 生成された<code>AxisService</code>
     */
    public AxisService populateService(OMElement rootElement) {
        AxisService service = new AxisService();
        ClassLoader loader = this.configCtx.getAxisConfiguration().getServiceClassLoader();
        service.setClassLoader(loader);

        ServiceBuilder serviceBuilder = new ServiceBuilder(this.configCtx,
                service);

        try {
            service = serviceBuilder.populateService(rootElement);
        } catch (DeploymentException ex) {
            String arg = rootElement.getText().replaceAll("\n", "");
            throw new DeployFailedException("EAXS0003", new Object[] { arg },
                    ex);
        }

        service.setWsdlFound(true);

        return service;
    }
}
