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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.WSDL11ToAxisServiceBuilder;
import org.apache.axis2.description.WSDL20ToAxisServiceBuilder;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.description.WSDLToAxisServiceBuilder;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.util.XMLUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.axis2.util.AxisServiceUtil;

/**
 * コンポーネントをAxisにデプロイする抽象クラスです。
 * 
 * @author takanori
 */
public abstract class AxisServiceDeployer {

    private static final Logger logger = Logger.getLogger(AxisServiceDeployer.class);

    /**
     * 指定されたコンポーネントをデプロイします。
     * 
     * @param configCtx Axisの設定情報
     * @param componentDef コンポーネント定義
     * @param metaDef メタデータ
     */
    public abstract void deploy(ConfigurationContext configCtx,
                                ComponentDef componentDef,
                                MetaDef metaDef);

    /**
     * 指定されたAxisSerivceをデプロイします。
     * 
     * @param axisConfig AxisConfiguration
     * @param axisService サービス
     */
    protected void deployAxisService(AxisConfiguration axisConfig,
                                     AxisService axisService) {

        setupCustomWSDL(axisService);

        try {
            axisService.setWsdlFound(true);
            axisConfig.addService(axisService);

            logger.log("IAXS0001", new Object[] { axisService.getName() });
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { axisService.getName() }, ex);
        }
    }

    /**
     * 
     * @param axisService
     */
    protected void setupCustomWSDL(AxisService axisService) {

        if (axisService == null || !axisService.isUseUserWSDL()) {
            return;
        }

        String serviceName = axisService.getName();
        String serviceClassName = (String)axisService.getParameterValue(Constants.SERVICE_CLASS);
        File wsdlFile = AxisServiceUtil.getWSDLResource(serviceName,
                serviceClassName);

        if (wsdlFile == null) {
            throw new DeployFailedException("EAXS0009",
                    new Object[] { serviceName });
        }

        String wsdlUri = wsdlFile.toURI().toString();

        try {
            InputStream in = new FileInputStream(wsdlFile);
            OMNamespace documentElementNS = ((OMElement)XMLUtils.toOM(in)).getNamespace();

            String nsURI = documentElementNS.getNamespaceURI();
            WSDLToAxisServiceBuilder wsdlToAxisServiceBuilder;

            // WSDLのバージョンに合わせて処理する。
            if (Constants.NS_URI_WSDL11.equals(nsURI)) {
                // WSDL 1.1
                InputStream wsdlIn = new FileInputStream(wsdlFile);
                wsdlToAxisServiceBuilder = new WSDL11ToAxisServiceBuilder(
                        wsdlIn, axisService);
            } else if (WSDL2Constants.WSDL_NAMESPACE.equals(nsURI)) {
                // WSDL 2.0
                wsdlToAxisServiceBuilder = new WSDL20ToAxisServiceBuilder(
                        wsdlUri, axisService);
            } else {
                throw new DeployFailedException("EAXS0010", new Object[] {
                        serviceName, wsdlFile });
            }

            wsdlToAxisServiceBuilder.setBaseUri(wsdlUri);
            wsdlToAxisServiceBuilder.populateService();
        } catch (FileNotFoundException ex) {
            throw new DeployFailedException("EAXS0010", new Object[] {
                    serviceName, wsdlFile });
        } catch (OMException ex) {
            throw new DeployFailedException("EAXS0010", new Object[] {
                    serviceName, wsdlFile });
        } catch (XMLStreamException ex) {
            throw new DeployFailedException("EAXS0010", new Object[] {
                    serviceName, wsdlFile });
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0010", new Object[] {
                    serviceName, wsdlFile });
        }

        if (logger.isDebugEnabled()) {
            logger.log("DAXS0008", new Object[] { serviceName, wsdlFile });
        }
    }
}
