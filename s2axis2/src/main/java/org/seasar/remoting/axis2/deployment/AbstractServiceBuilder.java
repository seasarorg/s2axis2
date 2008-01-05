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

import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.jaxws.server.JAXWSMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.remoting.axis2.builder.S2XFormURLEncodedBuilder;
import org.seasar.remoting.axis2.deployer.DeployFailedException;
import org.seasar.remoting.axis2.transport.http.S2XFormURLEncodedFormatter;

/**
 * AxisServiceを構築する抽象クラスです。
 * 
 * @author takanori
 */
public abstract class AbstractServiceBuilder {

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
        // REST用の設定
        axisConfig.addMessageBuilder("application/x-www-form-urlencoded",
                new S2XFormURLEncodedBuilder());
        axisConfig.addMessageFormatter("application/x-www-form-urlencoded",
                new S2XFormURLEncodedFormatter());
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

    public void setServiceObjectSupplierClass(Class serviceObjectSupplierClass) {
        this.serviceObjectSupplierClass = serviceObjectSupplierClass;
    }
}
