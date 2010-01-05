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
package org.seasar.remoting.axis2.deployment.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.DeploymentConstants;
import org.apache.axis2.deployment.DescriptionBuilder;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis2.deployer.DeployFailedException;
import org.seasar.remoting.axis2.deployment.AbstractServiceBuilder;
import org.seasar.remoting.axis2.deployment.S2ServiceBuilder;
import org.seasar.remoting.axis2.deployment.S2ServiceGroupBuilder;
import org.seasar.remoting.axis2.deployment.XmlBasedServiceBuilder;

/**
 * service.xmlを基に、サービスを構築するクラスです。
 * 
 * @author takanori
 */
public class XmlBasedServiceBuilderImpl extends AbstractServiceBuilder
        implements XmlBasedServiceBuilder {

    private static final Logger logger = Logger.getLogger(XmlBasedServiceBuilderImpl.class);

    /**
     * デフォルトコンストラクタです。
     */
    public XmlBasedServiceBuilderImpl() {}

    /**
     * <code>AxisService</code>を生成します。
     * 
     * @param configCtx Axisの設定情報
     * @param servicexmlPath service.xmlのパス（フルパス指定）
     * @return <code>AxisService</code>のリスト
     */
    public List<AxisService> populateService(ConfigurationContext configCtx,
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

        List<AxisService> serviceList = populateService(configCtx, rootElement);
        if (serviceList == null || serviceList.size() == 0) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { servicexmlPath });
        }

        return serviceList;
    }

    /**
     * <code>AxisService</code>を生成します。
     * 
     * @param configCtx Axisの設定情報
     * @param rootElement rootElement
     * @return <code>AxisService</code>のリスト
     */
    protected List<AxisService> populateService(ConfigurationContext configCtx,
                                                OMElement rootElement) {
        String elementName = rootElement.getLocalName();

        // カスタムWSDLからサービスを生成する。
        Map<String, AxisService> wsdlServiceMap = createWsdlServiceMap();
        HashMap<String, AxisService> wsdlServices;
        if (wsdlServiceMap instanceof HashMap) {
            wsdlServices = (HashMap<String, AxisService>)wsdlServiceMap;
        } else {
            wsdlServices = new HashMap<String, AxisService>(wsdlServiceMap);
        }

        List<AxisService> serviceList;
        if (DeploymentConstants.TAG_SERVICE.equals(elementName)) {
            S2ServiceBuilder serviceBuilder = new S2ServiceBuilder(configCtx);

            AxisService service = serviceBuilder.populateService(rootElement,
                    wsdlServices);

            serviceList = new ArrayList<AxisService>();
            serviceList.add(service);
        } else if (DeploymentConstants.TAG_SERVICE_GROUP.equals(elementName)) {
            S2ServiceGroupBuilder serviceGroupBuilder = new S2ServiceGroupBuilder(
                    configCtx);
            serviceList = serviceGroupBuilder.populateService(rootElement,
                    wsdlServices);
        } else {
            serviceList = new ArrayList<AxisService>();
        }

        // ログ出力
        if (logger.isDebugEnabled()) {
            if (wsdlServiceMap.size() > 0 && serviceList.size() > 0) {
                for (int index = 0; index < serviceList.size(); index++) {
                    AxisService axisService = serviceList.get(index);
                    String serviceName = axisService.getName();

                    if (axisService.isCustomWsdl()
                            && wsdlServiceMap.containsKey(serviceName)) {
                        logger.log("DAXS0008", new Object[] { serviceName });
                    }
                }
            }
        }

        return serviceList;
    }
}
