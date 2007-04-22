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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axis2.deployment.DescriptionBuilder;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis2.DeployFailedException;

/**
 * <code>service.xml</code>の情報から、<code>AxisService</code>を構築します。<br>
 * 
 * @author takanori
 */
public class S2XmlBasedServiceBuilder {

    /**
     * デフォルトコンストラクタです。
     */
    public S2XmlBasedServiceBuilder() {}

    /**
     * <code>AxisService</code>を生成します。
     * 
     * @param configCtx Axisの設定情報
     * @param servicexmlPath service.xmlのパス（フルパス指定）
     * @return <code>AxisService</code>のリスト
     */
    public List populateService(ConfigurationContext configCtx,
                                String servicexmlPath) {
        InputStream servicexmlStream = ResourceUtil.getResourceAsStream(servicexmlPath);
        if (servicexmlStream == null) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { servicexmlPath });
        }

        DescriptionBuilder builder = new DescriptionBuilder(servicexmlStream,
                configCtx);

        OMElement rootElement;
        try {
            rootElement = builder.buildOM();
        } catch (XMLStreamException ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { servicexmlPath }, ex);
        }

        List serviceList = populateService(configCtx, rootElement);
        if (serviceList == null || serviceList.size() == 0) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { servicexmlPath });
        }

        return serviceList;
    }

    protected List populateService(ConfigurationContext configCtx,
                                   OMElement rootElement) {
        String elementName = rootElement.getLocalName();

        List serviceList;
        if (DeploymentConstants.TAG_SERVICE.equals(elementName)) {
            S2ServiceBuilder serviceBuilder = new S2ServiceBuilder(configCtx);
            AxisService service = serviceBuilder.populateService(rootElement);

            serviceList = new ArrayList();
            serviceList.add(service);
        } else if (DeploymentConstants.TAG_SERVICE_GROUP.equals(elementName)) {
            S2ServiceGroupBuilder serviceGroupBuilder = new S2ServiceGroupBuilder(
                    configCtx);
            serviceList = serviceGroupBuilder.populateService(rootElement);
        } else {
            serviceList = new ArrayList();
        }

        return serviceList;
    }
}
