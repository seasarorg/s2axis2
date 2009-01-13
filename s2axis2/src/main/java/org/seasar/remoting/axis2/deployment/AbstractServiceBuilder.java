/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.deployment.repository.util.ArchiveReader;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.WSDL11ToAxisServiceBuilder;
import org.apache.axis2.description.WSDL20ToAxisServiceBuilder;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.description.WSDLToAxisServiceBuilder;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.jaxws.server.JAXWSMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.apache.axis2.util.XMLUtils;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.remoting.axis2.deployer.DeployFailedException;
import org.seasar.remoting.axis2.util.AxisServiceUtil;

/**
 * AxisServiceを構築する抽象クラスです。
 * 
 * @author takanori
 */
public abstract class AbstractServiceBuilder {

    /** ServiceObjectSupplierのクラス */
    protected Class             serviceObjectSupplierClass;

    private static final Logger logger = Logger.getLogger(AbstractServiceBuilder.class);

    /**
     * デフォルトのコンストラクタ。
     */
    public AbstractServiceBuilder() {}

    /**
     * 指定されたAxisServiceにParameterを設定します。
     * 
     * @param service AxisService
     */
    protected void buildServiceParameter(AxisService service) {
        // Service Object Supplier Class
        Parameter paramServiceObjectSupplier = new Parameter(
                Constants.SERVICE_OBJECT_SUPPLIER,
                this.serviceObjectSupplierClass.getName());
        try {
            service.addParameter(paramServiceObjectSupplier);
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        }
    }

    /**
     * RESTでの処理用に、S2Axis独自のMessageBuilder／Formatterを適用します。<br>
     * AxisServiceを生成するまえに、AxisConfigurationに適用する必要があります。<br>
     * 
     * @param axisConfig AxisConfiguration
     */
    protected void buildMessageBuilder(AxisConfiguration axisConfig) {
    // MessageBuilder／Formatterの適用を行っていたが、
    // Axis2本体が修正となったため、処理不要。
    }

    /**
     * 指定されたAxisServiceに、MessageReceiverを追加します。<br>
     * 
     * @param service AxisService
     * @param msgReceivers MessageReceiverのマップ
     */
    protected void addMessageReceiver(AxisService service,
                                      Map<String, Class> msgReceivers) {
        if (service == null || msgReceivers == null) {
            return;
        }

        Object[] mepArray = msgReceivers.keySet().toArray();
        for (int i = 0; i < mepArray.length; i++) {
            Object mep = mepArray[i];
            Class receiverClass = msgReceivers.get(mep);

            if ((mep instanceof String)
                    && (MessageReceiver.class.isAssignableFrom(receiverClass))) {

                MessageReceiver msgReceiver = (MessageReceiver)ClassUtil.newInstance(receiverClass);
                service.addMessageReceiver((String)mep, msgReceiver);

                if (logger.isDebugEnabled()) {
                    Object[] args = new Object[] { service.getName(), mep,
                            receiverClass };
                    logger.log("DAXS0005", args);
                }
            }

        }
    }

    /**
     * RPC用のMessageReceiver定義を生成します。
     * 
     * @return MessageReceiverのMap
     */
    protected Map<String, Class> createMessageReceiverDefAtRPC() {

        Map<String, Class> messageReceivers = new HashMap<String, Class>();

        messageReceivers.put(WSDL2Constants.MEP_URI_IN_OUT,
                RPCMessageReceiver.class);
        messageReceivers.put(WSDL2Constants.MEP_URI_IN_ONLY,
                RPCInOnlyMessageReceiver.class);

        return messageReceivers;
    }

    /**
     * JAX-WS用のMessageReceiver定義を生成します。
     * 
     * @return MessageReceiverのMap
     */
    protected Map<String, Class> createMessageReceiverDefAtJAXWS() {

        Map<String, Class> messageReceivers = new HashMap<String, Class>();

        messageReceivers.put(WSDL2Constants.MEP_URI_IN_OUT,
                JAXWSMessageReceiver.class);
        messageReceivers.put(WSDL2Constants.MEP_URI_IN_ONLY,
                RPCInOnlyMessageReceiver.class);

        return messageReceivers;
    }

    /**
     * 指定されたサービスに、カスタムWSDLを適用します。
     * 
     * @param axisService AxisService
     */
    public void buildCustomWSDL(AxisService axisService) {

        String serviceName = axisService.getName();
        File wsdlFile = AxisServiceUtil.getWSDLResource(serviceName);

        if (wsdlFile == null) {
            throw new DeployFailedException("EAXS00010",
                    new Object[] { serviceName });
        }

        String wsdlUri = wsdlFile.toURI().toString();

        try {
            InputStream in = new FileInputStream(wsdlFile);
            OMNamespace documentElementNS = ((OMElement)XMLUtils.toOM(in)).getNamespace();

            String nsURI = documentElementNS.getNamespaceURI();
            WSDLToAxisServiceBuilder wsdlToAxisServiceBuilder;

            // WSDLのバージョンに合わせて処理する。
            if (org.apache.axis2.namespace.Constants.NS_URI_WSDL11.equals(nsURI)) {
                // WSDL 1.1
                InputStream wsdlIn = new FileInputStream(wsdlFile);
                wsdlToAxisServiceBuilder = new WSDL11ToAxisServiceBuilder(
                        wsdlIn, axisService);
            } else if (WSDL2Constants.WSDL_NAMESPACE.equals(nsURI)) {
                // WSDL 2.0
                wsdlToAxisServiceBuilder = new WSDL20ToAxisServiceBuilder(
                        wsdlUri, axisService);
            } else {
                throw new DeployFailedException("EAXS0012", new Object[] {
                        serviceName, wsdlFile });
            }

            wsdlToAxisServiceBuilder.setBaseUri(wsdlUri);
            wsdlToAxisServiceBuilder.populateService();
        } catch (FileNotFoundException ex) {
            throw new DeployFailedException("EAXS0012", new Object[] {
                    serviceName, wsdlFile });
        } catch (OMException ex) {
            throw new DeployFailedException("EAXS0012", new Object[] {
                    serviceName, wsdlFile });
        } catch (XMLStreamException ex) {
            throw new DeployFailedException("EAXS0012", new Object[] {
                    serviceName, wsdlFile });
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0012", new Object[] {
                    serviceName, wsdlFile });
        }

        if (logger.isDebugEnabled()) {
            logger.log("DAXS0009", new Object[] { serviceName, wsdlFile });
        }
    }

    /**
     * WSDLファイルを適用した、AxisServiceを生成します。<br>
     * WSDLファイルは、サービス名と同じファイル名のものが適用されます。
     * 
     * @param serviceName サービス名
     * @return AxisService
     */
    public AxisService createWsdlService(String serviceName) {

        AxisService axisService = new AxisService(serviceName);
        buildCustomWSDL(axisService);

        return axisService;
    }

    /**
     * WSDLファイルを読み込みし、AxisServiceのマップを生成します。<br>
     * WSDLファイルは、クラスパスのルート直下にあるMETA-INFディレクトリにあるもの全てを読み込みます。
     * 
     * @return AxisServiceのマップ
     */
    public Map<String, AxisService> createWsdlServiceMap() {

        ArchiveReader archiveReader = new ArchiveReader();
        HashMap<String, AxisService> servicesMap = new HashMap<String, AxisService>();

        // クラスパスのルートを基準として、WSDLファイルを配置しているディレクトリを指定する。
        File wsdlRoot = ResourceUtil.getResourceAsFileNoException("META-INF"
                + File.separator);
        if (!wsdlRoot.exists()) {
            return servicesMap;
        }

        try {
            archiveReader.processFilesInFolder(wsdlRoot, servicesMap);
        } catch (DeploymentException ex) {
            throw new DeployFailedException("EAXS0011",
                    new Object[] { wsdlRoot });
        } catch (FileNotFoundException ex) {
            throw new DeployFailedException("EAXS0011",
                    new Object[] { wsdlRoot });
        } catch (XMLStreamException ex) {
            throw new DeployFailedException("EAXS0011",
                    new Object[] { wsdlRoot });
        }

        return servicesMap;
    }

    public void setServiceObjectSupplierClass(Class serviceObjectSupplierClass) {
        this.serviceObjectSupplierClass = serviceObjectSupplierClass;
    }

}
